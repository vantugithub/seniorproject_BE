package project.instagram.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
@EnableCaching
public class RedisConfig {
	
	@Autowired
    private RedisConnectionFactory redisConnectionFactory;

	@Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        
        return redisMessageListenerContainer;
    }
	
//	@Bean
//    public ExpirationListener keyExpiredListener() {
//        return new ExpirationListener(this.redisMessageListenerContainer());
//    }
}
