-- set @today=curdate();
-- set @yesterday=date_sub(curdate(), INTERVAL 1 DAY);
-- set @tomorrow=date_sub(curdate(), INTERVAL -1 DAY);
-- set @start_dt=@yesterday;
-- set @stop_dt=@today;

-- 徐州法院
select
gg_detail content,
'江苏省' province,
'徐州市' city
from xzfy
where add_time >=@start_dt and add_time<@stop_dt