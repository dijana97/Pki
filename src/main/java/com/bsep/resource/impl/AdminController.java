package com.bsep.resource.impl;

import com.bsep.domain.Admin;
import com.bsep.domain.Certificate;
import com.bsep.domain.LoginViewModel;
import com.bsep.repository.AdminRepository;
import com.bsep.repository.CertificateRepository;
import com.bsep.resource.Resource;
import com.bsep.service.IService;
import com.bsep.service.impl.CertificateService;
import com.bsep.service.impl.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


@RestController
@RequestMapping("/login")
@CrossOrigin(origins="http://localhost:3000")
public class AdminController {

    @Autowired
    private LoginService loginService;

    private AdminRepository adminRepository;

    public AdminController(AdminRepository groupRepository) {
        this.adminRepository = groupRepository;
    }

    @PostMapping(value = "/logindata", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<LoginViewModel> login(@RequestBody LoginViewModel admin, HttpServletResponse response) throws AuthenticationException, IOException {

        Admin log = loginService.loginUser(admin.getUsername(), admin.getPassword());
        System.out.println("LOGUJEM SE" + admin.getUsername() +admin.getPassword());
        if (log != null) {

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<>(HttpStatus.OK);
    }
}