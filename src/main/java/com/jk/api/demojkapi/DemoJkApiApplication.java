package com.jk.api.demojkapi;

import com.jk.api.demojkapi.events.EventValidator;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoJkApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoJkApiApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}

