package de.niklaseckert.epro_project.config;

import de.niklaseckert.epro_project.service.OkrBasicAuthenticationEntryPoint;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final OkrBasicAuthenticationEntryPoint authenticationEntryPoint;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/co/**").hasAnyRole("CO_OKR_ADMIN", "READ_ONLY")
                .antMatchers(HttpMethod.POST, "/co/**").hasRole("CO_OKR_ADMIN")
                .antMatchers(HttpMethod.PUT, "/co/**").hasRole("CO_OKR_ADMIN")
                .antMatchers(HttpMethod.PATCH, "/co/**").hasRole("CO_OKR_ADMIN")
                .antMatchers(HttpMethod.DELETE, "/co/**").hasRole("CO_OKR_ADMIN")
                .antMatchers(HttpMethod.GET, "/bu/**").hasAnyRole("BUO_OKR_ADMIN", "READ_ONLY")
                .antMatchers(HttpMethod.POST, "/bu/**").hasRole("BUO_OKR_ADMIN")
                .antMatchers(HttpMethod.PUT, "/bu/**").hasRole("BUO_OKR_ADMIN")
                .antMatchers(HttpMethod.PATCH, "/bu/**").hasRole("BUO_OKR_ADMIN")
                .antMatchers(HttpMethod.DELETE, "/bu/**").hasRole("BUO_OKR_ADMIN")
                //.antMatchers(HttpMethod.GET,"/co/**","/bu/**").hasRole("READ_ONLY")
                .and()
                .httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .csrf().disable();
    }

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
