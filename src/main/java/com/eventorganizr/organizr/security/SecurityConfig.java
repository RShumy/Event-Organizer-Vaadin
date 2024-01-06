package com.eventorganizr.organizr.security;
import com.eventorganizr.organizr.views.accessViews.LogInView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig
        extends VaadinWebSecurity {


    private final UserPrincipalDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    SecurityConfig(UserPrincipalDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        System.out.println(
                "From Security Config -> the User Detail Service hash is: " +
                        System.identityHashCode(userDetailsService.getUserService()));
    }

//    @Bean
//    @Primary
//    public AuthenticationManagerBuilder authManagerBuilder(AuthenticationManagerBuilder authManager) throws Exception {
//        return authManager.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder()).and();
//    }

    // Added afterwards
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder); // Choose your preferred password encoder
        return provider;
    }

    // Possible to configure endpoints to be ignored, this is almost equivalent with
    // httpSecurity.authorizeRequests( auth -> .antMatchers("/endpointwithoutlogin").anonymous() )
    // this won't interfere with adding a custom LogIn View
    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring().antMatchers("/VAADIN/**","/h2/**","/image/**","/favicon.ico");
        super.configure(webSecurity);
    }

    // Reverted to this option per recommendation of a Vaadin Community member
    // followed official documentation, removed the .authorizeRequests() chain method because it was interfering
    // with adding a custom LogIn View, where the frontend was frozen with a "Connection Lost" message
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic()
                .and()
                .headers(headers -> headers.frameOptions().sameOrigin())
                .userDetailsService(userDetailsService)
//                .formLogin().loginPage("/login").permitAll()
//                .and()
                .logout((logout) ->
                        logout.deleteCookies("JSESSIONID")
                                .invalidateHttpSession(true)
//                                .logoutUrl("/logout")
                                .clearAuthentication(true));
        super.configure(httpSecurity);
        setLoginView(httpSecurity, LogInView.class);
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder();}


}
