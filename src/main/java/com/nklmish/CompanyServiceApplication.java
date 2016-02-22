package com.nklmish;

import com.nklmish.boundary.BoundaryAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;

import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;

@SpringBootApplication
@EnableSpringDataWebSupport
@EnableHypermediaSupport(type = { HAL })
public class CompanyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompanyServiceApplication.class, args);
	}

	@Bean
	public BoundaryAspect loggingAspect() {
		return new BoundaryAspect();
	}
}
