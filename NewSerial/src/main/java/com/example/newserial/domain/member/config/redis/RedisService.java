package com.example.newserial.domain.member.config.redis;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    //키-값 저장
    @Transactional
    public void setValues(String key, String value){
        redisTemplate.opsForValue().set(key, value);
    }

    // 키-값을 만료시간과 함께 저장
    @Transactional
    public void setValuesWithTimeout(String key, String value, long timeout){
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    //키를 이용해 값 구하기
    public String getValues(String key){
        return redisTemplate.opsForValue().get(key);
    }

    //키를 이용해 값 삭제
    @Transactional
    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
}
