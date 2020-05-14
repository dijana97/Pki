package com.bsep.security;


import com.bsep.security.auth.RestAuthenticationEntryPoint;
import com.bsep.security.auth.TokenAuthenticationFilter;
import com.bsep.service.impl.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private LoginService jwtUserDetailsService;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // Definisemo nacin autentifikacije
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Autowired
    TokenUtils tokenUtils;

    // Definisemo prava pristupa odredjenim URL-ovima
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // komunikacija izmedju klijenta i servera je stateless
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                // za neautorizovane zahteve posalji 401 gresku
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()

                // svim korisnicima dopusti da pristupe putanjama /auth/**, /h2-console/** i
                // /api/foo
                .authorizeRequests().antMatchers("/add").hasRole("ADMIN")
                .antMatchers("/list").hasRole("ADMIN")
                .antMatchers("/certificates/type").hasRole("ADMIN")
                .antMatchers("/certificates/aimroot").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/certificates/issuers").hasRole("ADMIN")
                .anyRequest().authenticated().and()
                .cors()
                .and()
                .logout().logoutSuccessUrl("/").permitAll()
                .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());


        http.csrf().disable();
        http.headers().frameOptions().disable();

    }

    // Generalna bezbednost aplikacije
   @Override
    public void configure(WebSecurity web) {
        // TokenAuthenticationFilter ce ignorisati sve ispod navedene putanje
        web.ignoring().antMatchers(HttpMethod.POST);

        web.ignoring().antMatchers(HttpMethod.GET);

        // web.ignoring().antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html",
        // "/favicon.ico", "/**/*.html",
        // "/**/*.css", "/**/*.js");
    }


}