package com.grad.project.nc.configuration;

import com.grad.project.nc.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint());

        http
            .authorizeRequests()
                .antMatchers("/*", "/css/*", "/js/*", "/api/user/**").permitAll()
                .antMatchers("/admin/**", "/api/admin/**", "/api/csr/users/**").hasRole("ADMIN")
                .antMatchers("/csr/**", "/api/csr/**").hasRole("CSR")
                .antMatchers("/client/**", "/api/client/**").hasRole("CLIENT")
                .antMatchers("/pmg/**", "/api/pmg/**").hasRole("PMG")
                .antMatchers("/profile/**", "/api/profile/**").hasAnyRole("ADMIN", "CSR", "CLIENT", "PMG")
            .and()
                .formLogin()
                    .loginPage("/login")
                    .usernameParameter("email")
                    .successForwardUrl("/login/success")
                    .failureForwardUrl("/login/failed")
            .and()
                .logout()
                    .logoutSuccessUrl("/login/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
    }
}
