package com.zyhao.openec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;

/**
 * The {@link OpenecApplication} is a cloud-native Spring Boot application that manages
 * a bounded context for @{link Customer}, @{link Account}, @{link CreditCard}, and @{link Address}
 *
 * @author Kenny Bastani
 * @author Josh Long
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
@EnableEurekaClient
@EnableResourceServer
@EnableOAuth2Client  // 有需要访问其他需要认证的接口时使用
@EnableHystrix
public class OpenecApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpenecApplication.class, args);
    }

   
    /**
     * 与其他需要认证的服务交互时使用，不用可以不加载这个内容。
     * @param resource
     * @param context
     * @return
     */
    @LoadBalanced
    @Bean
    public OAuth2RestTemplate loadBalancedOauth2RestTemplate(
            OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
        return new OAuth2RestTemplate(resource, context);
    }

    @LoadBalanced
    @Bean(name = "normalRestTemplate")
    public RestTemplate loadBalancedRestTemplate() {
        return new RestTemplate();
    }


}
