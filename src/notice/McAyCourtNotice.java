package notice;

import java.util.HashMap;

import tools.AnYou;

public class McAyCourtNotice {
	public static HashMap<String,String>parse(String anJianMingCheng) 
	{
		HashMap<String,String> parseResult=new HashMap<String,String>();
		String anYou=AnYou.getAnYou(anJianMingCheng);
		String dangShiRen="";
		int dangShiRenStartIdx=-1;
		int dangShiRenStopIdx=-1;
		if(anYou !=null)
		{
			dangShiRenStopIdx=anJianMingCheng.indexOf(anYou);
			dangShiRen=anJianMingCheng.substring(0, dangShiRenStopIdx);
		}
		if(dangShiRenStopIdx==-1)
		{
			dangShiRen=anJianMingCheng;
			anYou="";
		}
		if(dangShiRen.endsWith("与"))
		{
			dangShiRen=dangShiRen.substring(0, dangShiRen.length()-1);
		}
		parseResult.put("案由", anYou);
		parseResult.put("当事人", dangShiRen);
		return parseResult;
	}
	
	public static void main(String[] args)
	{
		System.out.println(parse("2017-08-24 08:00在富宁县人民法院第二法庭开庭审理富宁桂柳工程机械配件部与李隆恩买卖合同纠纷"));
	}
}
