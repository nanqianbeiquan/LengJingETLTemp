package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class HbaseDataScan {
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	public static void scanTable(String tableName,String startRow,String stopRow) throws IOException
	{
		HTable table=TableFactory.getTable(tableName);
		Scan scan=new Scan();
//		scan.addFamily(Bytes.toBytes("kai_ting_gong_gao"));
		if (startRow!=null)	scan.setStartRow(Bytes.toBytes(startRow));
		if (stopRow!=null) scan.setStopRow(Bytes.toBytes(stopRow));
		ResultScanner scanRes = table.getScanner(scan);

		int i=0;
		for(Result res=scanRes.next();res!=null;res=scanRes.next())
		{
			i++;
			System.out.println("<----------------"+Bytes.toString(res.getRow())+"---------> "+i);
			for(Cell cell:res.rawCells())
			{
				String family=Bytes.toString(cell.getFamilyArray(),cell.getFamilyOffset(),cell.getFamilyLength());
				String qualifier=Bytes.toString(cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength());
				String value=Bytes.toString(cell.getValueArray(),cell.getValueOffset(),cell.getValueLength());
				System.out.println(sdf.format(new Date(cell.getTimestamp()))+">"+family+":"+qualifier+" -> "+value);
			}
		}
		scanRes.close();
	}
	
	public static void deleteRow(String tableName, String rowkey) throws IOException {
		HTable table=TableFactory.getTable(tableName);
		List list = new ArrayList(); 
		Delete d1 = new Delete(rowkey.getBytes()); 
		list.add(d1); 
		table.delete(list); 
//		System.out.println("删除行成功!"); 
	}
	
	
	public static void main(String[] args) throws IOException	
	{
		scanTable("GSTest","上海斯睿德信息技术有限公司_01","上海斯睿德信息技术有限公司_99");
//		scanTable("LengJingSF","绵阳顺腾物资有限公司_27_fea1e4347f5e130a6ce92eeabbf1b249","绵阳顺腾物资有限公司_27_fea1e4347f5e130a6ce92eeabbf1b249");
//		scanTable("LengJingSFTemp","绵阳顺腾物资有限公司_27_fea1e4347f5e130a6ce92eeabbf1b249","绵阳顺腾物资有限公司_27_fea1e4347f5e130a6ce92eeabbf1b249");
//		deleteRow("Dsr2SiFaDoc","江苏汇鸿股份有限公司_27_daa4b4fff89acec972cc389109303153");
//		File dir=new File("data/");
//		File f=new File(dir.getAbsolutePath()+"/rowkey");
//		System.out.println(f);
//		int cnt=0;
//		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));
//		for (String line = br.readLine(); line != null; line = br.readLine()) {
//			System.out.println(line);
//			deleteRow("Dsr2SiFaDoc",line);  
//			if((++cnt)%1000==0)
//			{
//				System.out.println("++"+cnt);
//			}
//				}
//				br.close();
////				System.out.println(f);
//				System.out.println("删除行成功!");
	}
}
