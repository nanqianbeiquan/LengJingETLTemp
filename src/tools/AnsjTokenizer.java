package tools;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

public class AnsjTokenizer {
	public static void main(String[] args){
		String n = "家食品药品监督管理局《药品生产质量管理规范认证管理办法》的规定，经现场检查和审核批准，平安银行股份有限公司,宝鸡向源中药饮片有限责任公司等39家药品生产企业符合《药品生产质量管理规范（2010年修订）》要求，发给《药品GMP证书》。特此公告。　陕西省食品药品监督管理局2014年04月29日附表：陕西省药品GMP认证目录证书编号企业名称地址认证范围认证日期有效期至发证机关SN20130053宝";
		for(Term t: NlpAnalysis.parse(n)){
			System.out.println(t.getName() + " " + t.getOffe());
		}
	}	
}
