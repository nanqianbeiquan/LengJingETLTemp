-- set @today=curdate();
-- set @yesterday=date_sub(curdate(), INTERVAL 1 DAY);
-- set @tomorrow=date_sub(curdate(), INTERVAL -1 DAY);
-- set @start_dt=@yesterday;
-- set @stop_dt=@today;
-- 北京
select 
`bjcourt`.`fa_yuan` fa_yuan_ming_cheng
,`bjcourt`.`gong_gao_nei_rong` content
, '' an_hao
,'' riqi
,'' an_you
,'' shen_pan_zhang
,'' fa_ting
,'北京' area
,'北京市' province
, '北京市' city
from `bjcourt` 
where add_time >=@start_dt and add_time<@stop_dt and gong_gao_nei_rong!='' and gong_gao_nei_rong not like '%现取消开庭。'
union all
-- 河南省法院
select 
fayuan fa_yuan_ming_cheng,
ungonggao content,
'' an_hao,
ktrq riqi,
'' an_you,
'' shen_pan_zhang,
'' fa_ting,
'河南' area,
'河南省' province,
'城市' city
from hn_fayuansusong4th
where add_time >=@start_dt and add_time<@stop_dt
union all
-- 安徽省法院
select 
fa_yuan fa_yuan_ming_cheng,
nei_rong content,
an_hao,
'' riqi,
'' an_you,
'' shen_pan_zhang,
'' fa_ting,
'安徽' area,
'安徽省' province,
'城市' city
from ah_susong2nd
where updatetime >=@start_dt and updatetime<@stop_dt
union all
-- 广州法院
select 
fa_yuan fa_yuan_ming_cheng,
xq content,
an_hao,
kai_ting_ri_qi riqi,
an_you,
shen_pan_zhang,
fa_ting,
'广州' area,
'广东省' province,
'广州市' city
from guang_zhou
where add_date >=@start_dt and add_date<@stop_dt
