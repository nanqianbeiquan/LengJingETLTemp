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
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;

import tools.ExecShell;
import tools.JobConfig;
import tools.MySQL;
import tools.ParseDate;
import tools.SysFunc;
import tools.TimeUtils;
import tools.WriteJobStatus;

public class CourtNoticeType5Job {
	String startDt=TimeUtils.getYesterday();
	String stopDt=TimeUtils.getToday();
	String tableId="27";
	Pattern pattern=Pattern.compile("申请再审人|原审\\(一审\\)诉讼地位|抗诉机关|诉讼|反诉|被申诉人|申诉人|再审|申请人|原审|被上诉人|上诉人|被告人|原告|被告|第三人|被|等");
	public String getSelectCmd() throws IOException
	{
		File src=new File("conf/notice_type5.sql");
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
		System.out.println("开始解析类型5");
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
		
		File f=new File(dir.getAbsolutePath()+"/notice_type5.txt");
//		File f=new File("data/courtnotice_"+startDt+"_"+stopDt+".txt");
		FileWriter fw=new FileWriter(f);
		String path=f.getAbsolutePath();
		String selectCmd=getSelectCmd();
		Pattern pattern1=Pattern.compile("\\s");
		System.out.println(selectCmd);
		int jobStatus1=0;
		String remark1="";
		try {
			ResultSet result=MySQL.executeQuery(selectCmd);
			int cnt=0;
			while(result.next())
			{
				String content=pattern1.matcher(result.getString("content")).replaceAll("");
				String province=result.getString("province");
				String city=result.getString("city");
//				System.out.println(content);
				String kaiTingRiQi="";
				String anHao="";
				String anYou="";
				String zhuShenFaGuan="";
				String shenLiFaTing="";
				String dangShiRen="";
				String chengBanTing="";
				String md5=null;
				content=content.replace("(", "（").replace(")", "）");
				String faYuanMingCheng="";
				
				HashMap<String,String> parseResult=XzCourtNotice.parse(content);
				if(parseResult==null)
				{
					continue;
				}
				anYou=parseResult.get("案由");
				shenLiFaTing=parseResult.get("审理法庭");
				kaiTingRiQi=parseResult.get("开庭日期");
				kaiTingRiQi=ParseDate.parse(kaiTingRiQi);
				zhuShenFaGuan=parseResult.get("主审法官");
				dangShiRen=parseResult.get("当事人");
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
				md5=DigestUtils.md5Hex(outLine2);
				
				String[] companyArr=dangShiRen.split("[;,；:：，、与及]");
				for(String company:companyArr)
				{
					company=pattern.matcher(company).replaceAll("");
					if(!(company.contains("原告")
						||company.contains("被告")
						||company.contains("被上诉人")
						||company.contains("上诉人")
						|| company.isEmpty()
						|| company.contains("null")
//						||company.contains("公司")
//						||company.contains("集团")
//						||company.contains("企业")
//						||company.contains("超市")
//						||company.contains("有限合伙")
//						||(company.length()>5&&(
//								company.endsWith("厂")
//								||company.endsWith("社")
//								||company.endsWith("场")
//								||company.endsWith("店")
//								||company.endsWith("部")
//								||company.endsWith("行")
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
			}
			fw.close();
		} catch (SQLException e) {
			e.printStackTrace();
			remark1=SysFunc.getError(e);
			jobStatus1=1;					
		}
		WriteJobStatus.writeJobStatus("开庭公告解析(类型5)", dt, jobStatus1, remark1.replace("'", ""));
//		String[] args=new String[]
//				{"hive"
//				,"-e"
//				,String.format("\"LOAD DATA LOCAL INPATH '%S' into TABLE ods.kai_ting_gong_gao partition(dt='%s') \"", path,dt)		
//				};
//		int jobStatus2=0;
//		String remark2="";
//		try {
//			remark2=ExecShell.exec(args);
//		} catch (Exception e) {
//			remark2=SysFunc.getError(e);
//			jobStatus2=1;			
//		}
//		WriteJobStatus.writeJobStatus("开庭公告导入hive(类型5)", dt, jobStatus2, remark2.replace("'", ""));
//		String[] load5HbaseArgs=new String[]
//				{"sh"
//				,"home/likai/ImputCourtNoticToHbase"
//				,dt		
//				};
//		int jobStatus3=0;
//		String remark3="";
//		try {
//			remark3=ExecShell.exec(load5HbaseArgs);
//		} catch (InterruptedException e) {
//			remark3=SysFunc.getError(e);
//			jobStatus3=1;
//		}
//		WriteJobStatus.writeJobStatus("开庭公告导入hbase(类型5)", dt, jobStatus3, remark3.replace("'", ""));
//		
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, ParseException, InterruptedException
	{
		String[] newArgs={"--startDt=2017-05-01"};
		JobConfig jobConf=new JobConfig(newArgs);
		CourtNoticeType5Job job =new CourtNoticeType5Job();
		job.run(jobConf);
	}
}
