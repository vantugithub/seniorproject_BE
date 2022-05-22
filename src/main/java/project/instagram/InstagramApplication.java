package project.instagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableCaching
public class InstagramApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstagramApplication.class, args);
	}
	
	@Bean
	public TaskScheduler taskScheduler() {
	  final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
	  scheduler.setPoolSize(3);
	  return scheduler;
	}

}
