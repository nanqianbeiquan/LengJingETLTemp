package notice;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import tools.ExecShell;
import tools.JobConfig;
import tools.SysFunc;
import tools.TimeUtils;
import tools.WriteJobStatus;

public class CourtNotice {
	String startDt=TimeUtils.getYesterday();
	String stopDt=TimeUtils.getToday();
	String tableId="27";
	CourtNoticeType1Job type1=new CourtNoticeType1Job();
	CourtNoticeType2Job type2=new CourtNoticeType2Job();
	CourtNoticeType3Job type3=new CourtNoticeType3Job();
	CourtNoticeType4Job type4=new CourtNoticeType4Job();
	CourtNoticeType5Job type5=new CourtNoticeType5Job();
	CourtNoticeType6Job type6=new CourtNoticeType6Job();
	public void run(JobConfig jobConf) throws ClassNotFoundException, SQLException, IOException, ParseException, InterruptedException
	{
		if(jobConf.hasProperty("startDt"))
		{
			startDt=jobConf.getString("startDt");
		}
		if(jobConf.hasProperty("stopDt"))
		{
			stopDt=jobConf.getString("stopDt");
		}
		String dt=TimeUtils.dateAdd(stopDt, -1);
//		String dt=TimeUtils.dateAdd(stopDt, 0);
		File dir=new File("datafile/"+TimeUtils.getYesterday());
		File f=new File(dir.getAbsolutePath());
//		FileWriter fw=new FileWriter(f);
		String path=f.getAbsolutePath();
		type1.run(jobConf);
		type2.run(jobConf);
		type3.run(jobConf);
		type4.run(jobConf);
		type5.run(jobConf);	
		type6.run(jobConf);
		String[] args=new String[]
		{"hive"
		,"-e"
		,String.format("\"LOAD DATA LOCAL INPATH '%s' overwrite into TABLE ods.kai_ting_gong_gao_test partition(dt='%s')\"", path,dt)
		};
		int jobStatus2=0;
		String remark2="";
		try {
			remark2=ExecShell.exec(args);
		} catch (Exception e) {
			remark2=SysFunc.getError(e);
			jobStatus2=1;
		}
		WriteJobStatus.writeJobStatus("开庭公告导入hive", dt, jobStatus2, remark2.replace("'", ""));
//		String[] loadHbaseArgs=new String[]
//			{"sh"
//			,"/home/likai/ImportCourtNoticeToHbase.sh"
//			,dt	
//			};
//		int jobStatus3=0;
//		String remark3="";
//		try {
//			remark3=ExecShell.exec(loadHbaseArgs);
//		} catch (Exception e) {
//			remark3=SysFunc.getError(e);
//			jobStatus3=1;
//		}
//		WriteJobStatus.writeJobStatus("开庭公告导入hbase", dt, jobStatus3, remark3.replace("'", ""));
//		String[] loadHbaseTempArgs=new String[]
//			{"sh"
//			,"/home/likai/ImportCourtNoticeTempToHbase.sh"
//			,dt	
//			};
//		int jobStatus4=0;
//		String remark4="";
//		try {
//			remark4=ExecShell.exec(loadHbaseTempArgs);
//		} catch (Exception e) {
//			remark4=SysFunc.getError(e);
//			jobStatus4=1;
//		}
//		WriteJobStatus.writeJobStatus("开庭公告导入hbasetemp", dt, jobStatus4, remark4.replace("'", ""));
		String[] loadHbaseDsr2SiFaDocArgs=new String[]
				{"sh"
				,"/home/tianjingwang/GongGaoTest/ImportCourtNoticeDsr2SiFaDocTestToHbase.sh"
				,dt	
				};
			int jobStatus5=0;
			String remark5="";
			try {
				remark5=ExecShell.exec(loadHbaseDsr2SiFaDocArgs);
			} catch (Exception e) {
				remark5=SysFunc.getError(e);
				jobStatus5=1;
			}
		WriteJobStatus.writeJobStatus("开庭公告导入Dsr2SiFaDocTest", dt, jobStatus5, remark5.replace("'", ""));
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, ParseException, InterruptedException
	{
		String[] newArgs={"--startDt=2017-08-10"};
		JobConfig jobConf=new JobConfig(newArgs);
		CourtNotice job =new CourtNotice();
		job.run(jobConf);
	}

}
