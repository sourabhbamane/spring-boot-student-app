package com.studentapp.student_management_boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.studentapp")
@EnableJpaRepositories(basePackages = "com.studentapp.repository")
@EntityScan(basePackages = "com.studentapp.model") //
public class StudentManagementBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementBootApplication.class, args);
	}

}



/*
* package com.studentapp.student_management_boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.studentapp")
@EnableJpaRepositories(basePackages = "com.studentapp.repository")
@EntityScan(basePackages = "com.studentapp.model")
public class StudentManagementBootApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(StudentManagementBootApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementBootApplication.class, args);
	}
}*/