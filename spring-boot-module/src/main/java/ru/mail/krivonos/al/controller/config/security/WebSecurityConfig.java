package ru.mail.krivonos.al.controller.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import ru.mail.krivonos.al.controller.config.security.handler.AppAccessDeniedHandler;
import ru.mail.krivonos.al.controller.config.security.handler.AppAuthenticationSuccessHandler;

import static ru.mail.krivonos.al.controller.constant.AuthorityConstants.ADMIN_AUTHORITY_NAME;
import static ru.mail.krivonos.al.controller.constant.URLConstants.BOOTSTRAP_CONTENT_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.FORBIDDEN_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.HOMEPAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.INTERNAL_ERROR_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.LOGIN_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REVIEWS_DELETE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REVIEWS_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REVIEWS_PAGE_WITH_PAGE_NUMBER_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REVIEWS_UPDATE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.USERS_ADD_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.USERS_DELETE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.USERS_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.USERS_PAGE_WITH_PAGE_NUMBER_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.USERS_PASSWORD_CHANGE_URL;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService detailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public WebSecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.detailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(detailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(USERS_PAGE_URL, USERS_PAGE_WITH_PAGE_NUMBER_URL, USERS_ADD_PAGE_URL, USERS_DELETE_URL,
                        USERS_PASSWORD_CHANGE_URL, REVIEWS_UPDATE_URL)
                .hasAuthority(ADMIN_AUTHORITY_NAME)
                .antMatchers(HOMEPAGE_URL, REVIEWS_PAGE_URL, REVIEWS_PAGE_WITH_PAGE_NUMBER_URL, BOOTSTRAP_CONTENT_URL,
                        FORBIDDEN_PAGE_URL, INTERNAL_ERROR_PAGE_URL, REVIEWS_DELETE_URL)
                .permitAll()
                .and()
                .formLogin()
                .loginPage(LOGIN_PAGE_URL)
                .successHandler(authenticationSuccessHandler())
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .csrf()
                .disable();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AppAuthenticationSuccessHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AppAccessDeniedHandler();
    }
}
