package com.bsep.service.impl;

import com.bsep.domain.Admin;
import com.bsep.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private AdminRepository adminRepository;

    public Admin loginUser(String username, String password) {
      //  if (!validateFields(username, password)) {
        //    return null;
        //}
        //System.out.println(passwordEncoder.encode(password));

        Admin a = adminRepository.findByUsername(username);

        if (a == null)
            return null;
      //  else if (a != null && a.isFirstLogin())  {
            //a.setFirstLogin(false);
        //    return a;
        //}
        else
            return a;
    }

}
