package com.loanpro.calculator.security.services;

import com.loanpro.calculator.models.User;
import com.loanpro.calculator.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

    public User getCurrentUser() {
        return userRepository.findByUsername(
                ((UserDetailsImpl)SecurityContextHolder.getContext()
                                .getAuthentication()
                                .getPrincipal()).getUsername())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Current user not found, a new log in is required."));
    }

}
