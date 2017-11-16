package etl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

import tools.TableFactory;

public class MergePersonNodesReducer  extends Reducer<Text,Text,NullWritable,Text>{
	
	HTable table;
	
	public void setup(Context context) throws IOException, InterruptedException
	{
		super.cleanup(context);
		table=TableFactory.getTable("PersonNodesMergeResult");
		table.setAutoFlushTo(false);
		table.setWriteBufferSize(6*1024*1024);
	}
	
	public void cleanup(Context context) throws IOException, InterruptedException
	{
		super.cleanup(context);
		table.flushCommits();
		table.close();
	}

	public void reduce(Text key,Iterable<Text> values,Context context) throws IOException, InterruptedException{
		
		String person=key.toString();
		ArrayList<HashSet<String>> mergeResultList=new ArrayList<HashSet<String>>();
        HashMap<String,Integer> mergeResultMap=new HashMap<String,Integer>();
		for(Text value:values)
		{
			String[] companys=value.toString().split("\001");
			String company1=companys[0];
        	String company2=companys[1];
        	int setIdx=-1;
        	if(mergeResultMap.containsKey(company1) && mergeResultMap.containsKey(company2))
        	{
        		setIdx=mergeResultMap.get(company1);
        		int setIdx2=mergeResultMap.get(company2);
        		if(setIdx!=setIdx2)
        		{
        			for(String company:mergeResultList.get(setIdx2))
            		{
            			mergeResultMap.put(company,setIdx);
            		}
            		mergeResultList.get(setIdx).addAll(mergeResultList.get(setIdx2));
            		mergeResultList.set(setIdx2, null);
        		}
        	}
        	else if(mergeResultMap.containsKey(company1) && !mergeResultMap.containsKey(company2))
        	{
        		setIdx=mergeResultMap.get(company1);
        		mergeResultList.get(setIdx).add(company2);
        		
        		mergeResultMap.put(company2, setIdx);
        	}
        	else if(mergeResultMap.containsKey(company2) && !mergeResultMap.containsKey(company1))
        	{
        		setIdx=mergeResultMap.get(company2);
        		mergeResultList.get(setIdx).add(company1);
        		
        		mergeResultMap.put(company1, setIdx);
        	}
        	else
        	{
        		setIdx=mergeResultList.size();
        		mergeResultList.add(new HashSet<String>());
        		mergeResultList.get(setIdx).add(company1);
        		mergeResultList.get(setIdx).add(company2);
        		mergeResultMap.put(company1, setIdx);
        		mergeResultMap.put(company2, setIdx);
        	}
		}
		int idx=0;
		Put put=new Put(Bytes.toBytes(person));
		Delete delete=new Delete(Bytes.toBytes(person));
		for(HashSet<String> companySet:mergeResultList)
        {
        	if(companySet!=null)
        	{
        		idx++;
        		for(String company:companySet)
        		{
        			context.write(NullWritable.get(), new Text(person+"\001"+company+"\001"+idx));
        			put.addColumn(Bytes.toBytes("Company"), Bytes.toBytes(company), Bytes.toBytes(String.valueOf(idx)));
        		}
        	}
        }
		table.delete(delete);
    	table.put(put);
	}
	
	public static void test() throws IOException
	{
		File src=new File("conf/周成建测试");
		InputStreamReader read = new InputStreamReader(new FileInputStream(src));
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineText = null;
        
        
        ArrayList<HashSet<String>> mergeResultList=new ArrayList<HashSet<String>>();
        HashMap<String,Integer> mergeResultMap=new HashMap<String,Integer>();
        while((lineText = bufferedReader.readLine()) != null)
        {
//        	System.out.println(lineText);
        	String[] companys=lineText.toString().split("\t");
        	String company1=companys[0];
        	String company2=companys[1];
        	int setIdx=-1;
        	if(mergeResultMap.containsKey(company1) && mergeResultMap.containsKey(company2))
        	{
        		setIdx=mergeResultMap.get(company1);
        		int setIdx2=mergeResultMap.get(company2);
        		if(setIdx!=setIdx2)
        		{
        			for(String company:mergeResultList.get(setIdx2))
            		{
            			mergeResultMap.put(company,setIdx);
            		}
            		mergeResultList.get(setIdx).addAll(mergeResultList.get(setIdx2));
            		mergeResultList.set(setIdx2, null);
        		}
        	}
        	else if(mergeResultMap.containsKey(company1) && !mergeResultMap.containsKey(company2))
        	{
        		setIdx=mergeResultMap.get(company1);
        		mergeResultList.get(setIdx).add(company2);
        		
        		mergeResultMap.put(company2, setIdx);
        	}
        	else if(mergeResultMap.containsKey(company2) && !mergeResultMap.containsKey(company1))
        	{
        		setIdx=mergeResultMap.get(company2);
        		mergeResultList.get(setIdx).add(company1);
        		
        		mergeResultMap.put(company1, setIdx);
        	}
        	else
        	{
        		setIdx=mergeResultList.size();
        		mergeResultList.add(new HashSet<String>());
        		mergeResultList.get(setIdx).add(company1);
        		mergeResultList.get(setIdx).add(company2);
        		mergeResultMap.put(company1, setIdx);
        		mergeResultMap.put(company2, setIdx);
        	}
//        	System.out.println(mergeResultList);
        }
        read.close();
        System.out.println(mergeResultMap.size());
//        System.out.println(mergeResultList);
        for(HashSet<String> s:mergeResultList)
        {
        	if(s!=null)
        	{
        		System.out.println(s);
        	}
        }
	}
	
	public static void main(String[] args) throws IOException
	{
		test();
	}
	
}
