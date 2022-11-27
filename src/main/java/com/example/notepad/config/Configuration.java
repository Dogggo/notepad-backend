package com.example.notepad.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
@EnableAutoConfiguration
public class Configuration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
