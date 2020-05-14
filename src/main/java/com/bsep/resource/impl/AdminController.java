package com.bsep.resource.impl;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;


/*@RestController
@RequestMapping("/login")
@CrossOrigin(origins="http://localhost:3000")*/
public class AdminController {
/*
    @Autowired
    private LoginService loginService;

    private AdminRepository adminRepository;

    private JwtAuthenticationFilter filter;


    private org.springframework.security.authentication.AuthenticationManager authenticationManager;


    public AdminController(AdminRepository groupRepository) {
        this.adminRepository = groupRepository;
    }

    @PostMapping(value = "/logindata", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AdminTokenState> login(@RequestBody LoginViewModel admin, HttpServletResponse response) throws AuthenticationException, IOException {



        Admin ulogovan = loginService.loginUser(admin.getUsername(), admin.getPassword());
        System.out.println("LOGUJEM SE " + admin.getUsername() +" "+admin.getPassword());
        if (ulogovan != null) {

            System.out.println("Ulogovan "+ ulogovan.getUsername()+ " "+ulogovan.getPassword());
           filter.attemptAuthentication(admin,response);


            return  ResponseEntity.ok(new AdminTokenState(token,System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME));
        } else {
            return new ResponseEntity<AdminTokenState>(HttpStatus.NOT_FOUND);
        }

    }*/
}