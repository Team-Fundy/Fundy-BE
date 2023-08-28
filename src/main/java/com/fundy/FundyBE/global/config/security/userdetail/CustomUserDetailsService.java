package com.fundy.FundyBE.global.config.security.userdetail;

import com.fundy.FundyBE.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return CustomUserDetails.builder().user(userRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("Username Not Found: " + username))).build();
    }
}
