package com.example.event5.demo_event5.authenticator;

import com.example.event5.demo_event5.model.Person;
import com.example.event5.demo_event5.model.Roles;
import com.example.event5.demo_event5.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthProvider implements AuthenticationProvider {
    @Autowired
   private PasswordEncoder passwordEncoder;
    @Autowired
    private PersonRepository personRepository;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name=authentication.getName();
        String pwd=authentication.getCredentials().toString();
        Person person=personRepository.findByUserName(name);
        if(person!=null && person.getId()>0 && passwordEncoder.matches(pwd,person.getPassword())){
            return new UsernamePasswordAuthenticationToken(
                    person.getUserName(),null,getGrantedAuthorities(person.getRoles())
            );
        }else{
            throw new BadCredentialsException("Invalid credentials!");
        }


    }
    private Collection<? extends GrantedAuthority> getGrantedAuthorities(Roles roles) {
        List<GrantedAuthority> grantedAuthorities=new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+roles.getRoleName()));
        return grantedAuthorities;
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
