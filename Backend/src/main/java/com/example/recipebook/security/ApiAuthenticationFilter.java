package com.example.recipebook.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.recipebook.model.User;
import com.example.recipebook.repository.UserRepository;
import com.example.recipebook.service.CustomUserDetail;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private TokenGenerator tokenGenerator;

	@Autowired
	private UserRepository userRepo; 

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String authHeader = request.getHeader("Authorization");
		String requestURI = request.getRequestURI();
		
		if(requestURI.startsWith("/admin/recipebook")) {
			filterChain.doFilter(request, response);
			return;
			
		}
		
		if(authHeader!=null && authHeader.startsWith("Bearer")) {
			String token = authHeader.substring(7);
			if(tokenGenerator.validateToken(token)) {
				User user = userRepo.findByToken(token);
				if(user!=null) {
					
					CustomUserDetail userDetails = new CustomUserDetail(user);
					
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);
							
				} else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("User not found for the token");
                    return;
				}
			} else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired token");
                return;
            }
		} else if (request.getRequestURI().startsWith("/recipebook/") 
                && !request.getRequestURI().equals("/recipebook/register") 
                && !request.getRequestURI().equals("/recipebook/login")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token required");
            return;
		
	}
			filterChain.doFilter(request, response);
	}
}

