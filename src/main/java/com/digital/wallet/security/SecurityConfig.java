package com.digital.wallet.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.digital.wallet.jwtUtils.TokenRequestFilter;
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private TokenRequestFilter tokenRequestFilter;
	@Autowired
	private AuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		// We don't need CSRF for this example
		httpSecurity.csrf().disable()
				// dont authenticate this particular request
		
				.authorizeRequests().antMatchers("/api/v1/**","/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll().
				// all other requests need to be authenticated
				anyRequest().authenticated().and().
				// make sure we use stateless session; session won't be used to
				// store user's state.
				exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(tokenRequestFilter, UsernamePasswordAuthenticationFilter.class);
		//httpSecurity.cors();
	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		System.out.println("cconfiguer auth beforre");
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		System.out.println("cconfiguer auth after");
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}