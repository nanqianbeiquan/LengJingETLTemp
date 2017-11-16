package notice;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tools.AnYou;

public class DetailCourtNotice {
	static Pattern pattern1 = Pattern.compile("[○O〇一二三四五六七八九(\\d+)]{4}[年-]{1}[一二三四五六七八九十(\\d+)]{1,2}[月-]{1}[一二三四五六七八九十(\\d+)]{1,2}[日]{0,1}");
	static Pattern pattern2 = Pattern.compile("在[\\s\\S]*审理");
	static Pattern pattern4 = Pattern.compile("[（(\\d+)）]{6}[^号]+号");
	public static HashMap<String, String> parse(String content)
	{
		if(content.equals(""))
		{
			return null;
		}
		if(content.endsWith("）"))
		{
			content=content.substring(0,content.lastIndexOf("（"));
		}
		if(!content.endsWith("。"))
		{
			content+="。";
		}
		if(content.endsWith("案件。"))
		{
			content=content.replace("案件。", "一案。");
		}
		HashMap<String,String> parseResult=new HashMap<String,String>();
		String kaiTingRiQi = "";
		String shenLiFaTing="";
		String dangShiRen="";
		String anHao="";
		Pattern pattern3=Pattern.compile("[\u4e00-\u9fa5]{0,}一案");
		Matcher m3 = pattern3.matcher(content);
		String anYou=AnYou.getAnYou(content);
		content=content.replace("公开开庭审理", "审理").replace("公开审理", "审理").replace("公开宣判", "审理").replace("开庭审理", "审理");		
		Matcher m1 = pattern1.matcher(content);
		if (m1.find()) {
			kaiTingRiQi=m1.group();        
        }
		Matcher m2 = pattern2.matcher(content);
		if (m2.find()) {
			shenLiFaTing=m2.group();
			shenLiFaTing=shenLiFaTing.replace("依法", "").replace("在","").replace("本院", "");
        } 
		Matcher m4=pattern4.matcher(content);
		if(m4.find())
		{
			anHao=m4.group();
		}
		if (anHao.indexOf("（")>0)
		{
			anHao=anHao.substring(anHao.indexOf("（"),anHao.length());
		}
		int dangShiRenStartIdx=-1;
		int dangShiRenStopIdx=-1;
		if(dangShiRenStartIdx==-1)
		{
			dangShiRenStartIdx=content.indexOf("宣判");
			if(dangShiRenStartIdx!=-1)
			{
				dangShiRenStartIdx=dangShiRenStartIdx+"宣判".length();
			}
		}
		if(dangShiRenStartIdx==-1)
		{
			dangShiRenStartIdx=content.indexOf("受理的");
			if(dangShiRenStartIdx!=-1)
			{
				dangShiRenStartIdx=dangShiRenStartIdx+"受理的".length();
			}
		}
		
		if(dangShiRenStartIdx==-1)
		{
			dangShiRenStartIdx=content.indexOf("审理");
			if(dangShiRenStartIdx!=-1)
			{
				dangShiRenStartIdx=dangShiRenStartIdx+"审理".length();
			}
		}
		if(dangShiRenStartIdx==-1)
		{
			dangShiRenStartIdx=content.indexOf("询问");
			if(dangShiRenStartIdx!=-1)
			{
				dangShiRenStartIdx=dangShiRenStartIdx+"询问".length();
			}
		}
		if(dangShiRenStartIdx==-1)
		{
			dangShiRenStartIdx=content.indexOf("召开庭前会议");
			if(dangShiRenStartIdx!=-1)
			{
				dangShiRenStartIdx=dangShiRenStartIdx+"召开庭前会议".length();
			}
		}
		if(anYou!=null)
		{
			if(anHao.length()>0 && anYou.length()>0 && content.indexOf("案由")<=0 && content.indexOf("案号")<=0 && content.indexOf(anYou)-content.indexOf(anHao)>0)
			{
				dangShiRenStartIdx=content.indexOf(anHao)+anHao.length();
				dangShiRenStopIdx=content.indexOf(anYou);
			}
			else if(content.indexOf("案号")<content.indexOf("当事人") && content.indexOf("当事人")<content.indexOf("审判长"))
			{
				dangShiRenStartIdx=content.indexOf("当事人")+3;
				dangShiRenStopIdx=content.indexOf("审判长");				
			}
			else
			{
				dangShiRenStopIdx=content.indexOf(anYou);
			}
			anYou=anYou.replace("一案", "");
		}
//		System.out.println(anYou);
//		System.out.println(content);
//		System.out.println(dangShiRenStartIdx);
//		System.out.println(dangShiRenStopIdx);
		if(dangShiRenStartIdx!=-1 && dangShiRenStopIdx!=-1 && dangShiRenStopIdx>dangShiRenStartIdx)
		{
			dangShiRen=content.substring(dangShiRenStartIdx,dangShiRenStopIdx);
			dangShiRen=dangShiRen.replace(" ", "");
		}
		if (dangShiRen.indexOf("审理")>0)
		{
			dangShiRen=dangShiRen.split("审理")[1].replace("审理", "");
		}			
		if(dangShiRen.endsWith("；")|dangShiRen.endsWith("与")|dangShiRen.endsWith("（"))
		{
			dangShiRen=dangShiRen.substring(0, dangShiRen.length()-1);
		
		}	
		if(dangShiRen.startsWith("，"))
		{
			dangShiRen=dangShiRen.substring(1, dangShiRen.length());
		
		}
		dangShiRen=dangShiRen.replace("本院受理", "");
		shenLiFaTing=shenLiFaTing.replace("公开开庭审理", "").replace("开庭审理", "").replace("本院", "").replace("审理", "");
		parseResult.put("案由", anYou);
		parseResult.put("案号", anHao);
		parseResult.put("当事人", dangShiRen);
		parseResult.put("审理法庭", shenLiFaTing);
		parseResult.put("开庭日期", kaiTingRiQi);
		
		return parseResult;
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		System.out.println(parse("2017-08-24 08:10在姚安县人民法院第五审判庭开庭审理董兴安与云南九泰药业有限责任公司怀德仁连锁店大药房姚安分店、周粉仙合同纠纷"));
//		String c="在第三法庭(刑事专用)审理郭志明,张福荣,李建辉诈骗,抽逃出资,挪用资金,集资诈骗罪一案。";
//		System.out.println(c.substring(0,c.lastIndexOf("（")));
		
	}
}
