-- set @today=curdate();
-- set @yesterday=date_sub(curdate(),INTERVAL 1 DAY);
-- set @tomorrow=date_sub(curdate(),INTERVAL -1 DAY);
-- set @start_dt=@yesterday;
-- set @stop_dt=@tomorrow;

-- 无锡
select
an_hao,
an_jian_ming_cheng,
shi_jian kai_ting_ri_qi,
fa_ting shen_li_fa_ting,
shen_pan_zhang,
'江苏省' province,
'无锡市' city
from wxfy
where add_time>=@start_dt and add_time<@stop_dt
union all
-- 江苏泰州
select
an_hao,
an_jian_ming_chen an_jian_ming_cheng,
ri_qi kai_ting_ri_qi,
fa_ting shen_li_fa_ting,
zhu_shen shen_pan_zhang,
'江苏省' province,
'泰州市' city
from js_taizhou
where updatetime>=@start_dt and updatetime<@stop_dt
union all
-- 江苏常州
select
an_hao,
an_you an_jian_ming_cheng,
kai_ting_shi_jian kai_ting_ri_qi,
fa_ting shen_li_fa_ting,
zhu_shen_fa_guan shen_pan_zhang,
'江苏省' province,
'常州市' city
from czfy
where add_time>=@start_dt and add_time<@stop_dt
union all
-- 南通法院
select
'' an_hao,
an_jian_ming_cheng,
kai_ting_shi_jian kai_ting_ri_qi,
fa_ting shen_li_fa_ting,
shen_pan_zhang,
'江苏省' province,
'南通市' city
from ntfy
where add_time>=@start_dt and add_time<@stop_dt
union all
-- 连云港法院
select
an_hao,
an_jian_ming_cheng,
kai_ting_shi_jian kai_ting_ri_qi,
kai_ting_di_dian shen_li_fa_ting,
'' shen_pan_zhang,
'江苏省' province,
'连云港市' city
from lygfy
where add_time>=@start_dt and add_time<@stop_dt
union all
-- 厦门法院
select
an_hao,
an_jian_shuo_ming an_jian_ming_cheng,
kai_ting_shi_jian kai_ting_ri_qi,
kai_ting_di_dian shen_li_fa_ting,
fa_guan shen_pan_zhang,
'福建省' province,
'厦门市' city
from xiamenfy
where add_time>=@start_dt and add_time<@stop_dt
union all
-- 福建泉州法院
select
an_hao,
ming_cheng an_jian_ming_cheng,
sj_dd kai_ting_ri_qi,
sj_dd shen_li_fa_ting,
'' shen_pan_zhang,
'福建省' province,
'泉州市' city
from fj_quanzhou
where updatetime>=@start_dt and updatetime<@stop_dt
union all
-- 亳州市中级人民法院
select
'' an_hao,
other an_jian_ming_cheng,
gong_gao kai_ting_ri_qi,
'' shen_li_fa_ting,
'' shen_pan_zhang,
'安徽省' province,
'亳州市' city
from ah_bozhoufy
where add_time>=@start_dt and add_time<@stop_dt and locate('√',other)=0
union all
-- 广西省南宁市
select
an_hao,
nei_rong an_jian_ming_cheng,
shi_jian kai_ting_ri_qi,
'' shen_li_fa_ting,
'' shen_pan_zhang,
'广西省' province,
'南宁市' city
from gx_nanning2nd
where updatetime>=@start_dt and updatetime<@stop_dt
union all
-- 广西省玉林市
select
an_hao,
nei_rong an_jian_ming_cheng,
kai_ting_shi_jian kai_ting_ri_qi,
kai_ting_di_dian shen_li_fa_ting,
'' shen_pan_zhang,
'广西省' province,
'玉林市' city
from gx_yl_fy
where updatetime>=@start_dt and updatetime<@stop_dt

