package notice;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.AbstractDocument.Content;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.apache.commons.lang3.StringUtils;

import tools.AnYou;

public class SiChuanNotice {
	
//	申请再审人|原审(一审)诉讼地位|抗诉机关|诉讼|反诉|被申诉人|申诉人|再审|申请人|原审|被上诉人|上诉人|被告人|原告|被告|第三人|被
	static Pattern pattern1 = Pattern.compile("[(\\d+)]{4}[-年][-(\\d)+]{1,2}[-月][一二三四五六七八九十日\\d+]{2,3}");
	static Pattern pattern2 = Pattern.compile("[（(\\d+)）]{6}[^号]+号");
	static Pattern pattern3 = Pattern.compile("[行刑民][一二政]庭");

	public static HashMap<String, String> parse(String content)
	{
		if(content.equals("") || content.contains("现取消开庭"))
		{
			return null;
		}
		if(content.endsWith("（"))
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
		if(!content.endsWith("案。"))
		{
			content=content.replace("。", "案。");
		}

		HashMap<String,String> parseResult=new HashMap<String,String>();
		String anHao="";
		String kaiTingRiQi = "";
		String fayuanMingCheng="";
		String shenliFaTing="";
		String dangShiRen="";
		String anYou=AnYou.getAnYou(content);
		content=content.replace("本院受理的", "本院受理").replace("我院受理", "本院受理").replace("案由", "");

		Matcher m1 = pattern1.matcher(content);
		if (m1.find()) {
			kaiTingRiQi=m1.group();        
        }
		if (content.indexOf("公告")>0)
		{
			fayuanMingCheng=content.split("公告")[0];
		}
		Matcher m2=pattern2.matcher(content);
		if(m2.find())
		{
			anHao=m2.group();
		}
		Matcher m3=pattern3.matcher(content);
		if(m3.find())
		{
			shenliFaTing=m3.group();
		}
		int dangShiRenStartIdx=-1;
		int dangShiRenStopIdx=-1;
		if(dangShiRenStartIdx==-1)
		{
			dangShiRenStartIdx=content.indexOf("本院受理");
			if(dangShiRenStartIdx!=-1)
			{
				dangShiRenStartIdx=dangShiRenStartIdx+"本院受理".length();
			}
		}

		if(anYou!=null)
		{
			if(anHao.length()>0 && kaiTingRiQi.length()>0 && content.indexOf(kaiTingRiQi)-content.indexOf(anHao)>0)
			{
				dangShiRenStartIdx=content.indexOf(anHao)+anHao.length();
				dangShiRenStopIdx=content.indexOf(kaiTingRiQi);
			}
			else if(content.indexOf(shenliFaTing)>content.indexOf(anYou))
			{
				dangShiRenStartIdx=content.indexOf(anYou)+anYou.length();
				dangShiRenStopIdx=content.indexOf(shenliFaTing);
			}
			else if(anHao.length()>0 && kaiTingRiQi.length()>0 
					&& content.indexOf(kaiTingRiQi)-content.indexOf(anHao)<0 
					&& content.indexOf(shenliFaTing)<content.indexOf(anYou)
					&& content.indexOf("本院受理")==-1)
			{
				dangShiRenStartIdx=content.indexOf(anHao)+anHao.length();
				dangShiRenStopIdx=content.indexOf(anYou);
			}
			else if(anHao.length()>0 && kaiTingRiQi.length()>0 
					&& content.indexOf(kaiTingRiQi)-content.indexOf(anHao)<0 
					&& content.indexOf(shenliFaTing)<content.indexOf(anYou)
					&& content.indexOf("本院受理")>0)
			{
				dangShiRenStartIdx=content.indexOf("本院受理")+4;
				dangShiRenStopIdx=content.indexOf(anYou);
			}
			else
			{
				dangShiRenStopIdx=content.indexOf(anYou);
			}			
		}

		if(dangShiRenStartIdx!=-1 && dangShiRenStopIdx!=-1 && dangShiRenStopIdx>dangShiRenStartIdx)
		{
			dangShiRen=content.substring(dangShiRenStartIdx,dangShiRenStopIdx);
			dangShiRen=dangShiRen.replace(" ", "");
		}
		
		if(dangShiRen.endsWith("与"))
		{
			dangShiRen=dangShiRen.substring(0, dangShiRen.length()-1);
		}
		if (anHao.indexOf("（")>0)
		{
			anHao=anHao.substring(anHao.indexOf("（"),anHao.length());
		}
		Pattern pattern4 = Pattern.compile("【案号[（(\\d+)）]{6}[^号]+号】");
		dangShiRen=pattern4.matcher(dangShiRen).replaceAll("");
//		System.out.println("案号："+anHao);
//		System.out.println(kaiTingRiQi);
//		System.out.println(shenLiFaTing);
//		System.out.println(dangShiRen);
		parseResult.put("案号", anHao);
		parseResult.put("案由", anYou);
		parseResult.put("当事人", dangShiRen);
		parseResult.put("法院名称", fayuanMingCheng);
		parseResult.put("审理法庭", shenliFaTing);
		parseResult.put("开庭日期", kaiTingRiQi);
		
		return parseResult;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		System.out.println(parse("2017-08-24 08:00在富宁县人民法院第二法庭开庭审理富宁桂柳工程机械配件部与李隆恩买卖合同纠纷"));
		
	}

}

