package com.example.common.infrastructure.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import com.example.common.domain.util.AppUtil;

import java.util.TimeZone;

@Configuration
public class LocaleConfig {
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(AppUtil.APP_TIMEZONE));
    }
}
