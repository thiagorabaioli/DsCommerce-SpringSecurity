package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    public void validateSelfOrtAdmin(long userId) {

        User userMe = userService.authenticated();
        if (!userMe.hasHole("ROLE_ADMIN") && !userMe.getId().equals(userId)) {
            throw new ForbiddenException("acesso negado");
        }
    }
}
