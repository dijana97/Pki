package com.bsep;

import com.bsep.domain.Certificate;
import com.bsep.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private IService<Certificate> service;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		for(int i=1; i<=3; i++) {
			Certificate certificate = new Certificate();
			certificate.setSubject("Cert");
			certificate.setEmail("cert@gmail.com");
			certificate.setEndDate("12.12.2020.");
			certificate.setStartDate("01.01.2020.");
			certificate.setExtension("cdf");
			certificate.setName("cc");
			certificate.setAim("fefhei");
			certificate.setSurname("rrr");
			certificate.setRevoked("false");
			certificate.setIssuer("mali");
			certificate.setType("root");
			certificate.setWithdrawn(false);
			service.saveOrUpdate(certificate);
		}
	}

}
