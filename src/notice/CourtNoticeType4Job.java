package notice;

import java.io.File;
import java.io.FileInputStream;
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

public class CourtNoticeType4Job {
	String startDt=TimeUtils.getYesterday();
	String stopDt=TimeUtils.getToday();
	String tableId="27";

	Pattern pattern=Pattern.compile("抗诉的|被上诉人|上诉人|申请再审人|原审被告、第三人|二审|原审\\(一审\\)诉讼地位|抗诉机关|诉讼|反诉|被申诉人|申诉人|再审人|再审|申请人|原审|一审|被告人|原告|被告|第三人|附带民诉人|被|罪犯|等人|等");
	static HashMap<String,String> courtCity=new HashMap<String,String>();
	static
	{
		courtCity.put("唐山市中级人民法院","唐山市");
		courtCity.put("秦皇岛市中级人民法院","秦皇岛市");
		courtCity.put("邯郸市中级人民法院","邯郸市");
		courtCity.put("邢台市中级人民法院","邢台市");
		courtCity.put("保定市中级人民法院","保定市");
		courtCity.put("张家口市中级人民法院","张家口市");
		courtCity.put("承德市中级人民法院","承德市");
		courtCity.put("沧州市中级人民法院","沧州市");
		courtCity.put("石家庄铁路法院","石家庄市");	
		courtCity.put("廊坊市中级人民法院","廊坊市");
		courtCity.put("衡水市中级人民法院","衡水市");
		courtCity.put("石家庄铁路运输法院","石家庄市");
		courtCity.put("河北省高级人民法院","石家庄市");
		courtCity.put("河北省石家庄市中级人民法院","石家庄市");
		courtCity.put("石家庄市长安区人民法院","石家庄市");
		courtCity.put("石家庄市桥西区人民法院","石家庄市");
		courtCity.put("石家庄市新华区人民法院","石家庄市");
		courtCity.put("石家庄市井陉矿区人民法院","石家庄市");
		courtCity.put("石家庄市裕华区人民法院","石家庄市");
		courtCity.put("石家庄市藁城区人民法院","石家庄市");
		courtCity.put("石家庄市鹿泉区人民法院","石家庄市");
		courtCity.put("石家庄市栾城区人民法院","石家庄市");
		courtCity.put("石家庄市中级人民法院","石家庄市");
		courtCity.put("长安区人民法院","石家庄市");
		courtCity.put("桥西区人民法院","石家庄市");
		courtCity.put("新华区人民法院","石家庄市");
		courtCity.put("井陉矿区人民法院","石家庄市");
		courtCity.put("裕华区人民法院","石家庄市");
		courtCity.put("鹿泉区人民法院","石家庄市");
		courtCity.put("栾城区人民法院","石家庄市");
		courtCity.put("栾城区人民法院","石家庄市");
		courtCity.put("井陉县人民法院","石家庄市");
		courtCity.put("正定县人民法院","石家庄市");
		courtCity.put("行唐县人民法院","石家庄市");
		courtCity.put("灵寿县人民法院","石家庄市");
		courtCity.put("高邑县人民法院","石家庄市");
		courtCity.put("深泽县人民法院","石家庄市");
		courtCity.put("赞皇县人民法院","石家庄市");
		courtCity.put("无极县人民法院","石家庄市");
		courtCity.put("平山县人民法院","石家庄市");
		courtCity.put("元氏县人民法院","石家庄市");
		courtCity.put("赵县人民法院","石家庄市");
		courtCity.put("辛集市人民法院","石家庄市");
		courtCity.put("晋州市人民法院","石家庄市");
		courtCity.put("新乐市人民法院","石家庄市");
		courtCity.put("石家庄高新技术产业开发区人民法院","石家庄市");
		courtCity.put("河北省唐山市中级人民法院","唐山市");
		courtCity.put("唐山市路南区人民法院","唐山市");
		courtCity.put("唐山市路北区人民法院","唐山市");
		courtCity.put("唐山市古冶区人民法院","唐山市");
		courtCity.put("唐山市开平区人民法院","唐山市");
		courtCity.put("唐山市丰南区人民法院","唐山市");
		courtCity.put("唐山市丰润区人民法院","唐山市");
		courtCity.put("唐山市曹妃甸区人民法院","唐山市");
		courtCity.put("滦县人民法院","唐山市");
		courtCity.put("滦南县人民法院","唐山市");
		courtCity.put("乐亭县人民法院","唐山市");
		courtCity.put("迁西县人民法院","唐山市");
		courtCity.put("玉田县人民法院","唐山市");
		courtCity.put("遵化市人民法院","唐山市");
		courtCity.put("迁安市人民法院","唐山市");
		courtCity.put("唐山高新技术产业开发区人民法院","唐山市");
		courtCity.put("河北省秦皇岛市中级人民法院","秦皇岛市");
		courtCity.put("秦皇岛市海港区人民法院","秦皇岛市");
		courtCity.put("秦皇岛市山海关区人民法院","秦皇岛市");
		courtCity.put("秦皇岛市北戴河区人民法院","秦皇岛市");
		courtCity.put("青龙满族自治县人民法院","秦皇岛市");
		courtCity.put("昌黎县人民法院","秦皇岛市");
		courtCity.put("抚宁县人民法院","秦皇岛市");
		courtCity.put("卢龙县人民法院","秦皇岛市");
		courtCity.put("秦皇岛经济技术开发区人民法院","秦皇岛市");
		courtCity.put("秦皇岛北戴河新区人民法院","秦皇岛市");
		courtCity.put("河北省邯郸市中级人民法院","邯郸市");
		courtCity.put("邯郸市邯山区人民法院","邯郸市");
		courtCity.put("邯郸市丛台区人民法院","邯郸市");
		courtCity.put("邯郸市复兴区人民法院","邯郸市");
		courtCity.put("邯郸市峰峰矿区人民法院","邯郸市");
		courtCity.put("邯郸县人民法院","邯郸市");
		courtCity.put("临漳县人民法院","邯郸市");
		courtCity.put("成安县人民法院","邯郸市");
		courtCity.put("大名县人民法院","邯郸市");
		courtCity.put("涉县人民法院","邯郸市");
		courtCity.put("磁县人民法院","邯郸市");
		courtCity.put("肥乡县人民法院","邯郸市");
		courtCity.put("永年县人民法院","邯郸市");
		courtCity.put("邱县人民法院","邯郸市");
		courtCity.put("鸡泽县人民法院","邯郸市");
		courtCity.put("广平县人民法院","邯郸市");
		courtCity.put("馆陶县人民法院","邯郸市");
		courtCity.put("魏县人民法院","邯郸市");
		courtCity.put("曲周县人民法院","邯郸市");
		courtCity.put("武安市人民法院","邯郸市");
		courtCity.put("河北省邢台市中级人民法院","邢台市");
		courtCity.put("邢台市桥东区人民法院","邢台市");
		courtCity.put("邢台市桥西区人民法院","邢台市");
		courtCity.put("邢台县人民法院","邢台市");
		courtCity.put("临城县人民法院","邢台市");
		courtCity.put("内丘县人民法院","邢台市");
		courtCity.put("柏乡县人民法院","邢台市");
		courtCity.put("隆尧县人民法院","邢台市");
		courtCity.put("任县人民法院","邢台市");
		courtCity.put("南和县人民法院","邢台市");
		courtCity.put("宁晋县人民法院","邢台市");
		courtCity.put("巨鹿县人民法院","邢台市");
		courtCity.put("新河县人民法院","邢台市");
		courtCity.put("广宗县人民法院","邢台市");
		courtCity.put("平乡县人民法院","邢台市");
		courtCity.put("威县人民法院","邢台市");
		courtCity.put("清河县人民法院","邢台市");
		courtCity.put("临西县人民法院","邢台市");
		courtCity.put("南宫市人民法院","邢台市");
		courtCity.put("沙河市人民法院","邢台市");
		courtCity.put("邢台经济开发区人民法院","邢台市");
		courtCity.put("河北省保定市中级人民法院","保定市");
		courtCity.put("保定市新市区人民法院","保定市");
		courtCity.put("保定市北市区人民法院","保定市");
		courtCity.put("保定市南市区人民法院","保定市");
		courtCity.put("满城县人民法院","保定市");
		courtCity.put("围场县人民法院","邢台市");
		courtCity.put("保定高新技术产业开发区人民法院","保定市");
		courtCity.put("清苑县人民法院","保定市");
		courtCity.put("涞水县人民法院","保定市");
		courtCity.put("阜平县人民法院","保定市");
		courtCity.put("徐水县人民法院","保定市");
		courtCity.put("定兴县人民法院","保定市");
		courtCity.put("唐县人民法院","保定市");
		courtCity.put("高阳县人民法院","保定市");
		courtCity.put("容城县人民法院","保定市");
		courtCity.put("涞源县人民法院","保定市");
		courtCity.put("望都县人民法院","保定市");
		courtCity.put("安新县人民法院","保定市");
		courtCity.put("易县人民法院","保定市");
		courtCity.put("曲阳县人民法院","保定市");
		courtCity.put("蠡县人民法院","保定市");
		courtCity.put("顺平县人民法院","保定市");
		courtCity.put("博野县人民法院","保定市");
		courtCity.put("雄县人民法院","保定市");
		courtCity.put("涿州市人民法院","保定市");
		courtCity.put("定州市人民法院","保定市");
		courtCity.put("安国市人民法院","保定市");
		courtCity.put("高碑店市人民法院","保定市");
		courtCity.put("河北省张家口市中级人民法院","张家口市");
		courtCity.put("张家口市桥东区人民法院","张家口市");
		courtCity.put("张家口市桥西区人民法院","张家口市");
		courtCity.put("张家口市宣化区人民法院","张家口市");
		courtCity.put("张家口市下花园区人民法院","张家口市");
		courtCity.put("宣化县人民法院","张家口市");
		courtCity.put("张北县人民法院","张家口市");
		courtCity.put("康保县人民法院","张家口市");
		courtCity.put("沽源县人民法院","张家口市");
		courtCity.put("尚义县人民法院","张家口市");
		courtCity.put("蔚县人民法院","张家口市");
		courtCity.put("阳原县人民法院","张家口市");
		courtCity.put("怀安县人民法院","张家口市");
		courtCity.put("万全县人民法院","张家口市");
		courtCity.put("怀来县人民法院","张家口市");
		courtCity.put("涿鹿县人民法院","张家口市");
		courtCity.put("赤城县人民法院","张家口市");
		courtCity.put("崇礼县人民法院","张家口市");
		courtCity.put("张家口经济开发区人民法院","张家口市");
		courtCity.put("河北省承德市中级人民法院","承德市");
		courtCity.put("承德市双桥区人民法院","承德市");
		courtCity.put("承德市双滦区人民法院","承德市");
		courtCity.put("承德市鹰手营子矿区人民法院","承德市");
		courtCity.put("承德县人民法院","承德市");
		courtCity.put("兴隆县人民法院","承德市");
		courtCity.put("平泉县人民法院","承德市");
		courtCity.put("滦平县人民法院","承德市");
		courtCity.put("隆化县人民法院","承德市");
		courtCity.put("丰宁满族自治县人民法院","承德市");
		courtCity.put("宽城满族自治县人民法院","承德市");
		courtCity.put("围场满族蒙古族自治县人民法院","承德市");
		courtCity.put("河北省沧州市中级人民法院","沧州市");
		courtCity.put("沧州市新华区人民法院","沧州市");
		courtCity.put("沧州市运河区人民法院","沧州市");
		courtCity.put("沧县人民法院","沧州市");
		courtCity.put("青县人民法院","沧州市");
		courtCity.put("东光县人民法院","沧州市");
		courtCity.put("海兴县人民法院","沧州市");
		courtCity.put("盐山县人民法院","沧州市");
		courtCity.put("肃宁县人民法院","沧州市");
		courtCity.put("南皮县人民法院","沧州市");
		courtCity.put("吴桥县人民法院","沧州市");
		courtCity.put("献县人民法院","沧州市");
		courtCity.put("孟村回族自治县人民法院","沧州市");
		courtCity.put("泊头市人民法院","沧州市");
		courtCity.put("任丘市人民法院","沧州市");
		courtCity.put("黄骅市人民法院","沧州市");
		courtCity.put("河间市人民法院","沧州市");
		courtCity.put("河北省廊坊市中级人民法院","廊坊市");
		courtCity.put("廊坊市安次区人民法院","廊坊市");
		courtCity.put("廊坊市广阳区人民法院","廊坊市");
		courtCity.put("固安县人民法院","廊坊市");
		courtCity.put("永清县人民法院","廊坊市");
		courtCity.put("香河县人民法院","廊坊市");
		courtCity.put("大城县人民法院","廊坊市");
		courtCity.put("文安县人民法院","廊坊市");
		courtCity.put("大厂回族自治县人民法院","廊坊市");
		courtCity.put("霸州市人民法院","廊坊市");
		courtCity.put("三河市人民法院","廊坊市");
		courtCity.put("廊坊市经济技术开发区人民法院","廊坊市");
		courtCity.put("河北省衡水市中级人民法院","衡水市");
		courtCity.put("衡水市桃城区人民法院","衡水市");
		courtCity.put("枣强县人民法院","衡水市");
		courtCity.put("武邑县人民法院","衡水市");
		courtCity.put("武强县人民法院","衡水市");
		courtCity.put("饶阳县人民法院","衡水市");
		courtCity.put("安平县人民法院","衡水市");
		courtCity.put("故城县人民法院","衡水市");
		courtCity.put("景县人民法院","衡水市");
		courtCity.put("阜城县人民法院","衡水市");
		courtCity.put("冀州市人民法院","衡水市");
		courtCity.put("深州市人民法院","衡水市");
		courtCity.put("石家庄铁路运输法院","石家庄市");
		courtCity.put("抚宁区人民法院","秦皇岛市");
		courtCity.put("张家口市崇礼区人民法院","张家口市");
		courtCity.put("藁城区人民法院","石家庄市");
		courtCity.put("藁城市人民法院","石家庄市");
		courtCity.put("栾城县人民法院","石家庄市");
		courtCity.put("石家庄市桥东区人民法院","石家庄市");
		courtCity.put("黑龙江省高级人民法院","哈尔滨市");
		courtCity.put("黑龙江省哈尔滨市中级人民法院","哈尔滨市");
		courtCity.put("哈尔滨市中级人民法院","哈尔滨市");
		courtCity.put("哈尔滨市道里区人民法院","哈尔滨市");
		courtCity.put("哈尔滨市南岗区人民法院","哈尔滨市");
		courtCity.put("哈尔滨市道外区人民法院","哈尔滨市");
		courtCity.put("哈尔滨市平房区人民法院","哈尔滨市");
		courtCity.put("哈尔滨市松北区人民法院","哈尔滨市");
		courtCity.put("哈尔滨市香坊区人民法院","哈尔滨市");
		courtCity.put("哈尔滨市呼兰区人民法院","哈尔滨市");
		courtCity.put("哈尔滨市阿城区人民法院","哈尔滨市");
		courtCity.put("哈尔滨市双城区人民法院","哈尔滨市");
		courtCity.put("吉林市昌邑区人民法院","吉林市");
		courtCity.put("湖南省高级人民法院","长沙市");
		courtCity.put("蛟河市人民法院","吉林市");
		courtCity.put("靖宇县人民法院","白山市");
		courtCity.put("鹿泉市人民法院","石家庄市 ");
		courtCity.put("吉林市龙潭区人民法院","吉林市");		
		courtCity.put("依兰县人民法院","哈尔滨市");
		courtCity.put("方正县人民法院","哈尔滨市");
		courtCity.put("宾县人民法院","哈尔滨市");
		courtCity.put("巴彦县人民法院","哈尔滨市");
		courtCity.put("木兰县人民法院","哈尔滨市");
		courtCity.put("通河县人民法院","哈尔滨市");
		courtCity.put("延寿县人民法院","哈尔滨市");
		courtCity.put("尚志市人民法院","哈尔滨市");
		courtCity.put("五常市人民法院","哈尔滨市");
		courtCity.put("黑龙江省齐齐哈尔市中级人民法院","齐齐哈尔市");
		courtCity.put("齐齐哈尔市中级人民法院","齐齐哈尔市");
		courtCity.put("齐齐哈尔市龙沙区人民法院","齐齐哈尔市");
		courtCity.put("齐齐哈尔市建华区人民法院","齐齐哈尔市");
		courtCity.put("齐齐哈尔市铁锋区人民法院","齐齐哈尔市");
		courtCity.put("齐齐哈尔市昂昂溪区人民法院","齐齐哈尔市");
		courtCity.put("齐齐哈尔市富拉尔基区人民法院","齐齐哈尔市");
		courtCity.put("齐齐哈尔市碾子山区人民法院","齐齐哈尔市");
		courtCity.put("齐齐哈尔市梅里斯达斡尔族区人民法院","齐齐哈尔市");
		courtCity.put("龙江县人民法院","齐齐哈尔市");
		courtCity.put("依安县人民法院","齐齐哈尔市");
		courtCity.put("泰来县人民法院","齐齐哈尔市");
		courtCity.put("甘南县人民法院","齐齐哈尔市");
		courtCity.put("富裕县人民法院","齐齐哈尔市");
		courtCity.put("克山县人民法院","齐齐哈尔市");
		courtCity.put("克东县人民法院","齐齐哈尔市");
		courtCity.put("拜泉县人民法院","齐齐哈尔市");
		courtCity.put("讷河市人民法院","齐齐哈尔市");
		courtCity.put("黑龙江省鸡西市中级人民法院","鸡西市");
		courtCity.put("鸡西市中级人民法院","鸡西市");
		courtCity.put("鸡西市鸡冠区人民法院","鸡西市");
		courtCity.put("鸡西市恒山区人民法院","鸡西市");
		courtCity.put("鸡西市滴道区人民法院","鸡西市");
		courtCity.put("鸡西市梨树区人民法院","鸡西市");
		courtCity.put("鸡西市城子河区人民法院","鸡西市");
		courtCity.put("鸡西市麻山区人民法院","鸡西市");
		courtCity.put("鸡东县人民法院","鸡西市");
		courtCity.put("虎林市人民法院","鸡西市");
		courtCity.put("密山市人民法院","鸡西市");
		courtCity.put("黑龙江省鹤岗市中级人民法院","鹤岗市");
		courtCity.put("鹤岗市中级人民法院","鹤岗市");
		courtCity.put("鹤岗市向阳区人民法院","鹤岗市");
		courtCity.put("鹤岗市工农区人民法院","鹤岗市");
		courtCity.put("鹤岗市南山区人民法院","鹤岗市");
		courtCity.put("鹤岗市兴安区人民法院","鹤岗市");
		courtCity.put("鹤岗市东山区人民法院","鹤岗市");
		courtCity.put("鹤岗市兴山区人民法院","鹤岗市");
		courtCity.put("鹤岗市萝北县人民法院","鹤岗市");
		courtCity.put("鹤岗市绥滨县人民法院","鹤岗市");
		courtCity.put("黑龙江省双鸭山市中级人民法院","双鸭山市");
		courtCity.put("双鸭山市中级人民法院","双鸭山市");
		courtCity.put("双鸭山市尖山区人民法院","双鸭山市");
		courtCity.put("双鸭山市岭东区人民法院","双鸭山市");
		courtCity.put("双鸭山市四方台区人民法院","双鸭山市");
		courtCity.put("双鸭山市宝山区人民法院","双鸭山市");
		courtCity.put("集贤县人民法院","双鸭山市");
		courtCity.put("友谊县人民法院","双鸭山市");
		courtCity.put("宝清县人民法院","双鸭山市");
		courtCity.put("饶河县人民法院","双鸭山市");
		courtCity.put("黑龙江省大庆市中级人民法院","大庆市");
		courtCity.put("大庆市萨尔图区人民法院","大庆市");
		courtCity.put("大庆市龙凤区人民法院","大庆市");
		courtCity.put("大庆市让胡路区人民法院","大庆市");
		courtCity.put("大庆市红岗区人民法院","大庆市");
		courtCity.put("大庆市大同区人民法院","大庆市");
		courtCity.put("肇州县人民法院","大庆市");
		courtCity.put("肇源县人民法院","大庆市");
		courtCity.put("林甸县人民法院","大庆市");
		courtCity.put("杜尔伯特蒙古族自治县人民法院","大庆市");
		courtCity.put("大庆高新技术产业开发区人民法院","大庆市");
		courtCity.put("黑龙江省伊春市中级人民法院","伊春市");
		courtCity.put("伊春市中级人民法院","伊春市");
		courtCity.put("伊春市伊春区人民法院","伊春市");
		courtCity.put("伊春市南岔区人民法院","伊春市");
		courtCity.put("伊春市友好区人民法院","伊春市");
		courtCity.put("伊春市西林区人民法院","伊春市");
		courtCity.put("伊春市翠峦区人民法院","伊春市");
		courtCity.put("伊春市新青区人民法院","伊春市");
		courtCity.put("伊春市美溪区人民法院","伊春市");
		courtCity.put("伊春市金山屯区人民法院","伊春市");
		courtCity.put("伊春市五营区人民法院","伊春市");
		courtCity.put("伊春市乌马河区人民法院","伊春市");
		courtCity.put("伊春市汤旺河区人民法院","伊春市");
		courtCity.put("伊春市带岭区人民法院","伊春市");
		courtCity.put("伊春市乌伊岭区人民法院","伊春市");
		courtCity.put("伊春市红星区人民法院","伊春市");
		courtCity.put("伊春市上甘岭区人民法院","伊春市");
		courtCity.put("嘉荫县人民法院","伊春市");
		courtCity.put("铁力市人民法院","伊春市");
		courtCity.put("朗乡林区基层法院","伊春市");
		courtCity.put("双丰林区基层法院","伊春市");
		courtCity.put("铁力林区基层法院","伊春市");
		courtCity.put("桃山林区基层法院","伊春市");
		courtCity.put("黑龙江省佳木斯市中级人民法院","佳木斯市");
		courtCity.put("佳木斯市中级人民法院","佳木斯市");
		courtCity.put("佳木斯市向阳区人民法院","佳木斯市");
		courtCity.put("佳木斯市前进区人民法院","佳木斯市");
		courtCity.put("佳木斯市东风区人民法院","佳木斯市");
		courtCity.put("佳木斯市郊区人民法院","佳木斯市");
		courtCity.put("桦南县人民法院","佳木斯市");
		courtCity.put("桦川县人民法院","佳木斯市");
		courtCity.put("汤原县人民法院","佳木斯市");
		courtCity.put("抚远县人民法院","佳木斯市");
		courtCity.put("同江市人民法院","佳木斯市");
		courtCity.put("富锦市人民法院","佳木斯市");
		courtCity.put("黑龙江省七台河市中级人民法院","七台河市");
		courtCity.put("七台河市中级人民法院","七台河市");
		courtCity.put("七台河市新兴区人民法院","七台河市");
		courtCity.put("七台河市桃山区人民法院","七台河市");
		courtCity.put("七台河市茄子河区人民法院","七台河市");
		courtCity.put("勃利县人民法院","七台河市");
		courtCity.put("黑龙江省牡丹江市中级人民法院","牡丹江市");
		courtCity.put("牡丹江市中级人民法院","牡丹江市");
		courtCity.put("牡丹江市东安区人民法院","牡丹江市");
		courtCity.put("牡丹江市阳明区人民法院","牡丹江市");
		courtCity.put("牡丹江市爱民区人民法院","牡丹江市");
		courtCity.put("牡丹江市西安区人民法院","牡丹江市");
		courtCity.put("东宁县人民法院","牡丹江市");
		courtCity.put("林口县人民法院","牡丹江市");
		courtCity.put("绥芬河市人民法院","牡丹江市");
		courtCity.put("海林市人民法院","牡丹江市");
		courtCity.put("宁安市人民法院","牡丹江市");
		courtCity.put("穆棱市人民法院","牡丹江市");
		courtCity.put("黑龙江省黑河市中级人民法院","黑河市");
		courtCity.put("黑河市中级人民法院","黑河市");
		courtCity.put("黑河市爱辉区人民法院","黑河市");
		courtCity.put("嫩江县人民法院","黑河市");
		courtCity.put("逊克县人民法院","黑河市");
		courtCity.put("孙吴县人民法院","黑河市");
		courtCity.put("北安市人民法院","黑河市");
		courtCity.put("五大连池市人民法院","黑河市");
		courtCity.put("黑龙江省绥化市中级人民法院","绥化市");
		courtCity.put("绥化市中级人民法院","绥化市");
		courtCity.put("绥化市北林区人民法院","绥化市");
		courtCity.put("望奎县人民法院","绥化市");
		courtCity.put("兰西县人民法院","绥化市");
		courtCity.put("青冈县人民法院","绥化市");
		courtCity.put("庆安县人民法院","绥化市");
		courtCity.put("明水县人民法院","绥化市");
		courtCity.put("绥棱县人民法院","绥化市");
		courtCity.put("安达市人民法院","绥化市");
		courtCity.put("肇东市人民法院","绥化市");
		courtCity.put("海伦市人民法院","绥化市");
		courtCity.put("黑龙江省大兴安岭地区中级人民法院","大兴安岭地区");
		courtCity.put("大兴安岭地区加格达奇区人民法院","大兴安岭地区");
		courtCity.put("大兴安岭地区松岭区人民法院","大兴安岭地区");
		courtCity.put("大兴安岭地区新林区人民法院","大兴安岭地区");
		courtCity.put("大兴安岭地区呼中区人民法院","大兴安岭地区");
		courtCity.put("加格达奇区人民法院","大兴安岭地区");
		courtCity.put("大兴安岭地区中级人民法院","大兴安岭地区");
		courtCity.put("松岭区人民法院","大兴安岭地区");
		courtCity.put("新林区人民法院","大兴安岭地区");
		courtCity.put("呼中区人民法院","大兴安岭地区");
		courtCity.put("呼玛县人民法院","大兴安岭地区");
		courtCity.put("塔河县人民法院","大兴安岭地区");
		courtCity.put("漠河县人民法院","大兴安岭地区");
		courtCity.put("图强林区基层法院","大兴安岭地区");
		courtCity.put("阿木尔林区基层法院","大兴安岭地区");
		courtCity.put("十八站林区基层法院","大兴安岭地区");
		courtCity.put("哈尔滨铁路运输中级法院","哈尔滨市");
		courtCity.put("哈尔滨铁路运输法院","哈尔滨市");
		courtCity.put("牡丹江铁路运输法院","哈尔滨市");
		courtCity.put("佳木斯铁路运输法院","哈尔滨市");
		courtCity.put("齐齐哈尔铁路运输法院","哈尔滨市");
		courtCity.put("黑龙江省林区中级人民法院","哈尔滨市");
		courtCity.put("东京城林区基层法院","哈尔滨市");
		courtCity.put("迎春林区基层法院","哈尔滨市");
		courtCity.put("东方红林区基层法院","哈尔滨市");
		courtCity.put("方正林区基层法院","哈尔滨市");
		courtCity.put("双鸭山林区基层法院","哈尔滨市");
		courtCity.put("通北林区基层法院","哈尔滨市");
		courtCity.put("桦南林区基层法院","哈尔滨市");
		courtCity.put("亚布力林区基层法院","哈尔滨市");
		courtCity.put("绥棱林区基层法院","哈尔滨市");
		courtCity.put("沾河林区基层法院","哈尔滨市");
		courtCity.put("穆棱林区基层法院","哈尔滨市");
		courtCity.put("清河林区基层法院","哈尔滨市");
		courtCity.put("八面通林区基层法院","哈尔滨市");
		courtCity.put("绥阳林区基层法院","哈尔滨市");
		courtCity.put("大海林林区基层法院","哈尔滨市");
		courtCity.put("海林林区基层法院","哈尔滨市");
		courtCity.put("柴河林区基层法院","哈尔滨市");
		courtCity.put("林口林区基层法院","哈尔滨市");
		courtCity.put("鹤北林区基层法院","哈尔滨市");
		courtCity.put("鹤立林区基层法院","哈尔滨市");
		courtCity.put("兴隆林区基层法院","哈尔滨市");
		courtCity.put("山河屯林区基层法院","哈尔滨市");
		courtCity.put("苇河林区基层法院","哈尔滨市");
		courtCity.put("黑龙江省农垦中级法院","哈尔滨市");
		courtCity.put("宝泉岭农垦法院","哈尔滨市");
		courtCity.put("建三江农垦法院","哈尔滨市");
		courtCity.put("红兴隆农垦法院","哈尔滨市");
		courtCity.put("九三农垦法院","哈尔滨市");
		courtCity.put("绥化农垦法院","哈尔滨市");
		courtCity.put("北安农垦法院","哈尔滨市");
		courtCity.put("齐齐哈尔农垦法院","哈尔滨市");
		courtCity.put("牡丹江农垦法院","哈尔滨市");
		courtCity.put("山西省高级人民法院","太原市");
		courtCity.put("山西省太原市中级人民法院","太原市");
		courtCity.put("太原市中级人民法院","太原市");
		courtCity.put("太原市小店区人民法院","太原市");
		courtCity.put("太原市迎泽区人民法院","太原市");
		courtCity.put("太原市杏花岭区人民法院","太原市");
		courtCity.put("太原市尖草坪区人民法院","太原市");
		courtCity.put("太原市万柏林区人民法院","太原市");
		courtCity.put("太原市晋源区人民法院","太原市");
		courtCity.put("清徐县人民法院","太原市");
		courtCity.put("阳曲县人民法院","太原市");
		courtCity.put("娄烦县人民法院","太原市");
		courtCity.put("古交市人民法院","太原市");
		courtCity.put("山西省大同市中级人民法院","大同市");
		courtCity.put("大同市中级人民法院","大同市");
		courtCity.put("大同市城区人民法院","大同市");
		courtCity.put("大同市矿区人民法院","大同市");
		courtCity.put("矿区人民法院","大同市");
		courtCity.put("大同市南郊区人民法院","大同市");
		courtCity.put("大同市新荣区人民法院","大同市");
		courtCity.put("阳高县人民法院","大同市");
		courtCity.put("天镇县人民法院","大同市");
		courtCity.put("广灵县人民法院","大同市");
		courtCity.put("灵丘县人民法院","大同市");
		courtCity.put("浑源县人民法院","大同市");
		courtCity.put("左云县人民法院","大同市");
		courtCity.put("大同县人民法院","大同市");
		courtCity.put("山西省阳泉市中级人民法院","阳泉市");
		courtCity.put("阳泉市中级人民法院","阳泉市");
		courtCity.put("阳泉市城区人民法院","阳泉市");
		courtCity.put("阳泉市矿区人民法院","阳泉市");
		courtCity.put("阳泉市郊区人民法院","阳泉市");
		courtCity.put("平定县人民法院","阳泉市");
		courtCity.put("盂县人民法院","阳泉市");
		courtCity.put("山西省长治市中级人民法院","长治市");
		courtCity.put("长治市中级人民法院","长治市");
		courtCity.put("长治市城区人民法院","长治市");
		courtCity.put("长治市郊区人民法院","长治市");
		courtCity.put("长治县人民法院","长治市");
		courtCity.put("襄垣县人民法院","长治市");
		courtCity.put("屯留县人民法院","长治市");
		courtCity.put("平顺县人民法院","长治市");
		courtCity.put("黎城县人民法院","长治市");
		courtCity.put("壶关县人民法院","长治市");
		courtCity.put("长子县人民法院","长治市");
		courtCity.put("武乡县人民法院","长治市");
		courtCity.put("沁县人民法院","长治市");
		courtCity.put("沁源县人民法院","长治市");
		courtCity.put("潞城市人民法院","长治市");
		courtCity.put("山西省晋城市中级人民法院","晋城市");
		courtCity.put("晋城市中级人民法院","晋城市");
		courtCity.put("晋城市城区人民法院","晋城市");
		courtCity.put("沁水县人民法院","晋城市");
		courtCity.put("阳城县人民法院","晋城市");
		courtCity.put("陵川县人民法院","晋城市");
		courtCity.put("泽州县人民法院","晋城市");
		courtCity.put("高平市人民法院","晋城市");
		courtCity.put("山西省朔州市中级人民法院","朔州市");
		courtCity.put("朔州市中级人民法院","朔州市");
		courtCity.put("朔州市朔城区人民法院","朔州市");
		courtCity.put("朔州市平鲁区人民法院","朔州市");
		courtCity.put("山阴县人民法院","朔州市");
		courtCity.put("应县人民法院","朔州市");
		courtCity.put("右玉县人民法院","朔州市");
		courtCity.put("怀仁县人民法院","朔州市");
		courtCity.put("山西省晋中市中级人民法院","晋中市");
		courtCity.put("晋中市中级人民法院","晋中市");
		courtCity.put("晋中市榆次区人民法院","晋中市");
		courtCity.put("榆社县人民法院","晋中市");
		courtCity.put("左权县人民法院","晋中市");
		courtCity.put("和顺县人民法院","晋中市");
		courtCity.put("昔阳县人民法院","晋中市");
		courtCity.put("寿阳县人民法院","晋中市");
		courtCity.put("太谷县人民法院","晋中市");
		courtCity.put("祁县人民法院","晋中市");
		courtCity.put("平遥县人民法院","晋中市");
		courtCity.put("灵石县人民法院","晋中市");
		courtCity.put("介休市人民法院","晋中市");
		courtCity.put("山西省运城市中级人民法院","运城市");
		courtCity.put("运城市中级人民法院","运城市");
		courtCity.put("运城市盐湖区人民法院","运城市");
		courtCity.put("临猗县人民法院","运城市");
		courtCity.put("万荣县人民法院","运城市");
		courtCity.put("闻喜县人民法院","运城市");
		courtCity.put("稷山县人民法院","运城市");
		courtCity.put("新绛县人民法院","运城市");
		courtCity.put("绛县人民法院","运城市");
		courtCity.put("垣曲县人民法院","运城市");
		courtCity.put("夏县人民法院","运城市");
		courtCity.put("平陆县人民法院","运城市");
		courtCity.put("芮城县人民法院","运城市");
		courtCity.put("永济市人民法院","运城市");
		courtCity.put("河津市人民法院","运城市");
		courtCity.put("山西省忻州市中级人民法院","忻州市");
		courtCity.put("忻州市中级人民法院","忻州市");
		courtCity.put("忻州市忻府区人民法院","忻州市");
		courtCity.put("定襄县人民法院","忻州市");
		courtCity.put("五台县人民法院","忻州市");
		courtCity.put("代县人民法院","忻州市");
		courtCity.put("繁峙县人民法院","忻州市");
		courtCity.put("宁武县人民法院","忻州市");
		courtCity.put("静乐县人民法院","忻州市");
		courtCity.put("神池县人民法院","忻州市");
		courtCity.put("五寨县人民法院","忻州市");
		courtCity.put("岢岚县人民法院","忻州市");
		courtCity.put("河曲县人民法院","忻州市");
		courtCity.put("保德县人民法院","忻州市");
		courtCity.put("偏关县人民法院","忻州市");
		courtCity.put("原平市人民法院","忻州市");
		courtCity.put("山西省临汾市中级人民法院","临汾市");
		courtCity.put("临汾市中级人民法院","临汾市");
		courtCity.put("临汾市尧都区人民法院","临汾市");
		courtCity.put("曲沃县人民法院","临汾市");
		courtCity.put("翼城县人民法院","临汾市");
		courtCity.put("襄汾县人民法院","临汾市");
		courtCity.put("洪洞县人民法院","临汾市");
		courtCity.put("古县人民法院","临汾市");
		courtCity.put("安泽县人民法院","临汾市");
		courtCity.put("浮山县人民法院","临汾市");
		courtCity.put("吉县人民法院","临汾市");
		courtCity.put("乡宁县人民法院","临汾市");
		courtCity.put("大宁县人民法院","临汾市");
		courtCity.put("隰县人民法院","临汾市");
		courtCity.put("永和县人民法院","临汾市");
		courtCity.put("蒲县人民法院","临汾市");
		courtCity.put("汾西县人民法院","临汾市");
		courtCity.put("侯马市人民法院","临汾市");
		courtCity.put("霍州市人民法院","临汾市");
		courtCity.put("山西省吕梁市中级人民法院","吕梁市");
		courtCity.put("吕梁市中级人民法院","吕梁市");
		courtCity.put("吕梁市离石区人民法院","吕梁市");
		courtCity.put("文水县人民法院","吕梁市");
		courtCity.put("交城县人民法院","吕梁市");
		courtCity.put("兴县人民法院","吕梁市");
		courtCity.put("临县人民法院","吕梁市");
		courtCity.put("柳林县人民法院","吕梁市");
		courtCity.put("石楼县人民法院","吕梁市");
		courtCity.put("岚县人民法院","吕梁市");
		courtCity.put("方山县人民法院","吕梁市");
		courtCity.put("中阳县人民法院","吕梁市");
		courtCity.put("交口县人民法院","吕梁市");
		courtCity.put("孝义市人民法院","吕梁市");
		courtCity.put("汾阳市人民法院","吕梁市");
		courtCity.put("太原铁路运输中级法院","太原市");
		courtCity.put("临汾铁路运输法院","太原市");
		courtCity.put("太原铁路运输法院","太原市");
		courtCity.put("大同铁路运输法院","太原市");
		courtCity.put("廊坊经济技术开发区人民法院","廊坊市");
		courtCity.put("满城区人民法院","保定市");
		courtCity.put("邯郸市肥乡区人民法院","邯郸市");
		courtCity.put("丰南市人民法院","唐山市");
		courtCity.put("徐水区人民法院","保定市");
		courtCity.put("汉沽农场法庭汉沽人民法院","唐山市");
		courtCity.put("河北省献县人民法院","沧州市");
		courtCity.put("保定市竞秀区人民法院","保定市");
		courtCity.put("双城市人民法院","哈尔滨市");
		courtCity.put("张家口市万全区人民法院","张家口市");
		courtCity.put("穆棱县人民法院","穆棱市");
		courtCity.put("冀州区人民法院","冀州市");
		courtCity.put("石家庄市中级人民法院","石家庄市");
		courtCity.put("清苑区人民法院","保定市");
		courtCity.put("阿城市人民法院","哈尔滨市");
		courtCity.put("鹿泉区人民法院","石家庄市");
		courtCity.put("河北省大厂回族自治县人民法院","石家庄市");
		courtCity.put("保定市莲池区人民法院","保定市");
		courtCity.put("大庆市中级人民法院","大庆市");
		courtCity.put("平泉市人民法院","承德市");
		courtCity.put("大厂县人民法院","廊坊市");
		courtCity.put("芦台农场法庭芦台人民法院","唐山市");
		courtCity.put("邯郸市永年区人民法院","邯郸市");
		courtCity.put("河北唐山芦台经济开发区人民法庭芦台人民法院","唐山市");
		courtCity.put("丰宁县满族自治县人民法院","承德市");
		courtCity.put("丰宁县满族自治县人民法院","承德市");
		courtCity.put("内邱县人民法院","邢台市");
		courtCity.put("矿区法院","大同市");
		courtCity.put("东港市人民法院","丹东市");
		courtCity.put("丹东市元宝区人民法院","丹东市");
		courtCity.put("丹东市振兴区人民法院","丹东市");
		courtCity.put("丹东市振安区人民法院","丹东市");
		courtCity.put("丹东铁路运输法院","沈阳市");
		courtCity.put("义县人民法院","锦州市");
		courtCity.put("兴城市人民法院","葫芦岛市");
		courtCity.put("凌海市人民法院","锦州市");
		courtCity.put("凌源市人民法院","朝阳市");
		courtCity.put("凤城市人民法院","丹东市");
		courtCity.put("北票市人民法院","朝阳市");
		courtCity.put("北镇市人民法院","锦州市");
		courtCity.put("台安县人民法院","鞍山市");
		courtCity.put("喀喇沁左翼蒙古族自治县人民法院","朝阳市");
		courtCity.put("大洼县人民法院","盘锦市");
		courtCity.put("大石桥市人民法院","营口市");
		courtCity.put("大连市中山区人民法院","大连市");
		courtCity.put("大连市旅顺口区人民法院","大连市");
		courtCity.put("大连市沙河口区人民法院","大连市");
		courtCity.put("大连市甘井子区人民法院","大连市");
		courtCity.put("大连市西岗区人民法院","大连市");
		courtCity.put("大连市金州区人民法院","大连市");
		courtCity.put("大连海事法院","大连市");
		courtCity.put("大连经济技术开发区人民法院","大连市");
		courtCity.put("大连铁路运输法院","沈阳市");
		courtCity.put("宽甸满族自治县人民法院","丹东市");
		courtCity.put("岫岩满族自治县人民法院","鞍山市");
		courtCity.put("鞍山市中级人民法院","鞍山市");
		courtCity.put("庄河市人民法院","大连市");
		courtCity.put("康平县人民法院","沈阳市");
		courtCity.put("建平县人民法院","朝阳市");
		courtCity.put("建昌县人民法院","葫芦岛市");
		courtCity.put("开原市人民法院","铁岭市");
		courtCity.put("彰武县人民法院","阜新市");
		courtCity.put("抚顺县人民法院","抚顺市");
		courtCity.put("抚顺市东洲区人民法院","抚顺市");
		courtCity.put("抚顺市新抚区人民法院","抚顺市");
		courtCity.put("抚顺市望花区人民法院","抚顺市");
		courtCity.put("抚顺市顺城区人民法院","抚顺市");
		courtCity.put("新宾满族自治县人民法院","抚顺市");
		courtCity.put("新民市人民法院","沈阳市");
		courtCity.put("昌图县人民法院","铁岭市");
		courtCity.put("普兰店市人民法院","大连市");
		courtCity.put("朝阳县人民法院","朝阳市");
		courtCity.put("喀左县人民法院","朝阳市");
		courtCity.put("朝阳市双塔区人民法院","朝阳市");
		courtCity.put("朝阳市龙城区人民法院","朝阳市");
		courtCity.put("本溪市南芬区人民法院","本溪市");
		courtCity.put("本溪市平山区人民法院","本溪市");
		courtCity.put("本溪市明山区人民法院","本溪市");
		courtCity.put("本溪市溪湖区人民法院","本溪市");
		courtCity.put("本溪满族自治县人民法院","本溪市");
		courtCity.put("桓仁满族自治县人民法院","本溪市");
		courtCity.put("沈阳市于洪区人民法院","沈阳市");
		courtCity.put("沈阳市和平区人民法院","沈阳市");
		courtCity.put("沈阳市大东区人民法院","沈阳市");
		courtCity.put("沈阳市沈北新区人民法院","沈阳市");
		courtCity.put("沈阳市沈河区人民法院","沈阳市");
		courtCity.put("沈阳市浑南区人民法院","沈阳市");
		courtCity.put("沈阳市皇姑区人民法院","沈阳市");
		courtCity.put("沈阳市苏家屯区人民法院","沈阳市");
		courtCity.put("沈阳市铁西区人民法院","沈阳市");
		courtCity.put("沈阳经济技术开发区人民法院","沈阳市");
		courtCity.put("沈阳铁路运输中级法院","沈阳市");
		courtCity.put("沈阳铁路运输法院","沈阳市");
		courtCity.put("沈阳高新技术产业开发区人民法院","沈阳市");
		courtCity.put("法库县人民法院","沈阳市");
		courtCity.put("海城市人民法院","鞍山市");
		courtCity.put("清原满族自治县人民法院","抚顺市");
		courtCity.put("灯塔市人民法院","辽阳市");
		courtCity.put("瓦房店市人民法院","大连市");
		courtCity.put("盖州市人民法院","营口市");
		courtCity.put("盘山县人民法院","盘锦市");
		courtCity.put("盘锦市兴隆台区人民法院","盘锦市");
		courtCity.put("盘锦市双台子区人民法院","盘锦市");
		courtCity.put("绥中县人民法院","葫芦岛市");
		courtCity.put("营口市站前区人民法院","营口市");
		courtCity.put("营口市老边区人民法院","营口市");
		courtCity.put("营口市西市区人民法院","营口市");
		courtCity.put("营口市鲅鱼圈区人民法院","营口市");
		courtCity.put("葫芦岛市南票区人民法院","葫芦岛市");
		courtCity.put("葫芦岛市连山区人民法院","葫芦岛市");
		courtCity.put("葫芦岛市龙港区人民法院","葫芦岛市");
		courtCity.put("西丰县人民法院","铁岭市");
		courtCity.put("调兵山市人民法院","铁岭市");
		courtCity.put("辽中县人民法院","沈阳市");
		courtCity.put("辽宁省丹东市中级人民法院","丹东市");
		courtCity.put("辽宁省大连市中级人民法院","大连市");
		courtCity.put("辽宁省抚顺市中级人民法院","抚顺市");
		courtCity.put("辽宁省朝阳市中级人民法院","朝阳市");
		courtCity.put("辽宁省本溪市中级人民法院","本溪市");
		courtCity.put("辽宁省沈阳市中级人民法院","沈阳市");
		courtCity.put("辽宁省盘锦市中级人民法院","盘锦市");
		courtCity.put("辽宁省营口市中级人民法院","营口市");
		courtCity.put("辽宁省葫芦岛市中级人民法院","葫芦岛市");
		courtCity.put("辽宁省辽河中级人民法院","盘锦市");
		courtCity.put("辽宁省辽阳市中级人民法院","辽阳市");
		courtCity.put("辽宁省铁岭市中级人民法院","铁岭市");
		courtCity.put("辽宁省锦州市中级人民法院","锦州市");
		courtCity.put("辽宁省阜新市中级人民法院","阜新市");
		courtCity.put("辽宁省鞍山市中级人民法院","鞍山市");
		courtCity.put("丹东市中级人民法院","丹东市");
		courtCity.put("大连市中级人民法院","大连市");
		courtCity.put("抚顺市中级人民法院","抚顺市");
		courtCity.put("朝阳市中级人民法院","朝阳市");
		courtCity.put("本溪市中级人民法院","本溪市");
		courtCity.put("沈阳市中级人民法院","沈阳市");
		courtCity.put("盘锦市中级人民法院","盘锦市");
		courtCity.put("营口市中级人民法院","营口市");
		courtCity.put("葫芦岛市中级人民法院","葫芦岛市");
		courtCity.put("辽河中级人民法院","盘锦市");
		courtCity.put("大洼区人民法院","盘锦市");
		courtCity.put("辽阳市中级人民法院","辽阳市");
		courtCity.put("铁岭市中级人民法院","铁岭市");
		courtCity.put("锦州市中级人民法院","锦州市");
		courtCity.put("阜新市中级人民法院","阜新市");
		courtCity.put("辽宁省高级人民法院","沈阳市");
		courtCity.put("辽河人民法院","盘锦市");
		courtCity.put("辽阳县人民法院","辽阳市");
		courtCity.put("辽阳市太子河区人民法院","辽阳市");
		courtCity.put("辽阳市宏伟区人民法院","辽阳市");
		courtCity.put("辽阳市弓长岭区人民法院","辽阳市");
		courtCity.put("辽阳市文圣区人民法院","辽阳市");
		courtCity.put("辽阳市白塔区人民法院","辽阳市");
		courtCity.put("铁岭县人民法院","铁岭市");
		courtCity.put("铁岭市清河区人民法院","铁岭市");
		courtCity.put("铁岭市银州区人民法院","铁岭市");
		courtCity.put("锦州市凌河区人民法院","锦州市");
		courtCity.put("锦州市古塔区人民法院","锦州市");
		courtCity.put("锦州市太和区人民法院","锦州市");
		courtCity.put("锦州铁路运输法院","沈阳市");
		courtCity.put("长海县人民法院","大连市");
		courtCity.put("阜新市太平区人民法院","阜新市");
		courtCity.put("阜新市新邱区人民法院","阜新市");
		courtCity.put("阜新市海州区人民法院","阜新市");
		courtCity.put("阜新市清河门区人民法院","阜新市");
		courtCity.put("阜新市细河区人民法院","阜新市");
		courtCity.put("阜新蒙古族自治县人民法院","阜新市");
		courtCity.put("鞍山市千山区人民法院","鞍山市");
		courtCity.put("鞍山市立山区人民法院","鞍山市");
		courtCity.put("鞍山市铁东区人民法院","鞍山市");
		courtCity.put("鞍山市铁西区人民法院","鞍山市");
		courtCity.put("黑山县人民法院","锦州市");
		courtCity.put("辽中区人民法院","沈阳市");
		courtCity.put("大连高新技术产业园区人民法院","大连市");
		courtCity.put("丘北县人民法院","文山壮族苗族自治州");
		courtCity.put("个旧市人民法院","红河哈尼族彝族自治州");
		courtCity.put("临沧市临翔区人民法院","临沧市");
		courtCity.put("丽江市古城区人民法院","丽江市");
		courtCity.put("云南省临沧市中级人民法院","临沧市");
		courtCity.put("云南省丽江市中级人民法院","丽江市");
		courtCity.put("云南省保山市中级人民法院","保山市");
		courtCity.put("云南省大理白族自治州中级人民法院","大理白族自治州");
		courtCity.put("云南省德宏傣族景颇族自治州中级人民法院","德宏傣族景颇族自治州");
		courtCity.put("云南省怒江傈僳族自治州中级人民法院","怒江傈僳族自治州");
		courtCity.put("云南省文山壮族苗族自治州中级人民法院","文山壮族苗族自治州");
		courtCity.put("云南省昆明市中级人民法院","昆明市");
		courtCity.put("云南省昭通市中级人民法院","昭通市");
		courtCity.put("云南省普洱市中级人民法院","普洱市");
		courtCity.put("云南省曲靖市中级人民法院","曲靖市");
		courtCity.put("曲靖市沾益区人民法院","曲靖市");
		courtCity.put("云南省楚雄彝族自治州中级人民法院","楚雄彝族自治州");
		courtCity.put("云南省玉溪市中级人民法院","玉溪市");
		courtCity.put("玉溪市江川区人民法院","玉溪市");
		courtCity.put("云南省红河哈尼族彝族自治州中级人民法院","红河哈尼族彝族自治州");
		courtCity.put("云南省西双版纳傣族自治州中级人民法院","西双版纳傣族自治州");
		courtCity.put("云南省迪庆藏族自治州中级人民法院","迪庆藏族自治州");
		courtCity.put("云南省高级人民法院","昆明市");
		courtCity.put("临沧市中级人民法院","临沧市");
		courtCity.put("丽江市中级人民法院","丽江市");
		courtCity.put("保山市中级人民法院","保山市");
		courtCity.put("大理白族自治州中级人民法院","大理白族自治州");
		courtCity.put("德宏傣族景颇族自治州中级人民法院","德宏傣族景颇族自治州");
		courtCity.put("怒江傈僳族自治州中级人民法院","怒江傈僳族自治州");
		courtCity.put("文山壮族苗族自治州中级人民法院","文山壮族苗族自治州");
		courtCity.put("昆明市中级人民法院","昆明市");
		courtCity.put("昭通市中级人民法院","昭通市");
		courtCity.put("普洱市中级人民法院","普洱市");
		courtCity.put("曲靖市中级人民法院","曲靖市");
		courtCity.put("楚雄彝族自治州中级人民法院","楚雄彝族自治州");
		courtCity.put("玉溪市中级人民法院","玉溪市");
		courtCity.put("红河哈尼族彝族自治州中级人民法院","红河哈尼族彝族自治州");
		courtCity.put("西双版纳傣族自治州中级人民法院","西双版纳傣族自治州");
		courtCity.put("迪庆藏族自治州中级人民法院","迪庆藏族自治州");
		courtCity.put("云县人民法院","临沧市");
		courtCity.put("云龙县人民法院","大理白族自治州");
		courtCity.put("会泽县人民法院","曲靖市");
		courtCity.put("保山市隆阳区人民法院","保山市");
		courtCity.put("元江哈尼族彝族傣族自治县人民法院","玉溪市");
		courtCity.put("元谋县人民法院","楚雄彝族自治州");
		courtCity.put("元阳县人民法院","红河哈尼族彝族自治州");
		courtCity.put("兰坪白族普米族自治县人民法院","怒江傈僳族自治州");
		courtCity.put("凤庆县人民法院","临沧市");
		courtCity.put("剑川县人民法院","大理白族自治州");
		courtCity.put("勐海县人民法院","西双版纳傣族自治州");
		courtCity.put("勐腊县人民法院","西双版纳傣族自治州");
		courtCity.put("华坪县人民法院","丽江市");
		courtCity.put("华宁县人民法院","玉溪市");
		courtCity.put("南华县人民法院","楚雄彝族自治州");
		courtCity.put("南涧彝族自治县人民法院","大理白族自治州");
		courtCity.put("双柏县人民法院","楚雄彝族自治州");
		courtCity.put("双江拉祜族佤族布朗族傣族自治县人民法院","临沧市");
		courtCity.put("墨江哈尼族自治县人民法院","普洱市");
		courtCity.put("大关县人民法院","昭通市");
		courtCity.put("大姚县人民法院","楚雄彝族自治州");
		courtCity.put("大理市人民法院","大理白族自治州");
		courtCity.put("姚安县人民法院","楚雄彝族自治州");
		courtCity.put("威信县人民法院","昭通市");
		courtCity.put("孟连傣族拉祜族佤族自治县人民法院","普洱市");
		courtCity.put("宁洱哈尼族彝族自治县人民法院","普洱市");
		courtCity.put("宁蒗彝族自治县人民法院","丽江市");
		courtCity.put("安宁市人民法院","昆明市");
		courtCity.put("宜良县人民法院","昆明市");
		courtCity.put("宣威市人民法院","曲靖市");
		courtCity.put("宾川县人民法院","大理白族自治州");
		courtCity.put("富宁县人民法院","文山壮族苗族自治州");
		courtCity.put("富民县人民法院","昆明市");
		courtCity.put("富源县人民法院","曲靖市");
		courtCity.put("寻甸回族彝族自治县人民法院","昆明市");
		courtCity.put("屏边苗族自治县人民法院","红河哈尼族彝族自治州");
		courtCity.put("峨山彝族自治县人民法院","玉溪市");
		courtCity.put("嵩明县人民法院","昆明市");
		courtCity.put("巍山彝族回族自治县人民法院","大理白族自治州");
		courtCity.put("巧家县人民法院","昭通市");
		courtCity.put("师宗县人民法院","曲靖市");
		courtCity.put("广南县人民法院","文山壮族苗族自治州");
		courtCity.put("建水县人民法院","红河哈尼族彝族自治州");
		courtCity.put("开远市人民法院","红河哈尼族彝族自治州");
		courtCity.put("开远铁路运输法院","昆明铁路运输中级法院管辖");
		courtCity.put("弥勒市人民法院","红河哈尼族彝族自治州");
		courtCity.put("弥渡县人民法院","大理白族自治州");
		courtCity.put("彝良县人民法院","昭通市");
		courtCity.put("德钦县人民法院","迪庆藏族自治州");
		courtCity.put("文山市人民法院","文山壮族苗族自治州");
		courtCity.put("新平彝族傣族自治县人民法院","玉溪市");
		courtCity.put("施甸县人民法院","保山市");
		courtCity.put("昆明市东川区人民法院","昆明市");
		courtCity.put("昆明市五华区人民法院","昆明市");
		courtCity.put("昆明市呈贡区人民法院","昆明市");
		courtCity.put("昆明市官渡区人民法院","昆明市");
		courtCity.put("昆明市盘龙区人民法院","昆明市");
		courtCity.put("昆明市西山区人民法院","昆明市");
		courtCity.put("昆明铁路运输中级法院","昆明铁路运输中级法院管辖");
		courtCity.put("昆明铁路运输法院","昆明铁路运输中级法院管辖");
		courtCity.put("昌宁县人民法院","保山市");
		courtCity.put("易门县人民法院","玉溪市");
		courtCity.put("昭通市昭阳区人民法院","昭通市");
		courtCity.put("晋宁县人民法院","昆明市");
		courtCity.put("普洱市思茅区人民法院","普洱市");
		courtCity.put("景东彝族自治县人民法院","普洱市");
		courtCity.put("景洪市人民法院","西双版纳傣族自治州");
		courtCity.put("景谷傣族彝族自治县人民法院","普洱市");
		courtCity.put("曲靖市麒麟区人民法院","曲靖市");
		courtCity.put("梁河县人民法院","德宏傣族景颇族自治州");
		courtCity.put("楚雄市人民法院","楚雄彝族自治州");
		courtCity.put("武定县人民法院","楚雄彝族自治州");
		courtCity.put("水富县人民法院","昭通市");
		courtCity.put("永仁县人民法院","楚雄彝族自治州");
		courtCity.put("永善县人民法院","昭通市");
		courtCity.put("永平县人民法院","大理白族自治州");
		courtCity.put("永德县人民法院","临沧市");
		courtCity.put("永胜县人民法院","丽江市");
		courtCity.put("江城哈尼族彝族自治县人民法院","普洱市");
		courtCity.put("江川县人民法院","玉溪市");
		courtCity.put("沧源佤族自治县人民法院","临沧市");
		courtCity.put("河口瑶族自治县人民法院","红河哈尼族彝族自治州");
		courtCity.put("沾益县人民法院","曲靖市");
		courtCity.put("泸水县人民法院","怒江傈僳族自治州");
		courtCity.put("泸西县人民法院","红河哈尼族彝族自治州");
		courtCity.put("洱源县人民法院","大理白族自治州");
		courtCity.put("漾濞彝族自治县人民法院","大理白族自治州");
		courtCity.put("澄江县人民法院","玉溪市");
		courtCity.put("澜沧拉祜族自治县人民法院","普洱市");
		courtCity.put("牟定县人民法院","楚雄彝族自治州");
		courtCity.put("玉溪市红塔区人民法院","玉溪市");
		courtCity.put("玉龙纳西族自治县人民法院","丽江市");
		courtCity.put("瑞丽市人民法院","德宏傣族景颇族自治州");
		courtCity.put("盈江县人民法院","德宏傣族景颇族自治州");
		courtCity.put("盐津县人民法院","昭通市");
		courtCity.put("石屏县人民法院","红河哈尼族彝族自治州");
		courtCity.put("石林彝族自治县人民法院","昆明市");
		courtCity.put("砚山县人民法院","文山壮族苗族自治州");
		courtCity.put("祥云县人民法院","大理白族自治州");
		courtCity.put("禄丰县人民法院","楚雄彝族自治州");
		courtCity.put("禄劝彝族苗族自治县人民法院","昆明市");
		courtCity.put("福贡县人民法院","怒江傈僳族自治州");
		courtCity.put("红河县人民法院","红河哈尼族彝族自治州");
		courtCity.put("绥江县人民法院","昭通市");
		courtCity.put("维西傈僳族自治县人民法院","迪庆藏族自治州");
		courtCity.put("绿春县人民法院","红河哈尼族彝族自治州");
		courtCity.put("罗平县人民法院","曲靖市");
		courtCity.put("耿马傣族佤族自治县人民法院","临沧市");
		courtCity.put("腾冲县人民法院","保山市");
		courtCity.put("芒市人民法院","德宏傣族景颇族自治州");
		courtCity.put("蒙自市人民法院","红河哈尼族彝族自治州");
		courtCity.put("西畴县人民法院","文山壮族苗族自治州");
		courtCity.put("西盟佤族自治县人民法院","普洱市");
		courtCity.put("贡山独龙族怒族自治县人民法院","怒江傈僳族自治州");
		courtCity.put("通海县人民法院","玉溪市");
		courtCity.put("金平苗族瑶族傣族自治县人民法院","红河哈尼族彝族自治州");
		courtCity.put("镇康县人民法院","临沧市");
		courtCity.put("镇沅彝族哈尼族拉祜族自治县人民法院","普洱市");
		courtCity.put("镇雄县人民法院","昭通市");
		courtCity.put("陆良县人民法院","曲靖市");
		courtCity.put("陇川县人民法院","德宏傣族景颇族自治州");
		courtCity.put("香格里拉县人民法院","迪庆藏族自治州");
		courtCity.put("马关县人民法院","文山壮族苗族自治州");
		courtCity.put("马龙县人民法院","曲靖市");
		courtCity.put("鲁甸县人民法院","昭通市");
		courtCity.put("鹤庆县人民法院","大理白族自治州");
		courtCity.put("麻栗坡县人民法院","文山壮族苗族自治州");
		courtCity.put("龙陵县人民法院","保山市");
		courtCity.put("香格里拉市人民法院","迪庆藏族自治州");
		courtCity.put("泸水市人民法院","怒江傈僳族自治州 ");
	}
	public String getSelectCmd() throws IOException
	{
		File src=new File("conf/notice_type4.sql");
//		File src=new File("conf/notice_supplement.sql");
		FileInputStream reader=new FileInputStream(src);
		int l=(int)src.length();
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
		System.out.println("开始解析类型4");
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
		
		File f=new File(dir.getAbsolutePath()+"/notice_type4.txt");
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
				String kaiTingRiQi="";
				String anYou="";
				String anHao="";
				String shenLiFaTing="";				
				String dangShiRen="";	
				String zhuShenFaGuan="";
				String chengBanTing="";
				String md5=null;
				content=content.replace("(", "（").replace(")", "）");				
				content=content.replace("二O", "二〇");
				content=content.replace("提起公诉的", "诉");
				content=content.replace("（）", "");
				String faYuanMingCheng=result.getString("shen_li_fa_yuan");
//				System.out.println(faYuanMingCheng);
//				System.out.println(content);
//				if(content.indexOf("定于")>0)
//				{
//					faYuanMingCheng=content.substring(0, content.indexOf("定于"));
//				}
				
				HashMap<String,String> parseResult=DetailCourtNotice.parse(content);
				if(parseResult==null)
				{
					continue;
				}
				anYou=parseResult.get("案由");
				shenLiFaTing=parseResult.get("审理法庭");
				dangShiRen=parseResult.get("当事人");
				anHao=parseResult.get("案号");
				dangShiRen=pattern.matcher(dangShiRen).replaceAll(""); 
				if (province.equals("天津市"))
				{
					kaiTingRiQi=result.getString("riqi");
				}
				else
				{
					kaiTingRiQi=parseResult.get("开庭日期");
				}	
				if(shenLiFaTing.indexOf("法院")>0)
				{
					faYuanMingCheng=shenLiFaTing.substring(0, shenLiFaTing.indexOf("法院")+2);
					shenLiFaTing=shenLiFaTing.substring(shenLiFaTing.indexOf("法院")+2, shenLiFaTing.length());
				}
				if (city.equals("不确定"))
				{
					city=courtCity.get(faYuanMingCheng);
				}
				
				if(city!=null && city.equals("吉林市"))
				{
					province="吉林省";
				}		
//				if(kaiTingRiQi.indexOf("年")==4 && kaiTingRiQi.indexOf("月")-kaiTingRiQi.indexOf("年")<=2 && !(kaiTingRiQi.endsWith("日")))
//				{
//					kaiTingRiQi=kaiTingRiQi+"日";
//				}

				kaiTingRiQi=ParseDate.parse(kaiTingRiQi);	
				dangShiRen=pattern1.matcher(dangShiRen).replaceAll("").replace("；", ";").replace("：", ":").replace(";:", ";");
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
				for(String company:companyArr)
				{
					company=pattern1.matcher(company).replaceAll("").replace(" ", "");
//					company=company.replace(" ", "");
					if(!(company.contains("原告")
							|| company.contains("被告")
							|| company.contains("被上诉人")
							|| company.contains("上诉人")
							|| company.isEmpty()
							|| company.contains("null")
							|| company.equals("（")
//							|| company.contains("公司")
//							|| company.contains("集团")
//							|| company.contains("企业")
//							|| company.contains("超市")
//							|| company.contains("有限合伙")
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
			}
			fw.close();
		} catch (SQLException e) {
			e.printStackTrace();
			remark1=SysFunc.getError(e);
			jobStatus1=1;
		}
		WriteJobStatus.writeJobStatus("开庭公告解析(类型4)", dt, jobStatus1, remark1.replace("'", ""));
//		String[] args=new String[]
//				{"hive"
//				,"-e"
//				,String.format("\"LOAD DATA LOCAL INPATH '%s' into TABLE ods.kai_ting_gong_gao partition(dt='%s')\"", path,dt)
//				};
//		int jobStatus2=0;
//		String remark2="";
//		try {
//			remark2=ExecShell.exec(args);
//		} catch (Exception e) {
//			remark2=SysFunc.getError(e);
//			jobStatus2=1;
//		}
//		WriteJobStatus.writeJobStatus("开庭公告导入hive(类型4)", dt, jobStatus2, remark2.replace("'", ""));
//		String[] load4HbaseArgs=new String[]
//				{"sh"
//				,"home/likai/ImportCourtNoticeToHbase"
//				,dt	
//				};
//		int jobStatus3=0;
//		String remark3="";
//		try {
//			remark3=ExecShell.exec(load4HbaseArgs);
//		} catch (Exception e) {
//			remark3=SysFunc.getError(e);
//			jobStatus3=1;
//		}
//		WriteJobStatus.writeJobStatus("开庭公告导入hbase(类型4)", dt, jobStatus3, remark3.replace("'", ""));
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, ParseException, InterruptedException
	{
		String[] newArgs={"--startDt=2017-08-24","--stopDt=2017-08-25"};
		JobConfig jobConf=new JobConfig(newArgs);
		CourtNoticeType4Job job =new CourtNoticeType4Job();
		job.run(jobConf);
	}

}
