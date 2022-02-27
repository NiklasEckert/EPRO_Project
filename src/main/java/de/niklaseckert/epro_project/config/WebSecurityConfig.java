package de.niklaseckert.epro_project.config;

import de.niklaseckert.epro_project.service.OkrBasicAuthenticationEntryPoint;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Class which restricts access based on the {@link de.niklaseckert.epro_project.model.Role role} of the {@link de.niklaseckert.epro_project.model.User user}.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /** Checks if a {@link de.niklaseckert.epro_project.model.User user} is authenticated.  */
    private final OkrBasicAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    /**
     * Configures the access for the mappings.
     *
     * @param http allows configuring web based security for specific http requests.
     * @throws Exception if something goes wrong.
     */
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
                .antMatchers("/dashboard").hasRole("READ_ONLY")
                .and()
                .httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .formLogin()
                .and()
                .csrf().disable();
    }

    /**
     * Password encoder which is used to encrypt the passwords.
     *
     * @return a password encoder.
     */
    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
