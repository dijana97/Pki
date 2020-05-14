package com.bsep.service.impl;

import com.bsep.domain.Admin;
import com.bsep.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Admin loginUser(String username, String password) {

        Admin admin = (Admin) adminRepository.findByUsername(username);

        if (admin == null)
            return null;
        else if (admin != null )  {
            return admin;
        }
        else
            return admin;
    }

}
