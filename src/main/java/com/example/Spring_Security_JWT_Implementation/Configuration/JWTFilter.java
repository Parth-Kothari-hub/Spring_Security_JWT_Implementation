package com.example.Spring_Security_JWT_Implementation.Configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.Spring_Security_JWT_Implementation.Services.JwtService;
import com.example.Spring_Security_JWT_Implementation.Services.MyUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private ApplicationContext context;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Bearer + Token
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String userName = null;

		if (authHeader != null && authHeader.startsWith("Bearer")) {
			token = authHeader.substring(7);
			userName = jwtService.extractUserName(token);
		}

		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(userName);

			if (jwtService.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());

				authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authtoken);

			}
		}
		
		filterChain.doFilter(request, response);
	}

}
