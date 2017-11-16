-- set @today=curdate();
-- set @yesterday=date_sub(curdate(), INTERVAL 1 DAY);
-- set @tomorrow=date_sub(curdate(), INTERVAL -1 DAY);
-- set @start_dt=@yesterday;
-- set @stop_dt=@today;


select 
gong_gao riqi,
'' an_you,
'' an_hao,
'' fa_yuan_ming_cheng,
'' shen_li_fa_ting,
'' zhu_shen_fa_guan,
'' cheng_ban_ting,
gg_detail content,
'四川省' province,
'阿坝藏族羌族自治州' city
from `sc_abaqiangzang2nd`
where add_time >=@start_dt and add_time<@stop_dt
union all
select 
'' riqi,
'' an_you,
'' an_hao,
'' fa_yuan_ming_cheng,
'' shen_li_fa_ting,
'' zhu_shen_fa_guan,
'' cheng_ban_ting,
gg_detail content,
'四川省' province,
'成都市' city
from `sc_chengdu2nd`
where add_time >=@start_dt and add_time<@stop_dt
union all
-- 江西省赣州市中级人民法院
select
'' riqi,
'' an_you,
'' an_hao,
'' fa_yuan_ming_cheng,
'' shen_li_fa_ting,
'' zhu_shen_fa_guan,
'' cheng_ban_ting,
gg_content content,
'江西省' province,
'赣州市' city
from jx_ganzhou3rd
where add_time>=@start_dt and add_time<@stop_dt
union all
-- 马鞍山市中级人民法院
select
'' riqi,
'' an_you,
'' an_hao,
'马鞍山市中级人民法院' fa_yuan_ming_cheng,
'' shen_li_fa_ting,
'' zhu_shen_fa_guan,
'' cheng_ban_ting,
nei_rong content,
'安徽省' province,
'马鞍山市' city
from ah_maanshan2nd
where updatetime>=@start_dt and updatetime<@stop_dt
union all
-- 淮南市中级人民法院
select
'' riqi,
'' an_you,
'' an_hao,
'淮南市中级人民法院' fa_yuan_ming_cheng,
'' shen_li_fa_ting,
'' zhu_shen_fa_guan,
'' cheng_ban_ting,
neirong content,
'安徽省' province,
'淮南市' city
from ah_huainan2nd
where updatetime>=@start_dt and updatetime<@stop_dt