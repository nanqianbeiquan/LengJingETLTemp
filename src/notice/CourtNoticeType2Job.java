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

public class CourtNoticeType2Job {
	
	String startDt=TimeUtils.getYesterday();
	String stopDt=TimeUtils.getToday();
	String tableId="27";
	Pattern pattern = Pattern.compile("申请再审人|原审\\(一审\\)诉讼地位|抗诉机关|诉讼|反诉|被申诉人|申诉人|再审|申请人|原审|被上诉人|上诉人|被告人|原告|被告|第三人|被|等");
	static Pattern pattern4 = Pattern.compile("[（(\\d+)）]{6}[^号]+号");
	static HashMap<String,String> courtCity=new HashMap<String,String>();
	static
	{
		courtCity.put("三门峡市湖滨区人民法院","三门峡市");
		courtCity.put("三门峡市陕州区人民法院","三门峡市");
		courtCity.put("上蔡县人民法院","驻马店市");
		courtCity.put("中牟县人民法院","郑州市");
		courtCity.put("临颍县人民法院","漯河市");
		courtCity.put("义马市人民法院","三门峡市");
		courtCity.put("伊川县人民法院","洛阳市");
		courtCity.put("信阳市平桥区人民法院","信阳市");
		courtCity.put("信阳市浉河区人民法院","信阳市");
		courtCity.put("修武县人民法院","焦作市");
		courtCity.put("偃师市人民法院","洛阳市");
		courtCity.put("光山县人民法院","信阳市");
		courtCity.put("兰考县人民法院","开封市");
		courtCity.put("内乡县人民法院","南阳市");
		courtCity.put("内黄县人民法院","安阳市");
		courtCity.put("南乐县人民法院","濮阳市");
		courtCity.put("南召县人民法院","南阳市");
		courtCity.put("南阳市卧龙区人民法院","南阳市");
		courtCity.put("南阳市宛城区人民法院","南阳市");
		courtCity.put("博爱县人民法院","焦作市");
		courtCity.put("卢氏县人民法院","三门峡市");
		courtCity.put("卫辉市人民法院","新乡市");
		courtCity.put("原阳县人民法院","新乡市");
		courtCity.put("台前县人民法院","濮阳市");
		courtCity.put("叶县人民法院","平顶山市");
		courtCity.put("周口市川汇区人民法院","周口市");
		courtCity.put("唐河县人民法院","南阳市");
		courtCity.put("商丘市梁园区人民法院","商丘市");
		courtCity.put("商丘市睢阳区人民法院","商丘市");
		courtCity.put("商城县人民法院","信阳市");
		courtCity.put("商水县人民法院","周口市");
		courtCity.put("固始县人民法院","信阳市");
		courtCity.put("夏邑县人民法院","商丘市");
		courtCity.put("太康县人民法院","周口市");
		courtCity.put("孟州市人民法院","焦作市");
		courtCity.put("孟津县人民法院","洛阳市");
		courtCity.put("宁陵县人民法院","商丘市");
		courtCity.put("安阳县人民法院","安阳市");
		courtCity.put("安阳市北关区人民法院","安阳市");
		courtCity.put("安阳市文峰区人民法院","安阳市");
		courtCity.put("安阳市殷都区人民法院","安阳市");
		courtCity.put("安阳市龙安区人民法院","安阳市");
		courtCity.put("宜阳县人民法院","洛阳市");
		courtCity.put("宝丰县人民法院","平顶山市");
		courtCity.put("封丘县人民法院","新乡市");
		courtCity.put("尉氏县人民法院","开封市");
		courtCity.put("嵩县人民法院","洛阳市");
		courtCity.put("巩义市人民法院","郑州市");
		courtCity.put("平舆县人民法院","驻马店市");
		courtCity.put("平顶山市卫东区人民法院","平顶山市");
		courtCity.put("平顶山市新华区人民法院","平顶山市");
		courtCity.put("平顶山市湛河区人民法院","平顶山市");
		courtCity.put("平顶山市石龙区人民法院","平顶山市");
		courtCity.put("延津县人民法院","新乡市");
		courtCity.put("开封市祥符区人民法院","开封市");
		courtCity.put("开封市禹王台区人民法院","开封市");
		courtCity.put("开封市金明区人民法院","开封市");
		courtCity.put("开封市顺河回族区人民法院","开封市");
		courtCity.put("开封市鼓楼区人民法院","开封市");
		courtCity.put("开封市龙亭区人民法院","开封市");
		courtCity.put("息县人民法院","信阳市");
		courtCity.put("扶沟县人民法院","周口市");
		courtCity.put("新乡县人民法院","新乡市");
		courtCity.put("新乡市凤泉区人民法院","新乡市");
		courtCity.put("新乡市卫滨区人民法院","新乡市");
		courtCity.put("新乡市牧野区人民法院","新乡市");
		courtCity.put("新乡市红旗区人民法院","新乡市");
		courtCity.put("新县人民法院","信阳市");
		courtCity.put("新安县人民法院","洛阳市");
		courtCity.put("新密市人民法院","郑州市");
		courtCity.put("新蔡县人民法院","驻马店市");
		courtCity.put("新郑市人民法院","郑州市");
		courtCity.put("新野县人民法院","南阳市");
		courtCity.put("方城县人民法院","南阳市");
		courtCity.put("杞县人民法院","开封市");
		courtCity.put("林州市人民法院","安阳市");
		courtCity.put("柘城县人民法院","商丘市");
		courtCity.put("栾川县人民法院","洛阳市");
		courtCity.put("桐柏县人民法院","南阳市");
		courtCity.put("正阳县人民法院","驻马店市");
		courtCity.put("武陟县人民法院","焦作市");
		courtCity.put("民权县人民法院","商丘市");
		courtCity.put("永城市人民法院","商丘市");
		courtCity.put("汝南县人民法院","驻马店市");
		courtCity.put("汝州市人民法院","平顶山市");
		courtCity.put("汝阳县人民法院","洛阳市");
		courtCity.put("汤阴县人民法院","安阳市");
		courtCity.put("沁阳市人民法院","焦作市");
		courtCity.put("沈丘县人民法院","周口市");
		courtCity.put("河南省三门峡市中级人民法院","三门峡市");
		courtCity.put("河南省信阳市中级人民法院","信阳市");
		courtCity.put("河南省南阳市中级人民法院","南阳市");
		courtCity.put("河南省周口市中级人民法院","周口市");
		courtCity.put("河南省商丘市中级人民法院","商丘市");
		courtCity.put("河南省安阳市中级人民法院","安阳市");
		courtCity.put("河南省平顶山市中级人民法院","平顶山市");
		courtCity.put("河南省开封市中级人民法院","开封市");
		courtCity.put("河南省新乡市中级人民法院","新乡市");
		courtCity.put("河南省洛阳市中级人民法院","洛阳市");
		courtCity.put("河南省济源中级人民法院","济源市");
		courtCity.put("河南省漯河市中级人民法院","漯河市");
		courtCity.put("河南省濮阳市中级人民法院","濮阳市");
		courtCity.put("河南省焦作市中级人民法院","焦作市");
		courtCity.put("河南省许昌市中级人民法院","许昌市");
		courtCity.put("河南省郑州市中级人民法院","郑州市");
		courtCity.put("河南省驻马店市中级人民法院","驻马店市");
		courtCity.put("河南省高级人民法院","郑州市");
		courtCity.put("河南省鹤壁市中级人民法院","鹤壁市");
		courtCity.put("三门峡市中级人民法院","三门峡市");
		courtCity.put("信阳市中级人民法院","信阳市");
		courtCity.put("南阳市中级人民法院","南阳市");
		courtCity.put("周口市中级人民法院","周口市");
		courtCity.put("商丘市中级人民法院","商丘市");
		courtCity.put("安阳市中级人民法院","安阳市");
		courtCity.put("平顶山市中级人民法院","平顶山市");
		courtCity.put("开封市中级人民法院","开封市");
		courtCity.put("新乡市中级人民法院","新乡市");
		courtCity.put("洛阳市中级人民法院","洛阳市");
		courtCity.put("济源中级人民法院","济源市");
		courtCity.put("漯河市中级人民法院","漯河市");
		courtCity.put("濮阳市中级人民法院","濮阳市");
		courtCity.put("焦作市中级人民法院","焦作市");
		courtCity.put("许昌市中级人民法院","许昌市");
		courtCity.put("郑州市中级人民法院","郑州市");
		courtCity.put("驻马店市中级人民法院","驻马店市");
		courtCity.put("鹤壁市中级人民法院","鹤壁市");
		courtCity.put("泌阳县人民法院","驻马店市");
		courtCity.put("洛宁县人民法院","洛阳市");
		courtCity.put("洛阳市吉利区人民法院","洛阳市");
		courtCity.put("洛阳市洛龙区人民法院","洛阳市");
		courtCity.put("洛阳市涧西区人民法院","洛阳市");
		courtCity.put("洛阳市瀍河回族区人民法院","洛阳市");
		courtCity.put("洛阳市老城区人民法院","洛阳市");
		courtCity.put("洛阳市西工区人民法院","洛阳市");
		courtCity.put("洛阳铁路运输法院","郑州市");
		courtCity.put("洛阳高新技术产业开发区人民法院","洛阳市");
		courtCity.put("济源市人民法院","济源市");
		courtCity.put("浚县人民法院","鹤壁市");
		courtCity.put("淅川县人民法院","南阳市");
		courtCity.put("淇县人民法院","鹤壁市");
		courtCity.put("淮滨县人民法院","信阳市");
		courtCity.put("淮阳县人民法院","周口市");
		courtCity.put("清丰县人民法院","濮阳市");
		courtCity.put("渑池县人民法院","三门峡市");
		courtCity.put("温县人民法院","焦作市");
		courtCity.put("滑县人民法院","安阳市");
		courtCity.put("漯河市召陵区人民法院","漯河市");
		courtCity.put("漯河市源汇区人民法院","漯河市");
		courtCity.put("漯河市郾城区人民法院","漯河市");
		courtCity.put("潢川县人民法院","信阳市");
		courtCity.put("濮阳县人民法院","濮阳市");
		courtCity.put("濮阳市华龙区人民法院","濮阳市");
		courtCity.put("灵宝市人民法院","三门峡市");
		courtCity.put("焦作市中站区人民法院","焦作市");
		courtCity.put("焦作市山阳区人民法院","焦作市");
		courtCity.put("焦作市解放区人民法院","焦作市");
		courtCity.put("焦作市马村区人民法院","焦作市");
		courtCity.put("登封市人民法院","郑州市");
		courtCity.put("睢县人民法院","商丘市");
		courtCity.put("确山县人民法院","驻马店市");
		courtCity.put("社旗县人民法院","南阳市");
		courtCity.put("禹州市人民法院","许昌市");
		courtCity.put("罗山县人民法院","信阳市");
		courtCity.put("舞钢市人民法院","平顶山市");
		courtCity.put("舞阳县人民法院","漯河市");
		courtCity.put("范县人民法院","濮阳市");
		courtCity.put("荥阳市人民法院","郑州市");
		courtCity.put("获嘉县人民法院","新乡市");
		courtCity.put("虞城县人民法院","商丘市");
		courtCity.put("襄城县人民法院","许昌市");
		courtCity.put("西华县人民法院","周口市");
		courtCity.put("西峡县人民法院","南阳市");
		courtCity.put("西平县人民法院","驻马店市");
		courtCity.put("许昌县人民法院","许昌市");
		courtCity.put("许昌市魏都区人民法院","许昌市");
		courtCity.put("辉县市人民法院","新乡市");
		courtCity.put("通许县人民法院","开封市");
		courtCity.put("遂平县人民法院","驻马店市");
		courtCity.put("邓州市人民法院","南阳市");
		courtCity.put("郏县人民法院","平顶山市");
		courtCity.put("郑州市上街区人民法院","郑州市");
		courtCity.put("郑州市中原区人民法院","郑州市");
		courtCity.put("郑州市二七区人民法院","郑州市");
		courtCity.put("郑州市惠济区人民法院","郑州市");
		courtCity.put("郑州市管城回族区人民法院","郑州市");
		courtCity.put("郑州市金水区人民法院","郑州市");
		courtCity.put("郑州铁路运输中级法院","郑州市");
		courtCity.put("郑州铁路运输法院","郑州市");
		courtCity.put("郑州高新技术产业开发区人民法院","郑州市");
		courtCity.put("郸城县人民法院","周口市");
		courtCity.put("鄢陵县人民法院","许昌市");
		courtCity.put("镇平县人民法院","南阳市");
		courtCity.put("长垣县人民法院","新乡市");
		courtCity.put("长葛市人民法院","许昌市");
		courtCity.put("陕县人民法院","三门峡市");
		courtCity.put("项城市人民法院","周口市");
		courtCity.put("驻马店市驿城区人民法院","驻马店市");
		courtCity.put("鲁山县人民法院","平顶山市");
		courtCity.put("鹤壁市山城区人民法院","鹤壁市");
		courtCity.put("鹤壁市淇滨区人民法院","鹤壁市");
		courtCity.put("鹤壁市鹤山区人民法院","鹤壁市");
		courtCity.put("鹿邑县人民法院","周口市");
		courtCity.put("郑州航空港经济综合实验区人民法院","郑州市");
		courtCity.put("安徽省高级人民法院","合肥市");
		courtCity.put("安徽省合肥市中级人民法院","合肥市");
		courtCity.put("合肥市瑶海区人民法院","合肥市");
		courtCity.put("合肥市庐阳区人民法院","合肥市");
		courtCity.put("合肥市蜀山区人民法院","合肥市");
		courtCity.put("合肥市包河区人民法院","合肥市");
		courtCity.put("长丰县人民法院","合肥市");
		courtCity.put("肥东县人民法院","合肥市");
		courtCity.put("肥西县人民法院","合肥市");
		courtCity.put("庐江县人民法院","合肥市");
		courtCity.put("巢湖市人民法院","合肥市");
		courtCity.put("合肥高新技术产业开发区人民法院","合肥市");
		courtCity.put("安徽省芜湖市中级人民法院","芜湖市");
		courtCity.put("芜湖市镜湖区人民法院","芜湖市");
		courtCity.put("芜湖市弋江区人民法院","芜湖市");
		courtCity.put("芜湖市鸠江区人民法院","芜湖市");
		courtCity.put("芜湖市三山区人民法院","芜湖市");
		courtCity.put("芜湖县人民法院","芜湖市");
		courtCity.put("繁昌县人民法院","芜湖市");
		courtCity.put("南陵县人民法院","芜湖市");
		courtCity.put("无为县人民法院","芜湖市");
		courtCity.put("芜湖经济技术开发区人民法院","芜湖市");
		courtCity.put("安徽省蚌埠市中级人民法院","蚌埠市");
		courtCity.put("蚌埠市龙子湖区人民法院","蚌埠市");
		courtCity.put("蚌埠市蚌山区人民法院","蚌埠市");
		courtCity.put("蚌埠市禹会区人民法院","蚌埠市");
		courtCity.put("蚌埠市淮上区人民法院","蚌埠市");
		courtCity.put("怀远县人民法院","蚌埠市");
		courtCity.put("五河县人民法院","蚌埠市");
		courtCity.put("固镇县人民法院","蚌埠市");
		courtCity.put("寿县人民法院","蚌埠市");
		courtCity.put("安徽省淮南市中级人民法院","淮南市");
		courtCity.put("淮南市大通区人民法院","淮南市");
		courtCity.put("淮南市田家庵区人民法院","淮南市");
		courtCity.put("淮南市谢家集区人民法院","淮南市");
		courtCity.put("淮南市八公山区人民法院","淮南市");
		courtCity.put("淮南市潘集区人民法院","淮南市");
		courtCity.put("凤台县人民法院","淮南市");
		courtCity.put("安徽省马鞍山市中级人民法院","马鞍山市");
		courtCity.put("马鞍山市花山区人民法院","马鞍山市");
		courtCity.put("马鞍山市雨山区人民法院","马鞍山市");
		courtCity.put("马鞍山市博望区人民法院","马鞍山市");
		courtCity.put("当涂县人民法院","马鞍山市");
		courtCity.put("含山县人民法院","马鞍山市");
		courtCity.put("和县人民法院","马鞍山市");
		courtCity.put("安徽省淮北市中级人民法院","淮北市");
		courtCity.put("淮北市杜集区人民法院","淮北市");
		courtCity.put("淮北市相山区人民法院","淮北市");
		courtCity.put("淮北市烈山区人民法院","淮北市");
		courtCity.put("濉溪县人民法院","淮北市");
		courtCity.put("安徽省铜陵市中级人民法院","铜陵市");
		courtCity.put("铜陵市铜官山区人民法院","铜陵市");
		courtCity.put("铜陵市狮子山区人民法院","铜陵市");
		courtCity.put("铜陵市郊区人民法院","铜陵市");
		courtCity.put("铜陵县人民法院","铜陵市");
		courtCity.put("安徽省安庆市中级人民法院","安庆市");
		courtCity.put("安庆市迎江区人民法院","安庆市");
		courtCity.put("安庆市大观区人民法院","安庆市");
		courtCity.put("安庆市宜秀区人民法院","安庆市");
		courtCity.put("怀宁县人民法院","安庆市");
		courtCity.put("枞阳县人民法院","安庆市");
		courtCity.put("潜山县人民法院","安庆市");
		courtCity.put("太湖县人民法院","安庆市");
		courtCity.put("宿松县人民法院","安庆市");
		courtCity.put("望江县人民法院","安庆市");
		courtCity.put("岳西县人民法院","安庆市");
		courtCity.put("桐城市人民法院","安庆市");
		courtCity.put("安徽省黄山市中级人民法院","黄山市");
		courtCity.put("黄山市屯溪区人民法院","黄山市");
		courtCity.put("黄山市黄山区人民法院","黄山市");
		courtCity.put("黄山市徽州区人民法院","黄山市");
		courtCity.put("歙县人民法院","黄山市");
		courtCity.put("休宁县人民法院","黄山市");
		courtCity.put("黟县人民法院","黄山市");
		courtCity.put("祁门县人民法院","黄山市");
		courtCity.put("安徽省滁州市中级人民法院","滁州市");
		courtCity.put("滁州市琅琊区人民法院","滁州市");
		courtCity.put("滁州市南谯区人民法院","滁州市");
		courtCity.put("来安县人民法院","滁州市");
		courtCity.put("全椒县人民法院","滁州市");
		courtCity.put("定远县人民法院","滁州市");
		courtCity.put("凤阳县人民法院","滁州市");
		courtCity.put("天长市人民法院","滁州市");
		courtCity.put("明光市人民法院","滁州市");
		courtCity.put("安徽省阜阳市中级人民法院","阜阳市");
		courtCity.put("阜阳市颍州区人民法院","阜阳市");
		courtCity.put("阜阳市颍东区人民法院","阜阳市");
		courtCity.put("阜阳市颍泉区人民法院","阜阳市");
		courtCity.put("临泉县人民法院","阜阳市");
		courtCity.put("太和县人民法院","阜阳市");
		courtCity.put("阜南县人民法院","阜阳市");
		courtCity.put("颍上县人民法院","阜阳市");
		courtCity.put("界首市人民法院","阜阳市");
		courtCity.put("安徽省宿州市中级人民法院","宿州市");
		courtCity.put("宿州市埇桥区人民法院","宿州市");
		courtCity.put("砀山县人民法院","宿州市");
		courtCity.put("萧县人民法院","宿州市");
		courtCity.put("灵璧县人民法院","宿州市");
		courtCity.put("泗县人民法院","宿州市");
		courtCity.put("安徽省六安市中级人民法院","六安市");
		courtCity.put("六安市金安区人民法院","六安市");
		courtCity.put("广德县人民法院","宣城市");
		courtCity.put("绩溪县人民法院","宣城市");
		courtCity.put("黄山市中级人民法院","黄山市");
		courtCity.put("宿州市中级人民法院","宿州市");
		courtCity.put("芜湖市中级人民法院","芜湖市");
		courtCity.put("郎溪县人民法院","宣城市");
		courtCity.put("池州市中级人民法院","池州市");
		courtCity.put("铜陵市中级人民法院","铜陵市");
		courtCity.put("六安市裕安区人民法院","六安市");
		courtCity.put("泾县人民法院","宣城市");
		courtCity.put("霍山县人民法院","六安市");
		courtCity.put("马鞍山市中级人民法院","马鞍山市");
		courtCity.put("六安市中级人民法院","六安市");
		courtCity.put("蚌埠市中级人民法院","蚌埠市");
		courtCity.put("蒙城县人民法院","亳州市");
		courtCity.put("合肥市中级人民法院","合肥市");
		courtCity.put("合肥市高新区人民法院","合肥市");
		courtCity.put("涡阳县人民法院","亳州市");
		courtCity.put("阜阳市中级人民法院","阜阳市");
		courtCity.put("舒城县人民法院","六安市");
		courtCity.put("安庆市中级人民法院","安庆市");
		courtCity.put("亳州市中级人民法院","亳州市");
		courtCity.put("宁国市人民法院","宁国市");
		courtCity.put("滁州市中级人民法院","滁州市");
		courtCity.put("霍邱县人民法院","六安市");
		courtCity.put("旌德县人民法院","宣城市");
		courtCity.put("亳州市谯城区人民法院","亳州市");
		courtCity.put("东至县人民法院","池州市");
		courtCity.put("铜陵市义安区人民法院","铜陵市");

	}
	public String getSelectCmd() throws IOException
	{
		File src=new File("conf/notice_type2.sql");
		FileInputStream reader = new FileInputStream(src);
		int l=(int) src.length();
		byte[] content=new byte[l];
		reader.read(content);
		reader.close();
		String selectCmd=new String(content);
		selectCmd=selectCmd.replace("@start_dt", "'"+startDt+"'");
		selectCmd=selectCmd.replace("@stop_dt", "'"+stopDt+"'");
//		System.out.println(selectCmd.length()-100);
//		System.out.println(selectCmd);
		return selectCmd;
	}
	
