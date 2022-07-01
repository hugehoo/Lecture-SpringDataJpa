package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	@Bean // getCreatedBy/LastModifiedBy 사용하기 위해 필요한 빈 등록
	public AuditorAware<String> auditorAware() {
		return () -> Optional.of(UUID.randomUUID().toString());
		// 현재는 랜덤의 값을 꺼냈는데, 현업에서는 세션값을 넣어 사용하면 사용자를 식별할 수 있다.
	}
}
