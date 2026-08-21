package com.petcare.petcare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//@Mapper를 인터페이스마다 붙이기 번거로우니
//@MapperSacn("패키지") -> 패키지 밑의 인터페이스는 @Mapper를 안붙여도 자동으로 스캔
// @MapperScan("com.petcare.petcare")
@SpringBootApplication
public class PetcareApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PetcareApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(PetcareApplication.class, args);
    }
}
