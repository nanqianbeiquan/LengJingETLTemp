package notice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;

import tools.ExecShell;
import tools.JobConfig;
import tools.MySQL;
import tools.ParseDate;
import tools.SysFunc;
import tools.TimeUtils;
import tools.WriteJobStatus;

public class CourtNoticeType3Job {
	String startDt=TimeUtils.getYesterday();
	String stopDt=TimeUtils.getToday();
	String tableId="27";
	Pattern pattern = Pattern.compile("申请再审人|原审\\(一审\\)诉讼地位|抗诉机关|诉讼|反诉|被申诉人|申诉人|再审|申请人|原审|被上诉人|上诉人|被告人|原告|被告|第三人|被|关于|等");
	public String getSelectCmd() throws IOException
	{
		File src=new File("conf/notice_type3.sql");
		FileInputStream reader=new FileInputStream(src);
		int l=(int) src.length();
		byte[] content=new byte[l];
		reader.read(content);
		reader.close();
		String selectCmd=new String(content);
		selectCmd=selectCmd.replace("@start_dt", "'"+startDt+"'");
		selectCmd=selectCmd.replace("@stop_dt", "'"+stopDt+"'");
		return selectCmd;
	}
	
	public void run(JobConfig jobConf) throws ParseException, IOException, ClassNotFoundException, SQLException
	{
		System.out.println("开始解析类型3");
		if(jobConf.hasProperty("startDt"))
		{
			startDt=jobConf.getString("startDt");
		}
		if(jobConf.hasProperty("stopDt"))
		{
			stopDt=jobConf.getString("stopDt");
		}
		String dt=TimeUtils.dateAdd(stopDt, -1);
		File dir=new File("datafile/"+TimeUtils.getYesterday());
		if(!dir.exists())
		{
			dir.mkdirs();
		}
		
		File f=new File(dir.getAbsolutePath()+"/notice_type3.txt");
//		File f=new File("data/notice_type_"+startDt+"_"+stopDt+".txt");
		FileWriter fw=new FileWriter(f);
		String path=f.getAbsolutePath();
		String selectCmd=getSelectCmd();
		Pattern pattern1=Pattern.compile("\\s");
		System.out.println(selectCmd);
		int jobStatus1=0;
		String remark1="";
		try
		{
			ResultSet result=MySQL.executeQuery(selectCmd);
			int cnt=0;
			while(result.next())
			{
				String anHao=pattern1.matcher(result.getString("an_hao").trim()).replaceAll("");
				String anJianMingCheng=pattern1.matcher(result.getString("an_jian_ming_cheng")).replaceAll("");				
				String kaiTingRiQi=result.getString("kai_ting_ri_qi");
				String shenLiFaTing=pattern1.matcher(result.getString("shen_li_fa_ting")).replaceAll("");
				String zhuShenFaGuan=pattern1.matcher(result.getString("shen_pan_zhang")).replaceAll("");
				String province=result.getString("province");
				String city=result.getString("city");
				String anYou="";
				String faYuanMingCheng="";
				String dangShiRen="";
				String chengBanTing="";
//				System.out.println(kaiTingRiQi);
				if(anHao.indexOf("案号：")>-1)
				{
					anHao=anHao.split("：")[1];
				}
				if(kaiTingRiQi.indexOf("时间")>-1)
				{
					Pattern pattern2=Pattern.compile("[\\d+]{4}(-)[\\d+]{1,2}(-)[\\d+]{1,2}");
					Matcher m3 = pattern2.matcher(kaiTingRiQi);
					if (m3.find()) {
						kaiTingRiQi=m3.group();        
			        }
				}
				kaiTingRiQi=ParseDate.parse(kaiTingRiQi);
				if(shenLiFaTing.indexOf("地点")>0)
				{
					shenLiFaTing=shenLiFaTing.substring(shenLiFaTing.indexOf("地点：")+"地点：".length(),shenLiFaTing.length());
				}
				
				HashMap<String,String> parseResult=McAyCourtNotice.parse(anJianMingCheng);
				if(parseResult==null)
				{
					continue;
				}
				anYou=parseResult.get("案由");
				dangShiRen=parseResult.get("当事人");
				dangShiRen=pattern.matcher(dangShiRen).replaceAll("").replace("-", "");
				dangShiRen=dangShiRen.replace("()", "").replace("；", ";").replace("：", ":").replace(";:", ";");
				if(dangShiRen.startsWith(":") || dangShiRen.startsWith(";"))
				{
					dangShiRen=dangShiRen.substring(1,dangShiRen.length());
				}
				String outLine1=kaiTingRiQi+"\001"+anYou+"\001"+anHao+"\001"
						+faYuanMingCheng+"\001"+shenLiFaTing+"\001"
						+zhuShenFaGuan+"\001"+chengBanTing+"\001"+dangShiRen+"\001"
						+province+"\001"+city;
				String outLine2=kaiTingRiQi+"\001"+anYou+"\001"+anHao+"\001"
						+faYuanMingCheng+"\001"+shenLiFaTing+"\001"
						+zhuShenFaGuan+"\001"+chengBanTing+"\001"+dangShiRen;				
				String md5=DigestUtils.md5Hex(outLine2);
				String[] companyArr=dangShiRen.split("[;,；:：，、与及诉]");
				for(String company:companyArr)
				{
					company=pattern.matcher(company).replaceAll("");
					if(!(company.contains("原告")
						|| company.contains("被告")
						|| company.contains("被上诉人")
						|| company.contains("上诉人")
						|| company.isEmpty()
						|| company.contains("null")
//						||company.contains("集团")
//						|| company.contains("企业")
//						|| company.contains("超市")
//						|| company.contains("有限合伙")
//						|| company.contains("公司")
//						|| (company.length()>5 && (
//								company.endsWith("厂")
//								|| company.endsWith("社")
//								|| company.endsWith("场")
//								|| company.endsWith("店")
//								|| company.endsWith("行")
//								|| company.endsWith("部")
//							))
						))
					{
						company=company.replace("(", "（").replace(")", "）");
						String key=company+"_"+tableId+"_"+md5;
						String outLine=key+"\001"+company+"\001"+outLine1+"\n";
						fw.write(outLine);
//						System.out.println(outLine);
					}					
				}
				if((++cnt)%1000==0)
				{
					System.out.println("++"+cnt);
				}
			}
			fw.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				remark1=SysFunc.getError(e);
				jobStatus1=1;
			}
			WriteJobStatus.writeJobStatus("开庭公告解析（类型3）", dt, jobStatus1, remark1.replace("'", ""));
//			String[] args=new String[]
//					{"hive"
//					,"-e"
//					,String.format("\"LOAD DATA LOCAL INPATH '%s' into TABLE ods.kai_ting_gong_gao partition(dt='%s')\"",path,dt)
//					};	
//			int jobStatus2=0;
//			String remark2="";
//			try {
//				remark2=ExecShell.exec(args);
//			} catch (Exception e) 
//			{
//				remark2=SysFunc.getError(e);
//				jobStatus2=1;
//			}
//			WriteJobStatus.writeJobStatus("开庭公告导入hive(类型3)", dt, jobStatus2, remark2.replace("'", ""));
//			String[] load4HbaseArgs=new String[]
//					{
//						"sh",
//						"/home/likai/ImportCourtNoticeToHbase.sh",
//						dt						
//					};
//			int jobStatus3=0;
//			String remark3="";
//			try {
//				remark3=ExecShell.exec(load4HbaseArgs);
//			} catch (Exception e) {
//				remark3=SysFunc.getError(e);
//				jobStatus3=1;
//			}
//			WriteJobStatus.writeJobStatus("开庭公告导入hbase(类型3)", dt, jobStatus3, remark3.replace("'", ""));
	}
	public static void main(String[] args) throws ClassNotFoundException, ParseException, IOException, SQLException
	{
		String[] newArgs={"--startDt=2017-08-22","--stopDt=2017-08-23"};
		JobConfig jobConf=new JobConfig(newArgs);
		CourtNoticeType3Job job=new CourtNoticeType3Job();
		job.run(jobConf);
		
	}	
}
