package com.example.event5.demo_event5.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain customSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf((csrf) -> csrf.disable()).authorizeHttpRequests(requests->{

            requests.requestMatchers("/login").permitAll();
            requests.requestMatchers("/logout").authenticated();
            requests.requestMatchers("/display/AdminView").hasRole("ADMIN");
            requests.requestMatchers("/upload").hasRole("ADMIN");
            requests.requestMatchers("/editEvent/**").hasRole("ADMIN");
            requests.requestMatchers("/edit/{id}/updateContent/{contentId}").hasRole("ADMIN");
            requests.requestMatchers("/edit/{id}/deleteContent/{contentId}").hasRole("ADMIN");
            requests.requestMatchers("event/{eventId}/delete").hasRole("ADMIN");
            requests.requestMatchers("/edit/{id}/editEvent").hasRole("ADMIN");
            requests.requestMatchers("edit/{id}/deleteFile/{fileId}").hasRole("ADMIN");
            requests.requestMatchers("edit/{id}/deleteUrl/{urlId}").hasRole("ADMIN");
            requests.requestMatchers("/downloadfile").authenticated();
            requests.requestMatchers("/image").authenticated();
            requests.requestMatchers("/viewfile").authenticated();
            requests.requestMatchers("/search/{yearOption}").authenticated();
            requests.requestMatchers("/search").authenticated();
            requests.requestMatchers("/searchContacts").hasRole("ADMIN");
            requests.requestMatchers("/searchContacts1").hasRole("ADMIN");
            requests.requestMatchers("/events").authenticated();
            requests.requestMatchers("/event/{id}/view").authenticated();
            requests.requestMatchers("/dashboard").authenticated();
            requests.requestMatchers("/display1/{yearOption}").authenticated();
            requests.requestMatchers("/contact").hasRole("STUDENT");
            requests.requestMatchers("/contactMessage").hasRole("STUDENT");
            requests.requestMatchers("/contacts").hasRole("ADMIN");
            requests.requestMatchers("/contacts1").hasRole("ADMIN");
            requests.requestMatchers("/contact1/{id}/view").hasRole("ADMIN");
            requests.requestMatchers("/contact1/{id}/email").hasRole("ADMIN");
            requests.requestMatchers("/contact1/{id}/delete").hasRole("ADMIN");
            requests.requestMatchers("/contact1/{id}/close").hasRole("ADMIN");
            requests.requestMatchers("/contact1/{id}/approve").hasRole("ADMIN");
            requests.requestMatchers("/contact1/{id}/disapprove").hasRole("ADMIN");
            requests.requestMatchers("/submitEmail/{id}").hasRole("ADMIN");
            requests.requestMatchers("/viewfiles").hasRole("ADMIN");
            requests.requestMatchers("/images").hasRole("ADMIN");
            requests.requestMatchers("/downloadfiles").hasRole("ADMIN");
            requests.requestMatchers("/profiles/{id}/view").hasRole("ADMIN");
            requests.requestMatchers("/searchProfile").hasRole("ADMIN");
            requests.requestMatchers("/searchProfile1").hasRole("ADMIN");
            requests.requestMatchers("/profile1").hasRole("STUDENT");
            requests.requestMatchers("/profile2").hasRole("ADMIN");
            requests.requestMatchers("/3rdYears").hasRole("ADMIN");
            requests.requestMatchers("/4thYears").hasRole("ADMIN");
            requests.requestMatchers("/viewProfile").hasRole("STUDENT");
            requests.requestMatchers("/profile").hasRole("STUDENT");
            requests.requestMatchers("/profileUpload").hasRole("STUDENT");
            requests.requestMatchers("/editProfile/{id}/deleteFile/{fileId}").hasRole("STUDENT");
            requests.requestMatchers("/editProfile1/{id}/deleteFile/{fileId}").hasRole("STUDENT");
            requests.requestMatchers("/editLink/{id}/deleteUrl/{urlId}").hasRole("STUDENT");
            requests.requestMatchers("/editProfileResume/{id}/deleteResume/{fileId}").hasRole("STUDENT");
            requests.requestMatchers("/editProfile/{id}/ProfileUpload1").hasRole("STUDENT");
            requests.requestMatchers("/editProfile").hasRole("STUDENT");
            requests.requestMatchers("/viewfiler").authenticated();
            requests.requestMatchers("/imager").authenticated();
            requests.requestMatchers("/downloadfiler").authenticated();

            requests.requestMatchers("/viewfilec").authenticated();
            requests.requestMatchers("/imagec").authenticated();
            requests.requestMatchers("/downloadfilec").authenticated();

            requests.requestMatchers("/viewfileo").authenticated();
            requests.requestMatchers("/imageo").authenticated();
            requests.requestMatchers("/downloadfileo").authenticated();
            requests.requestMatchers("/years").hasRole("ADMIN");















                }).formLogin(loginConfigurer -> loginConfigurer.loginPage("/login")
                        .defaultSuccessUrl("/dashboard").failureUrl("/login?error=true").permitAll())
                .logout(logoutConfigurer -> logoutConfigurer.logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true).permitAll())
                .httpBasic(Customizer.withDefaults());
        return httpSecurity.build();

    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
