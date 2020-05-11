package com.bsep.service.impl;

import com.bsep.domain.Admin;
import com.bsep.repository.AdminRepository;
import com.bsep.security.AdminPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminPrincipalDetailsService implements UserDetailsService {
    private AdminRepository adminRepository;

    public AdminPrincipalDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Admin admin = this.adminRepository.findByUsername(s);
        AdminPrincipal adminPrincipal = new AdminPrincipal(admin);

        return adminPrincipal;
    }
}
