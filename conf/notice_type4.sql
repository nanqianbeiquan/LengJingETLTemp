-- set @today=curdate();
-- set @yesterday=date_sub(curdate(), INTERVAL 1 DAY);
-- set @tomorrow=date_sub(curdate(), INTERVAL -1 DAY);
-- set @start_dt=@yesterday;
-- set @stop_dt=@today;


-- 山东高院
select
'山东省高级人民法院' shen_li_fa_yuan,
neirong content,
'山东省' province,
'济南市' city,
'' riqi
from sd_gaoyuan
where updatetime >=@start_dt and updatetime<@stop_dt
union all
-- 福建法院
select 
'福建省高级人民法院' shen_li_fa_yuan,
gg_detail content,
'福建省' province,
'福州市' city,
'' riqi
from fjcourt
where add_time >=@start_dt and add_time<@stop_dt
union all
-- 福州法院
select
'福州市中级人民法院' shen_li_fa_yuan,
nei_rong content,
'福建省' province,
'福州市' city,
'' riqi
from fj_fuzhou
where updatetime>=@start_dt and updatetime<@stop_dt
union all
-- 福建三明法院
select
'三明市中级人民法院' shen_li_fa_yuan,
nei_rong content,
'福建省' province,
'三明市' city,
'' riqi
from fj_sanming
where updatetime>=@start_dt and updatetime<@stop_dt
union all
-- 宁波海事法院
select
'宁波海事法院' shen_li_fa_yuan,
gg_detail content,
'浙江省' province,
'宁波市' city,
'' riqi
from nbhsfy
where add_time>=@start_dt and add_time<@stop_dt
union all
-- 莆田市中级人民法院
select
'莆田市中级人民法院' shen_li_fa_yuan,
gg_detail content,
'福建省' province,
'莆田市' city,
'' riqi
from putianfy
where add_time>=@start_dt and add_time<@stop_dt
union all
-- 南平市中级人民法院
select
'南平市中级人民法院' shen_li_fa_yuan,
gg_detail content,
'福建省' province,
'南平市' city,
'' riqi
from nanpingfy
where add_time>=@start_dt and add_time<@stop_dt
union all
-- 宁德市中级人民法院
select
'宁德市中级人民法院' shen_li_fa_yuan,
gg_detail content,
'福建省' province,
'宁德市' city,
'' riqi
from ningdefy
where add_time>=@start_dt and add_time<@stop_dt
union all
-- 最高人民法院
select
'最高人民法院' shen_li_fa_yuan,
gg_detail content,
'北京市' province,
'北京市' city,
'' riqi
from zgrmfy
where add_time>=@start_dt and add_time<@stop_dt
union all
-- 衢州市中级人民法院
select
'衢州市中级人民法院' shen_li_fa_yuan,
nei_rong content,
'浙江省' province,
'衢州市' city,
'' riqi
from zj_quzhou
where updatetime>=@start_dt and updatetime<@stop_dt
union all
-- 龙岩市中级人民法院
select
'龙岩市中级人民法院' shen_li_fa_yuan,
nei_rong content,
'福建省' province,
'龙岩市' city,
'' riqi
from fj_longyan
where updatetime>=@start_dt and updatetime<@stop_dt
union all
-- 天津法院
select
fa_yuan shen_li_fa_yuan,
an_you content,
'天津市' province,
'天津市' city,
ri_qi riqi
from tianjin2nd
where updatetime>=@start_dt and updatetime<@stop_dt
union all
-- 四川省甘孜藏族自治州中级人民法院
select
'' shen_li_fa_yuan,
gg_detail content,
'四川省' province,
'甘孜藏族自治州' city,
'' riqi
from sc_ganzizangzu2nd
where add_time>=@start_dt and add_time<@stop_dt
union all
-- 四川省高级人民法院
select
'' shen_li_fa_yuan,
gg_detail content,
'四川省' province,
'眉山市' city,
'' riqi
from sc_meishan2nd
where add_time>=@start_dt and add_time<@stop_dt
union all
-- 宿州市中级人民法院
select
'' shen_li_fa_yuan,
nei_rong content,
'安徽省' province,
'宿州市' city,
'' riqi
from ah_suzhou2nd
where updatetime>=@start_dt and updatetime<@stop_dt
union all
-- 石家庄市人民法院
select
'' shen_li_fa_yuan,
gong_gao content,
'河北省' province,
'石家庄市' city,
'' riqi
from hb_shijiazhuang3rd
where add_time>=@start_dt and add_time<@stop_dt
union all
-- 广西壮族自治区北海市中级人民法院
select
'广西壮族自治区北海市中级人民法院' shen_li_fa_yuan,
nei_rong content,
'广西省' province,
'北海市' city,
'' riqi
from gx_beihai2nd
where updatetime>=@start_dt and updatetime<@stop_dt
union all
-- 河北省中级法院
select
fayuan shen_li_fa_yuan,
gong_gao content,
'河北省' province,
'不确定' city,
'' riqi
from hb_zhongjifayuan3rd
where add_time>=@start_dt and add_time<@stop_dt
union all
-- 江西省南昌市中级人民法院
select
'江西省南昌市中级人民法院' shen_li_fa_yuan,
gg_content content,
'江西省' province,
'南昌市' city,
'' riqi
from jx_nanchang3rd
where add_time>=@start_dt and add_time<@stop_dt
union all
-- 江西省景德镇市中级人民法院
select
'' shen_li_fa_yuan,
gong_gao content,
'江西省' province,
'景德镇市' city,
'' riqi
from jx_jingdezhen3rd
where add_time>=@start_dt and add_time<@stop_dt
union all
-- 江西省抚州中级人民法院
select
'' shen_li_fa_yuan,
gong_gao content,
'江西省' province,
'抚州市' city,
'' riqi
from jx_fuzhou3rd
where add_time>=@start_dt and add_time<@stop_dt
union all
-- 梅州市中级人民法院
select
'梅州市中级人民法院' shen_li_fa_yuan,
gg_detail content,
'广东省' province,
'梅州市' city,
'' riqi
from meizhoufy
where add_time>=@start_dt and add_time<@stop_dt
union all
-- 安徽省铜陵市中级人民法院
select
'安徽省铜陵市中级人民法院' shen_li_fa_yuan,
nei_rong content,
'安徽省' province,
'铜陵市' city,
'' riqi
from ah_tongling2nd
where updatetime>=@start_dt and updatetime<@stop_dt and an_you='公告'
union all
-- 河北法院网
select
'' shen_li_fa_yuan,
nei_rong content,
'河北省' province,
'不确定' city,
'' riqi
from hebei
where updatetime>=@start_dt and updatetime<@stop_dt
union all
-- 黑龙江法院
select
'' shen_li_fa_yuan,
nei_rong content,
'黑龙江省' province,
'不确定' city,
'' riqi
from hlj
where updatetime>=@start_dt and updatetime<@stop_dt
union all
-- 山西法院
select
fa_yuan shen_li_fa_yuan,
nei_rong content,
'山西省' province,
'不确定' city,
'' riqi
from sx_fy
where update_time>=@start_dt and update_time<@stop_dt
union all
-- 重庆法院
select
fa_yuan shen_li_fa_yuan,
nei_rong content,
'重庆市' province,
'重庆市' city,
kt_date riqi
from chongqing_fy
where add_time>=@start_dt and add_time<@stop_dt
union all
-- 辽宁法院
select
'' shen_li_fa_yuan,
gg_detail content,
'辽宁省' province,
'不确定' city,
'' riqi
from liaoning_fy
where add_time>=@start_dt and add_time<@stop_dt
union all
-- 云南法院
select
'' shen_li_fa_yuan,
gg_detail content,
'云南省' province,
'不确定' city,
'' riqi
from yunnan_sifa
where add_time>=@start_dt and add_time<@stop_dt
