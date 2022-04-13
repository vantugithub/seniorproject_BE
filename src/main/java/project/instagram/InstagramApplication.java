package project.instagram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import project.instagram.repository.ClientRepository;

@SpringBootApplication
@EnableJpaAuditing
public class InstagramApplication implements CommandLineRunner {
	
	@Autowired
	private ClientRepository clientRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(InstagramApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}

}
