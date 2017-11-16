-- set @today=curdate();
-- set @yesterday=date_sub(curdate(), INTERVAL 1 DAY);
-- set @tomorrow=date_sub(curdate(), INTERVAL -1 DAY);
-- set @start_dt=@yesterday;
-- set @stop_dt=@today;


select 
kai_ting_ri_qi,
an_you,
an_hao,
fa_yuan fa_yuan_ming_cheng,
fa_ting shen_li_fa_ting,
shen_pan_zhang zhu_shen_fa_guan,
cheng_ban_bu_men cheng_ban_ting,
concat('原告：',yuan_gao,'；被告：',bei_gao) dang_shi_ren,
'上海市' province,
'上海市' city
from `hshfy`
where add_time >=@start_dt and add_time<@stop_dt
union all
-- 广东
select 
kai_ting_shi_jian kai_ting_ri_qi,
'' an_you,
an_hao,
fa_yuan fa_yuan_ming_cheng,
kai_ting_di_dian shen_li_fa_ting,
zhu_shen_fa_guan,
'' cheng_ban_ting,
dang_shi_ren,
'广东省' province,
'不确定' city
from `gdcourts`
where add_time >=@start_dt and add_time<@stop_dt
union all
-- 浙江
select 
kai_ting_ri_qi,
an_you,
an_hao,
fa_yuan fa_yuan_ming_cheng,
fa_ting shen_li_fa_ting,
shen_pan_zhang zhu_shen_fa_guan,
cheng_ban_bu_men cheng_ban_ting,
concat(yuan_gao,'；',bei_gao) dang_shi_ren,
'浙江省' province,
'不确定' city
from `zjsfgkw`
where add_time >=@start_dt and add_time<@stop_dt
union all
-- 江苏-南京
select 
ri_qi kai_ting_ri_qi,
an_you,
an_hao,
'南京市中级人民法院' fa_yuan_ming_cheng,
fa_ting shen_li_fa_ting,
zhu_shen zhu_shen_fa_guan,
'' cheng_ban_ting,
dang_shi_ren,
'江苏省' province,
'南京市' city
from `js_nanjing`
where updatetime>=@start_dt and updatetime<@stop_dt
union all
-- 江苏-扬州
select
shi_jian kai_ting_ri_qi,
an_you,
an_hao,
'扬州市中级人民法院' fa_yuan_ming_cheng,
fa_ting shen_li_fa_ting,
zhu_shen zhu_shen_fa_guan,
'' cheng_ban_ting,
concat('原告：',yuan_gao,'；被告',bei_gao) dang_shi_ren,
'江苏省' province,
'扬州市' city
from `js_yangzhou`
where updatetime>=@start_dt and updatetime<@stop_dt
union all
-- 江苏-苏州
select 
ri_qi kai_ting_ri_qi,
an_you,
an_hao,
'苏州市中级人民法院' fa_yuan_ming_cheng,
di_dian shen_li_fa_ting,
shen_pan_zhang zhu_shen_fa_guan,
'' cheng_ban_ting,
dang_shi_ren,
'江苏省' province,
'苏州市' city
from `szfy`
where add_time >=@start_dt and add_time<@stop_dt
-- 山东法院
union all
select 
kai_ting_ri_qi,
an_you,
'' an_hao,
fa_yuan fa_yuan_ming_cheng,
fa_ting shen_li_fa_ting,
shen_pan_zhang zhu_shen_fa_guan,
'' cheng_ban_ting,
concat('原告：',yuan_gao,'；被告：',bei_gao) dang_shi_ren,
'山东省' province,
'不确定' city
from `sd_fy`
where add_time >=@start_dt and add_time<@stop_dt
-- 佛山市中级人民法院
union all
select 
kai_ting_ri_qi,
an_you,
an_hao,
'佛山市中级人民法院' fa_yuan_ming_cheng,
fa_ting shen_li_fa_ting,
'' zhu_shen_fa_guan,
'' cheng_ban_ting,
dang_shi_ren,
'广东省' province,
'佛山市' city
from `gd_foshan`
where updatetime >=@start_dt and updatetime<@stop_dt
-- 阳江市中级人民法院
union all
select 
li_an kai_ting_ri_qi,
an_you,
an_hao,
'阳江市中级人民法院' fa_yuan_ming_cheng,
'' shen_li_fa_ting,
zhu_shen zhu_shen_fa_guan,
'' cheng_ban_ting,
replace(dang_shi_ren,' ',','),
'广东省' province,
'阳江市' city
from `gd_yangjiang`
where updatetime >=@start_dt and updatetime<@stop_dt
-- 肇庆市中级人民法院
union all
select 
ri_qi kai_ting_ri_qi,
an_you,
an_hao,
'肇庆市中级人民法院' fa_yuan_ming_cheng,
fa_ting shen_li_fa_ting,
zhu_shen zhu_shen_fa_guan,
'' cheng_ban_ting,
dang_shi_ren,
'广东省' province,
'肇庆市' city
from `gd_zhaoqing`
where updatetime >=@start_dt and updatetime<@stop_dt
-- 广州海事法院
union all
select 
kai_ting_shi_jian kai_ting_ri_qi,
an_you,
an_hao,
'广州海事法院' fa_yuan_ming_cheng,
kai_ting_di_dian shen_li_fa_ting,
zhu_shen_fa_guan,
'' cheng_ban_ting,
concat('原告：',yuan_gao,'；被告：',bei_gao) dang_shi_ren,
'广东省' province,
'广州市' city
from `gzhsfy`
where add_time >=@start_dt and add_time<@stop_dt
-- 宁波市中级人民法院
union all
select 
kai_ting_ri_qi,
an_you,
an_hao,
'宁波市中级人民法院' fa_yuan_ming_cheng,
kai_ting_di_dian shen_li_fa_ting,
zhu_shen_fa_guan,
'' cheng_ban_ting,
dang_shi_ren,
'浙江省' province,
'宁波市' city
from `ningbofy`
where add_time >=@start_dt and add_time<@stop_dt
-- 韶关市中级人民法院
union all
select 
kai_ting_shi_jian kai_ting_ri_qi,
an_you,
an_hao,
'韶关市中级人民法院' fa_yuan_ming_cheng,
kai_ting_di_dian shen_li_fa_ting,
'' zhu_shen_fa_guan,
'' cheng_ban_ting,
dang_shi_ren,
'广东省' province,
'韶关市' city
from `shaoguanfy`
where add_time >=@start_dt and add_time<@stop_dt
-- 深圳市中级人民法院
union all
select 
kai_ting_ri_qi,
an_you,
an_hao,
'深圳市中级人民法院' fa_yuan_ming_cheng,
shen_pan_ting shen_li_fa_ting,
'' zhu_shen_fa_guan,
'' cheng_ban_ting,
dang_shi_ren,
'广东省' province,
'深圳市' city
from `szcourt`
where add_time >=@start_dt and add_time<@stop_dt
-- 中山市中级人民法院
union all
select 
kai_ting_ri_qi,
an_you,
an_hao,
'中山市中级人民法院' fa_yuan_ming_cheng,
kai_ting_di_dian shen_li_fa_ting,
'' zhu_shen_fa_guan,
'' cheng_ban_ting,
dang_shi_ren,
'广东省' province,
'中山市' city
from `zhongshanfy`
where add_time >=@start_dt and add_time<@stop_dt
-- 嘉兴市中级人民法院
union all
select 
shi_jian kai_ting_ri_qi,
an_you,
an_hao,
'嘉兴市中级人民法院' fa_yuan_ming_cheng,
fa_ting shen_li_fa_ting,
'' zhu_shen_fa_guan,
cheng_ban_ren cheng_ban_ting,
dang_shi_ren,
'浙江省' province,
'嘉兴市' city
from `zj_jiaxing`
where updatetime >=@start_dt and updatetime<@stop_dt
-- 绍兴市中级人民法院
union all
select 
kai_ting_ri_qi,
an_you,
an_hao,
'绍兴市中级人民法院' fa_yuan_ming_cheng,
fa_ting shen_li_fa_ting,
shen_pan_zhang zhu_shen_fa_guan,
'' cheng_ban_ting,
dang_shi_ren,
'浙江省' province,
'绍兴市' city
from `zj_shaoxing`
where updatetime >=@start_dt and updatetime<@stop_dt
-- 合肥市中级人民法院
union all
select 
others kai_ting_ri_qi,
an_you,
an_hao,
'合肥市中级人民法院' fa_yuan_ming_cheng,
'' shen_li_fa_ting,
'' zhu_shen_fa_guan,
'' cheng_ban_ting,
dang_shi_ren,
'安徽省' province,
'合肥市' city
from `ah_hefei2nd`
where add_time >=@start_dt and add_time<@stop_dt
-- 四川省高级人民法院
union all
select 
kai_ting_shi_jian kai_ting_ri_qi,
'' an_you,
an_hao,
fa_yuan fa_yuan_ming_cheng,
kai_ting_di_dian shen_li_fa_ting,
'' zhu_shen_fa_guan,
'' cheng_ban_ting,
dang_shi_ren,
'四川省' province,
'不确定' city
from `sc_gaoyuan2nd`
where add_time >=@start_dt and add_time<@stop_dt
-- 广西省桂林市
union all
select
ri_qi kai_ting_ri_qi,
an_you,
an_hao,
fa_yuan fa_yuan_ming_cheng,
fa_ting shen_li_fa_ting,
'' zhu_shen_fa_guan,
'' cheng_ban_ting,
concat('原告：',yuan_gao,'；被告：',bei_gao) dang_shi_ren,
'广西省' province,
'桂林市' city
from gx_guilin2nd
where updatetime >=@start_dt and updatetime<@stop_dt
-- 广西省梧州市
union all
select
ri_qi kai_ting_ri_qi,
an_you,
an_hao,
fa_yuan fa_yuan_ming_cheng,
fa_ting shen_li_fa_ting,
'' zhu_shen_fa_guan,
'' cheng_ban_ting,
concat('原告：',yuan_gao,'；被告：',bei_gao) dang_shi_ren,
'广西省' province,
'梧州市' city
from gx_wuzhou2nd
where updatetime >=@start_dt and updatetime<@stop_dt
-- 广西省钦州市
union all
select
ri_qi kai_ting_ri_qi,
an_you,
an_hao,
fa_yuan fa_yuan_ming_cheng,
fa_ting shen_li_fa_ting,
'' zhu_shen_fa_guan,
'' cheng_ban_ting,
concat('原告：',yuan_gao,'；被告：',bei_gao) dang_shi_ren,
'广西省' province,
'钦州市' city
from gx_qinzhou2nd
where updatetime >=@start_dt and updatetime<@stop_dt
-- 广西省贵港市
union all
select
ri_qi kai_ting_ri_qi,
an_you,
an_hao,
fa_yuan fa_yuan_ming_cheng,
fa_ting shen_li_fa_ting,
'' zhu_shen_fa_guan,
'' cheng_ban_ting,
concat('原告：',yuan_gao,'；被告：',bei_gao) dang_shi_ren,
'广西省' province,
'贵港市' city
from gx_guigang2nd
where updatetime >=@start_dt and updatetime<@stop_dt
-- 广西省高级人民法院
union all
select
ri_qi kai_ting_ri_qi,
an_you,
an_hao,
fa_yuan fa_yuan_ming_cheng,
fa_ting shen_li_fa_ting,
'' zhu_shen_fa_guan,
'' cheng_ban_ting,
concat('原告：',yuan_gao,'；被告：',bei_gao) dang_shi_ren,
'广西省' province,
if(shi_qu='广西高级法院','南宁市',shi_qu) city
from gx_gaoyuan2nd
where updatetime >=@start_dt and updatetime<@stop_dt
-- 江西省上饶市
union all
select
shi_jian kai_ting_ri_qi,
an_you,
an_hao,
'' fa_yuan_ming_cheng,
di_dian shen_li_fa_ting,
ceng_ban_ren zhu_shen_fa_guan,
'' cheng_ban_ting,
dang_shi_ren,
'江西省' province,
'上饶市' city
from jx_shangrao2end
where updatetime >=@start_dt and updatetime<@stop_dt
-- 江西省九江市
union all
select
kai_ting_shi_jian kai_ting_ri_qi,
an_you,
an_hao,
fa_yuan fa_yuan_ming_cheng,
'' shen_li_fa_ting,
'' zhu_shen_fa_guan,
'' cheng_ban_ting,
dang_shi_ren,
'江西省' province,
'九江市' city
from jx_jiujiang3rd
where add_time >=@start_dt and add_time<@stop_dt
-- 江西省鹰潭市中级人民法院
union all
select
kai_ting_shi_jian kai_ting_ri_qi,
an_you,
an_hao,
fa_yuan fa_yuan_ming_cheng,
'' shen_li_fa_ting,
'' zhu_shen_fa_guan,
'' ceng_ban_ting,
dang_shi_ren,
'江西省' province,
'鹰潭市' city
from jx_yingtan3rd
where add_time >=@start_dt and add_time<@stop_dt
-- 湖北法院
union all
select
kai_ting_ri_qi,
an_you,
'' an_hao,
fa_yuan fa_yuan_ming_cheng,
fa_ting shen_li_fa_ting,
'' zhu_shen_fa_guan,
'' cheng_ban_ting,
dang_shi_ren,
'湖北省' province,
'不确定' city
from hu_bei
where add_date>=@start_dt and add_date<@stop_dt
-- 江西法院
union all
select
kai_ting_ri_qi,
an_you,
an_hao,
fa_yuan fa_yuan_ming_cheng,
fa_ting shen_li_fa_ting,
'' zhu_shen_fa_guan,
'' cheng_ban_ting,
dang_shi_ren,
'江西省' province,
'不确定' city
from jiang_xi
where add_date>=@start_dt and add_date<@stop_dt
-- 太仓法院
union all
select
shi_jian kai_ting_ri_qi,
replace(an_you,'案　由：','') an_you,
replace(an_hao,'案　号：','') an_hao,
'太仓市人民法院' fa_yuan_ming_cheng,
replace(fa_ting,'地　点：','') shen_li_fa_ting,
'' zhu_shen_fa_guan,
'' cheng_ban_ting,
nei_rong dang_shi_ren,
'江苏省' province,
'苏州市' city
from js_taicang
where updatetime>=@start_dt and updatetime<@stop_dt
-- 江阴法院
union all
select
ri_qi kai_ting_ri_qi,
an_you,
an_hao,
'江阴市人民法院' fa_yuan_ming_cheng,
'' shen_li_fa_ting,
'' zhu_shen_fa_guan,
'' cheng_ban_ting,
dang_shi_ren,
'江苏省' province,
'无锡市' city
from js_jy_fy
where add_time>=@start_dt and add_time<@stop_dt