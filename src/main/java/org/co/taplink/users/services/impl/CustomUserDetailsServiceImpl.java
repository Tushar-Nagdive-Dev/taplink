package org.co.taplink.users.services.impl;

import lombok.RequiredArgsConstructor;
import org.co.taplink.users.repository.UsersRepository;
import org.co.taplink.users.services.CustomUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.usersRepository.findByUsernameWithRoles(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
