package ru.mail.krivonos.al.controller.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
import static ru.mail.krivonos.al.controller.constant.AuthorityConstants.CUSTOMER_AUTHORITY_NAME;
import static ru.mail.krivonos.al.controller.constant.AuthorityConstants.SALE_AUTHORITY_NAME;
import static ru.mail.krivonos.al.controller.constant.URLConstants.*;

@Configuration
@Order(2)
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
                .antMatchers(USERS_PAGE_URL, USERS_ADD_PAGE_URL, USERS_DELETE_URL,
                        USERS_PASSWORD_CHANGE_URL, REVIEWS_UPDATE_URL, REVIEWS_DELETE_URL, REVIEWS_PAGE_URL)
                .hasAuthority(ADMIN_AUTHORITY_NAME)
                .antMatchers(PROFILE_PAGE_URL, FAVORITE_ARTICLES_URL, PROFILE_UPDATE_URL, PROFILE_PASSWORD_UPDATE_URL,
                        ARTICLE_ADD_COMMENT_URL, REVIEWS_ADD_PAGE_URL, ORDERS_ADD_URL)
                .hasAuthority(CUSTOMER_AUTHORITY_NAME)
                .antMatchers(ADD_ARTICLE_PAGE_URL, DELETE_ARTICLE_URL, EDIT_ARTICLE_URL, ITEMS_ADD_URL, ORDER_PAGE_URL,
                        ITEMS_COPY_URL, ITEMS_DELETE_URL, ITEMS_UPLOAD_PAGE_URL)
                .hasAuthority(SALE_AUTHORITY_NAME)
                .antMatchers(ARTICLES_PAGE_URL, ARTICLE_PAGE_URL, ARTICLE_DELETE_COMMENT_URL, ITEMS_PAGE_URL,
                        ITEM_PAGE_URL, ORDERS_PAGE_URL)
                .hasAnyAuthority(CUSTOMER_AUTHORITY_NAME, SALE_AUTHORITY_NAME)
                .antMatchers(HOMEPAGE_URL, BOOTSTRAP_CONTENT_URL, FORBIDDEN_PAGE_URL,
                        INTERNAL_ERROR_PAGE_URL)
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
