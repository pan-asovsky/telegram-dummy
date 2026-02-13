package ru.festtur.telegramdummy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, String> template(final RedisConnectionFactory factory) {
        final RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        var serializer = new StringRedisSerializer();
        template.setKeySerializer(serializer);
        template.setHashKeySerializer(serializer);
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public DefaultRedisScript<Long> upsertSession() {
        final DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("/redis/upsert_session.lua"));
        script.setResultType(Long.class);
        return script;
    }

}

