package jhrspring.splearn;

import jhrspring.splearn.application.required.EmailSender;
import jhrspring.splearn.domain.MemberFixture;
import jhrspring.splearn.domain.PasswordEncoder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SplearnTestConfiguration {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return MemberFixture.createPasswordEncoder();
    }
}
