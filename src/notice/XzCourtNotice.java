package notice;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tools.AnYou;

public class XzCourtNotice {
	static Pattern pattern1 = Pattern.compile("(\\d+){1,2}月[(\\d+)([\\s\\S])]{1,4}日[\\s\\S][(\\d+):]{3,}");
	static Pattern pattern2 = Pattern.compile("在[\\s\\S]*开庭");
	public static HashMap<String, String> parse(String content)
	{
		content=content.replace("：", ":");
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
		String zhuShenFaGuan="";
		String anYou=AnYou.getAnYou(content);
//		System.out.println(content);
		Matcher m1 = pattern1.matcher(content);
		if (m1.find()) {
			kaiTingRiQi=m1.group();        
        }
		Matcher m2 = pattern2.matcher(content);
		if (m2.find()) {
			shenLiFaTing=m2.group();
			shenLiFaTing=shenLiFaTing.replace("依法", "").replace("在","").replace("本院", "");
        } 
		if(content.indexOf("主审法官")>0)
		{
			zhuShenFaGuan=content.substring(content.indexOf("主审法官")+"主审法官".length(), content.length()).replace("。", "");
		}
//		System.out.println(kaiTingRiQi);
		int dangShiRenStartIdx=-1;
		int dangShiRenStopIdx=-1;
		if(dangShiRenStartIdx==-1)
		{
			dangShiRenStartIdx=content.indexOf(kaiTingRiQi);
			if(dangShiRenStartIdx!=-1)
			{
				dangShiRenStartIdx=dangShiRenStartIdx+kaiTingRiQi.length();
			}
		}
		if(anYou!=null)
		{
			dangShiRenStopIdx=content.indexOf(anYou);
			anYou=anYou.replace("一案", "");
		}
		if(dangShiRenStartIdx!=-1 && dangShiRenStopIdx!=-1)
		{
			dangShiRen=content.substring(dangShiRenStartIdx,dangShiRenStopIdx);
			dangShiRen=dangShiRen.replace(" ", "");
		}
		if(dangShiRen.endsWith("；")|dangShiRen.endsWith("与"))
		{
			dangShiRen=dangShiRen.substring(0, dangShiRen.length()-1);
		}	
		shenLiFaTing=shenLiFaTing.replace("公开开庭审理", "").replace("开庭审理", "").replace("开庭", "").replace("本院", "");
		parseResult.put("案由", anYou);
		parseResult.put("当事人", dangShiRen);
		parseResult.put("审理法庭", shenLiFaTing);
		parseResult.put("开庭日期", kaiTingRiQi);
		parseResult.put("主审法官", zhuShenFaGuan);
		return parseResult;
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		System.out.println(parse("2017-08-24 08:00在富宁县人民法院第二法庭开庭审理富宁桂柳工程机械配件部与李隆恩买卖合同纠纷"));
//		String c="在第一法庭审理朱玉山与朱文利,天津华泰财产保险有限公司天津分公司机动车交通事故责任纠纷一案。";
//		System.out.println(c.substring(0,c.lastIndexOf("（")));
		
	}
}