	public void run(JobConfig jobConf) throws ClassNotFoundException, SQLException, IOException, ParseException, InterruptedException
	{
		System.out.println("开始解析类型2");
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
		
		File f=new File(dir.getAbsolutePath()+"/notice_type2.txt");
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
				String area=result.getString("area");
				String kaiTingRiQi=result.getString("riqi");
				String anYou=pattern1.matcher(result.getString("an_you")).replaceAll("");
				String anHao=pattern1.matcher(result.getString("an_hao")).replaceAll("");
				String shenLiFaTing=pattern1.matcher(result.getString("fa_ting")).replaceAll("");
				String zhuShenFaGuan=pattern1.matcher(result.getString("shen_pan_zhang")).replaceAll("");
				String chengBanTing="";
				String dangShiRen="";
				String province=result.getString("province");;
				String city=result.getString("city");
				String md5=null;
//				System.out.println(content);
				content=content.replace("(", "（").replace(")", "）");
				HashMap<String,String> parseResult=BjCourtNotice.parse(content);
				if(parseResult==null)
				{
					continue;
				}
				if(!(area.equals("广州")))
				{
					anHao=anHao.replace("案号：", "");
					anYou=parseResult.get("案由");
					shenLiFaTing=parseResult.get("审理法庭");					
				
					kaiTingRiQi=parseResult.get("开庭日期");		
					Matcher m4=pattern4.matcher(content);
					if(area.equals("北京"))
					{
						if(m4.find())
						{
							anHao=m4.group();
						}	
					}

				}
				dangShiRen=parseResult.get("当事人");
				dangShiRen=pattern.matcher(dangShiRen).replaceAll(""); 
//				System.out.println(city);
				if (city.equals("城市"))
				{
					city=courtCity.get(faYuanMingCheng);
				}
				kaiTingRiQi=ParseDate.parse(kaiTingRiQi);
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
				String[] companyArr=dangShiRen.split("[;,；:：，、与及诉]");
//				System.out.println(dangShiRen);
				for(String company:companyArr)
				{
					company=pattern.matcher(company).replaceAll("");
					if(!(company.contains("原告")
							|| company.contains("被告")
							|| company.contains("被上诉人")
							|| company.contains("上诉人")
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

		WriteJobStatus.writeJobStatus("开庭公告解析（类型2）", dt, jobStatus1, remark1.replace("'", ""));
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
		String[] newArgs={"--startDt=2017-05-31","--stopDt=2017-06-01"};
		JobConfig jobConf=new JobConfig(newArgs);
		CourtNoticeType2Job job =new CourtNoticeType2Job();
//		job.getSelectCmd();
		job.run(jobConf);
	}
}
