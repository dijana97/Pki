package com.bsep.service.impl;

import com.bsep.domain.Admin;
import com.bsep.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
public class AdminService implements CommandLineRunner{


    private AdminRepository adminRepository;

    private PasswordEncoder passwordEncoder;


    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args)  {

        this.adminRepository.deleteAll();

        Admin admin1 = new Admin("admin1","$2a$10$mwePsuQdjt3W2t15GFKIAOYAYzA454sUG6qVaBckFtQmbhBJiOFxS");
        Admin admin2 = new Admin("admin2",passwordEncoder.encode("admin222"),"ADMIN","");
        admin1.setActive(1);
        List<Admin> admins = Arrays.asList(admin1,admin2);



        this.adminRepository.saveAll(admins);



    }
}
