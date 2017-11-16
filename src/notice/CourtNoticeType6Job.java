package notice;

import java.io.File;
import java.io.FileInputStream;
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

public class CourtNoticeType6Job {
	
	String startDt=TimeUtils.getYesterday();
	String stopDt=TimeUtils.getToday();
	String tableId="27";
	static Pattern pattern2 = Pattern.compile("[O〇一二三四五六七八九(\\d+)]{4}-[一二三四五六七八九十(\\d+)]{1,2}-[一二三四五六七八九十(\\d+)]{1,3}");
	Pattern pattern = Pattern.compile("起诉人|申请再审人|原审\\(一审\\)诉讼地位|抗诉机关|诉讼|反诉|被申诉人|申诉人|再审|申请人|原审|被上诉人|上诉人|被告人|原告|被告|第三人|被|附带民事人|等|br");
	public String getSelectCmd() throws IOException
	{
		File src=new File("conf/notice_type6.sql");
		FileInputStream reader = new FileInputStream(src);
		int l=(int) src.length();
		byte[] content=new byte[l];
		reader.read(content);
		reader.close();
		String selectCmd=new String(content);
		selectCmd=selectCmd.replace("@start_dt", "'"+startDt+"'");
		selectCmd=selectCmd.replace("@stop_dt", "'"+stopDt+"'");
//		System.out.println(selectCmd.length()-100);
		System.out.println(selectCmd);
		return selectCmd;
	}
	
	public void run(JobConfig jobConf) throws ClassNotFoundException, SQLException, IOException, ParseException, InterruptedException
	{
		System.out.println("开始解析类型6");
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
		
		File f=new File(dir.getAbsolutePath()+"/notice_type6.txt");
//		File f=new File("data/notice_type2_"+startDt+"_"+stopDt+".txt");
		FileWriter fw=new FileWriter(f);
		String path = f.getAbsolutePath();
		String selectCmd=getSelectCmd();
		Pattern pattern1 = Pattern.compile("\\s");
		
		int jobStatus1=0;
		String remark1="";
		try
		{
			ResultSet result = MySQL.executeQuery(selectCmd);
			int cnt=0;
			while(result.next())
			{
				String faYuanMingCheng=pattern1.matcher(result.getString("fa_yuan_ming_cheng")).replaceAll(""); 
				String content=pattern1.matcher(result.getString("content")).replaceAll(""); 
				String riQi=pattern1.matcher(result.getString("riqi")).replaceAll("");
				String kaiTingRiQi="";
				String anYou="";
				String anHao="";
				String shenLiFaTing="";
				String zhuShenFaGuan="";
				String chengBanTing="";
				String dangShiRen="";
				String province=result.getString("province");
				String city=result.getString("city");;
				String md5=null;
				content=content.replace("(", "（").replace(")", "）");
				content=pattern.matcher(content).replaceAll("");
				content=content.replace("（）", "");
//				System.out.println("content："+content);

				HashMap<String,String> parseResult=SiChuanNotice.parse(content);
				if(parseResult==null)
				{
					continue;
				}
				anHao=parseResult.get("案号");
				anYou=parseResult.get("案由");
				shenLiFaTing=parseResult.get("审理法庭");
				dangShiRen=parseResult.get("当事人");

				if (city.equals("阿坝藏族羌族自治州"))
				{
					Matcher m1 = pattern2.matcher(riQi);

					if (m1.find()) {
						kaiTingRiQi=m1.group();   
//						System.out.println(kaiTingRiQi);
			        }
					
				}
				else
				{
					kaiTingRiQi=parseResult.get("开庭日期");
				}				
				kaiTingRiQi=ParseDate.parse(kaiTingRiQi);
//					if(dangShiRen.equals(""))
//					{
//						System.out.println(content);
//						System.out.println(parseResult);
//					}
					
				dangShiRen=dangShiRen.replace("因与","与").replace("以及","及").replace("；", ";").replace("：", ":").replace(";:", ";");
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
				
				md5=DigestUtils.md5Hex(outLine2);
				String[] companyArr=dangShiRen.split("[;,；:：，、诉与及]");
				for(String company:companyArr)
				{
					company=pattern.matcher(company).replaceAll("");
					if(!(company.contains("原告")
							|| company.contains("被告")
							|| company.contains("被上诉人")
							|| company.contains("上诉人")
							|| company.contains("你")
							|| company.isEmpty()
							|| company.contains("null")
//							|| company.contains("集团")
//							|| company.contains("企业")
//							|| company.contains("超市")
//							|| company.contains("有限合伙")
//							|| company.contains("公司")
//							|| (company.length()>5 && (
//									company.endsWith("厂")
//									|| company.endsWith("社")
//									|| company.endsWith("场")
//									|| company.endsWith("店")
//									|| company.endsWith("行")
//									|| company.endsWith("部")
					
//								))
							))
					{
						company=company.replace("(", "（").replace(")", "）");
						String key=company+"_"+tableId+"_"+md5;
						String outLine=key+"\001"+company+"\001"+outLine1+"\n";
//						System.out.println(outLine);
						fw.write(outLine);
					}
				}
				if((++cnt)%1000==0)
				{
					System.out.println("++"+cnt);
				}
//				System.out.println("++"+cnt);
			}
			fw.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			remark1=SysFunc.getError(e);
			jobStatus1=1;
		}

		WriteJobStatus.writeJobStatus("开庭公告解析（类型6）", dt, jobStatus1, remark1.replace("'", ""));
//		String[] args=new String[]
//				{"hive"
//				,"-e"
//				,String.format("\"LOAD DATA LOCAL INPATH '%s' into TABLE ods.kai_ting_gong_gao partition(dt='%s')\"",path,dt)
//				};
//		int jobStatus2=0;
//		String remark2="";
//		try
//		{
//			remark2=ExecShell.exec(args);
//		}
//		catch (Exception e)
//		{
//			remark2=SysFunc.getError(e);
//			jobStatus2=1;
//		}
//		WriteJobStatus.writeJobStatus("开庭公告导入hive（类型2）", dt, jobStatus2, remark2.replace("'", ""));
//		String[] load2HbaseArgs=new String[]
//				{"sh"
//				,"/home/likai/ImportCourtNoticeToHbase.sh"
//				,dt
//				};
//		int jobStatus3=0;
//		String remark3="";
//		try
//		{
//			remark3=ExecShell.exec(load2HbaseArgs);
//		}
//		catch (Exception e)
//		{
//			remark3=SysFunc.getError(e);
//			jobStatus3=1;
//		}
//		WriteJobStatus.writeJobStatus("开庭公告导入hbase）", dt, jobStatus3, remark3.replace("'", ""));
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, ParseException, InterruptedException
	{
		String[] newArgs={"--startDt=2017-07-01","--stopDt=2017-07-20"};
		JobConfig jobConf=new JobConfig(newArgs);
		CourtNoticeType6Job job =new CourtNoticeType6Job();
//		job.getSelectCmd();
		job.run(jobConf);
	}
}

