package com.developers.member.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing을 config 파일에서 수행하도록 분리
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
