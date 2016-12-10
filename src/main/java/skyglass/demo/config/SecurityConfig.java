package skyglass.demo.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import skyglass.demo.SkgApplication;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = {"skyglass.demo.config.security", "skyglass.demo.controller.security"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
    // Token is valid for one month
    private static final int TOKEN_VALIDITY_DAYS = 31;

    private static final int TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * TOKEN_VALIDITY_DAYS;
	
	private static final String LOGIN_PAGE = "/" + SkgApplication.APP_NAME + "/login";
	
	public static final String REMEMBER_ME_AUTHENTICATE_URL = "/rememberMeAuthenticate";
	
	public static final String AUTHENTICATE_URL = "/authenticate";

	protected final Log logger = LogFactory.getLog(getClass());

    public static final String REMEMBER_ME_COOKIE = "remember-me";
    
    public static final String REMEMBER_ME_PARAM = "rememberMe";

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	PersistentTokenRepository tokenRepository;
	
    @Autowired
    protected AuthenticationEntryPoint authenticationEntryPoint;
    
    @Autowired
    protected AuthenticationFailureHandler authenticationFailureHandler;
    
    @Autowired
    protected AuthenticationSuccessHandler authenticationSuccessHandler;	
    
	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authenticationProvider());
	} 

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
		.and()
			.authorizeRequests()
			.antMatchers("/index.html", LOGIN_PAGE).permitAll()
		.and()
			.formLogin()			
			.loginProcessingUrl(AUTHENTICATE_URL)
			.usernameParameter("username")
			.passwordParameter("password")
			.successHandler(authenticationSuccessHandler)
			.failureHandler(authenticationFailureHandler)
		.and()
			.rememberMe()
			.rememberMeParameter(REMEMBER_ME_PARAM)
			.tokenRepository(tokenRepository)
			.tokenValiditySeconds(TOKEN_VALIDITY_SECONDS)			
		.and()
			.csrf()
			.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		return authenticationProvider;
	}

	@Bean
	public PersistentTokenBasedRememberMeServices getPersistentTokenBasedRememberMeServices() {
		PersistentTokenBasedRememberMeServices tokenBasedservice = new PersistentTokenBasedRememberMeServices(
				REMEMBER_ME_COOKIE, userDetailsService, tokenRepository);
		return tokenBasedservice;
	}	

}