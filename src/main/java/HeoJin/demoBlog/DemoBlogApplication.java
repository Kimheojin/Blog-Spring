package HeoJin.demoBlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("local")
public class DemoBlogApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoBlogApplication.class, args);
	}

}
