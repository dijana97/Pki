package com.bsep;

import com.bsep.domain.Certificate;
import com.bsep.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private IService<Certificate> service;


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
			Certificate certificate = new Certificate();
			certificate.setSubject("123456789");
			certificate.setEmail("cert@gmail.com");
			DateFormat start = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
		DateFormat end = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
			start.parse("Sun Apr 12 00:00:00 CEST 2020");
			end.parse("Sat Dec 12 00:00:00 CEST 2020");
			certificate.setEndDate(start.parse("Sat Dec 12 00:00:00 CEST 2020"));
			certificate.setStartDate(end.parse("Sun Apr 12 00:00:00 CEST 2020"));
			certificate.setExtension("cdf");
			certificate.setName("cc");
			certificate.setAim("fefhei");
			certificate.setSurname("rrr");
			certificate.setRevoked("false");
			certificate.setIssuer("123456789");
			certificate.setType("Root");
			certificate.setWithdrawn(false);
			service.saveOrUpdate(certificate);

	}

}
