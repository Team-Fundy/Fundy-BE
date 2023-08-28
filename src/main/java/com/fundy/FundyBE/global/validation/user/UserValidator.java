package com.fundy.FundyBE.global.validation.user;

import com.fundy.FundyBE.domain.user.repository.UserRepository;
import com.fundy.FundyBE.global.exception.customexception.DuplicateUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;
    public final void hasDuplicateEmail(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw DuplicateUserException.createBasic();
        });
    }

    public final void hasDuplicateNickname(String nickname) {
        userRepository.findByNickname(nickname).ifPresent(user -> {
            throw DuplicateUserException.createBasic();
        });
    }
}
