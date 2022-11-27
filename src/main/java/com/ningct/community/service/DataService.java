package com.ningct.community.service;

import com.ningct.community.util.RedisKeyUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class DataService {
    @Resource
    private RedisTemplate redisTemplate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    public void recordUV(String ip){
        String key = RedisKeyUtil.getUVKey(dateFormat.format(new Date()));
        redisTemplate.opsForHyperLogLog().add(key,ip);
    }
    public long caculateUV(Date startDate, Date endDate){
        if(startDate == null || endDate == null){
            throw new IllegalArgumentException("日期不能为空！");
        }
        List<String> keys = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        while(!calendar.getTime().after(endDate)){
            String uvKey = RedisKeyUtil.getUVKey(dateFormat.format(calendar.getTime()));
            keys.add(uvKey);
            calendar.add(Calendar.DATE,1);
        }
        String key = RedisKeyUtil.getUVKey(dateFormat.format(startDate), dateFormat.format(endDate));
        redisTemplate.opsForHyperLogLog().union(key,keys.toArray());
        return redisTemplate.opsForHyperLogLog().size(key);
    }
    public void recordDAU(int id){
        String key = RedisKeyUtil.getDAUKey(dateFormat.format(new Date()));
        redisTemplate.opsForHyperLogLog().add(key,id);
    }
    public long caculateDau(Date startDate, Date endDate){
        if(startDate == null || endDate == null){
            throw new IllegalArgumentException("日期不能为空！");
        }
        List<String> keys = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        while(!calendar.getTime().after(endDate)){
            String dauKey = RedisKeyUtil.getDAUKey(dateFormat.format(calendar.getTime()));
            keys.add(dauKey);
            calendar.add(Calendar.DATE,1);
        }
        String key = RedisKeyUtil.getDAUKey(dateFormat.format(startDate), dateFormat.format(endDate));
        redisTemplate.opsForHyperLogLog().union(key,keys.toArray());
        return redisTemplate.opsForHyperLogLog().size(key);
    }
}
