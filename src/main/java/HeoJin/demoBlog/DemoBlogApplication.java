package HeoJin.demoBlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy // aop 로깅 관련 설정 추가
public class DemoBlogApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoBlogApplication.class, args);
	}

}
