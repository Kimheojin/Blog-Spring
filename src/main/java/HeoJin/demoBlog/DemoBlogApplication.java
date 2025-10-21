package HeoJin.demoBlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAspectJAutoProxy // aop 로깅 관련 설정 추가
@EnableScheduling
public class DemoBlogApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoBlogApplication.class, args);
	}

}
