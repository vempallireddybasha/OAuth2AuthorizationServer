package com.example.OAuthServer.service;

;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CustomAuthenticationProvider  implements AuthenticationProvider {

    @Autowired
    EmployeeService employeeService;

    @Autowired

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
       String password= authentication.getCredentials().toString();
        UserDetails userDetails = employeeService.loadUserByUsername(name);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;
        if(bCryptPasswordEncoder.matches(password,userDetails.getPassword())){
             usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());

        }
        else{
            new BadCredentialsException("Bad Credentials!!!!");
        }
        return  usernamePasswordAuthenticationToken;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return  UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
