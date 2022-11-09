package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.CallControl;
import com.zerody.user.vo.CallControlVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.zerody.user.mapper.CallControlMapper;
import com.zerody.user.service.CallControlService;

import java.util.concurrent.TimeUnit;

import static com.alibaba.nacos.client.utils.EnvUtil.LOGGER;

@Service
public class CallControlServiceImpl extends ServiceImpl<CallControlMapper, CallControl> implements CallControlService{

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public CallControlVo getByCompany(String companyId) {
        return null;
    }

    @Override
    public void addOrUpdate(CallControlVo param) {

    }

    @Override
    public void submitCallControl(UserVo user) {

    }

    /**
     * 判断同一个key在规定时间内访问次数是否到达了最高值
     * @author  DaBai
     * @date  2022/11/9 15:15
     * @param key   键
     * @param time  时间
     * @param count 一定时间内的访问次数
     * @return
     */
    public Boolean invokeExceededTimes(String key, int time, int count) {

        LOGGER.info("key值:{}",key);
        // 判断在redis中是否有key值
        Boolean redisKey = stringRedisTemplate.hasKey(key);
        if (redisKey) {
            // 获取key所对应的value
            Integer hasKey =Integer.parseInt((String)stringRedisTemplate.opsForValue().get(key));
            if (hasKey >= count) {
                return false;
            }
            // 对value进行加1操作
            stringRedisTemplate.opsForValue().increment(key,1);
            return true;
        }else {
            // 如果没有key值，对他进行添加到redis中
            stringRedisTemplate.opsForValue().set(key,"1",time, TimeUnit.SECONDS);
        }
        return true;
    }
}
