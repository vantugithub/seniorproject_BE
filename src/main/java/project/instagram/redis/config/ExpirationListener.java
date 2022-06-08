package project.instagram.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import project.instagram.common.enums.constants.JobConstants;
import project.instagram.security.SecurityAuditorAware;

@Component
public class ExpirationListener extends KeyExpirationEventMessageListener {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private SecurityAuditorAware securityAuditorAware;

	@Autowired
	private StringRedisTemplate redisTemplate;

	public ExpirationListener(RedisMessageListenerContainer listenerContainer) {
		super(listenerContainer);
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {

		if (!securityAuditorAware.getCurrentAuditor().isEmpty()) {
			String emailStaff = securityAuditorAware.getCurrentAuditor().get();
			project.instagram.socket.config.Message messageToStaff = new project.instagram.socket.config.Message();
			messageToStaff.setMessage("Expired time for request");
			messageToStaff.setTitle("Expired time");
			simpMessagingTemplate.convertAndSendToUser(emailStaff, "/private", messageToStaff);
		}
		System.out.println("Expired key:" + message.toString());
		redisTemplate.opsForList().leftPush(JobConstants.PENDING_REQUESTS, message.toString());
	}

}
