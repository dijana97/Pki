package com.bsep.service.impl;

import com.bsep.certificates.CertificateGenerator;
import com.bsep.data.IssuerData;
import com.bsep.data.SubjectData;
import com.bsep.domain.Certificate;
import com.bsep.keystores.KeyStoreReader;
import com.bsep.keystores.KeyStoreWriter;
import com.bsep.repository.CertificateRepository;
import com.bsep.service.IService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import Decoder.BASE64Encoder;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.stereotype.Service;
import java.util.Base64;


@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private KeyStoreWriter keyStoreWriter;

    @Autowired
    private KeyStoreReader keyStoreReader;

    @Autowired
    private IService<Certificate> certificateIService;

    private KeyPair getKeyPair(){
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    private SubjectData getSubjectData(Certificate certificate, PublicKey pk) {
        //KeyPair keyPairSubject = getKeyPair();


       Date startDate = certificate.getStartDate();


       Date endDate = certificate.getEndDate();


        String serialNumber = certificate.getSubject();

        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, certificate.getName() + " " + certificate.getSurname());
        builder.addRDN(BCStyle.SURNAME, certificate.getName());
        builder.addRDN(BCStyle.GIVENNAME, certificate.getSurname());
        builder.addRDN(BCStyle.E, certificate.getEmail());

        return new SubjectData(pk, builder.build(), serialNumber, startDate, endDate);
    }

    public boolean createRootCertificate(Certificate certificate, String type){

        if(validation(certificate)) {
            if (type.equals("Root")) {
                Certificate cer = certificateRepository.save(certificate);
                KeyPair selfKey = getKeyPair();
                SubjectData subjectData = getSubjectData(cer, selfKey.getPublic());
                System.out.println(certificate.getStartDate());
                System.out.println(certificate.getStartDate());
                X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
                builder.addRDN(BCStyle.CN, certificate.getName() + " " + certificate.getSurname());
                builder.addRDN(BCStyle.SURNAME, certificate.getName());
                builder.addRDN(BCStyle.GIVENNAME, certificate.getSurname());
                builder.addRDN(BCStyle.E, certificate.getEmail());

                IssuerData issuerData = new IssuerData(selfKey.getPrivate(), builder.build());
                CertificateGenerator certGenerator = new CertificateGenerator();
                X509Certificate certX509 = certGenerator.generateCertificate(subjectData, issuerData);

                String keyStoreFile = "cert.jks";
                KeyStoreWriter kw = new KeyStoreWriter();
                kw.loadKeyStore(keyStoreFile, "bsep20".toCharArray());
                kw.write(subjectData.getSerialNumber(), selfKey.getPrivate(), "bsep20".toCharArray(), certX509);
                kw.saveKeyStore(keyStoreFile, "bsep20".toCharArray());
                return true;
            } else if (type.equals("Intermediate")) {

                Certificate cer = certificateRepository.save(certificate);
                IssuerData issuerData = keyStoreReader.readIssuerFromStore("cert.jks", certificate.getIssuer(), "bsep20".toCharArray(), "bsep20".toCharArray());
                KeyPair subjectKey = getKeyPair();
                SubjectData subjectData = getSubjectData(cer, subjectKey.getPublic());
                CertificateGenerator certGenerator = new CertificateGenerator();
                X509Certificate certX509 = certGenerator.generateCertificate(subjectData, issuerData);
                String keyStoreFile = "";
                keyStoreFile = "cert.jks";

                KeyStoreWriter kw = new KeyStoreWriter();
                kw.loadKeyStore(keyStoreFile, "bsep20".toCharArray());
                kw.write(subjectData.getSerialNumber(), subjectKey.getPrivate(), "bsep20".toCharArray(), certX509);
                kw.saveKeyStore(keyStoreFile, "bsep20".toCharArray());
                return true;
            } else if (type.equals("End-entity")) {

                Certificate cer = certificateRepository.save(certificate);
                IssuerData issuerData = keyStoreReader.readIssuerFromStore("cert.jks", certificate.getIssuer(), "bsep20".toCharArray(), "bsep20".toCharArray());
                KeyPair subjectKey = getKeyPair();
                SubjectData subjectData = getSubjectData(cer, subjectKey.getPublic());
                CertificateGenerator certGenerator = new CertificateGenerator();
                X509Certificate certX509 = certGenerator.generateCertificate(subjectData, issuerData);
                String keyStoreFile = "";
                keyStoreFile = "endentity.jks";

                KeyStoreWriter kw = new KeyStoreWriter();
                kw.loadKeyStore(keyStoreFile, "bsep20".toCharArray());
                kw.write(subjectData.getSerialNumber(), subjectKey.getPrivate(), "bsep20".toCharArray(), certX509);
                kw.saveKeyStore(keyStoreFile, "bsep20".toCharArray());
                return true;
            } else {
                return false;
            }

        }

        return false;
    }


    public boolean validation(Certificate certificate){
        boolean provera = true;

        List<Certificate>certificates = (List<Certificate>) certificateRepository.findAll();
        for(Certificate c:certificates){
            if(c.getSubject().equals(certificate.getSubject()) || c.getEmail().equals(certificate.getEmail())){
                provera = false;
            }
        }

        if(certificate.getType().equals("Root")){
            if(certificate.getAimroot().equals("Signing the certificate") || certificate.getAimroot().equals("Withdrawal of certificate") || certificate.getAimroot().equals("Signature and withdrawal") ){
                provera = true;
            }else{
                provera = false;
            }
            if(!certificate.getIssuer().equals(certificate.getSubject())){
                provera = false;
            }
        }



        if(certificate.getEndDate().compareTo(certificate.getStartDate()) <= 0){
            provera = false;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        Date date =java.sql.Timestamp.valueOf(now);

        if(certificate.getEndDate().compareTo(date) <= 0){
            provera = false;
        }

        if(certificate.getType().equals("Intermediate") || certificate.getType().equals("End-entity")){
            if(certificate.getAim()==null || certificate.getAim().equals("")){
                provera = false;
            }
        }

      /* Certificate issuer = certificateRepository.findByIssuer(certificate.getIssuer());
        if(issuer!= null){
            if(issuer.getEndDate().compareTo(certificate.getEndDate()) < 0 ){
                provera = false;
            }
        }*/
        return provera;
    }

    public File downloadCertificate(Long id) {
        Certificate certificate = certificateIService.findById(id);
        java.security.cert.Certificate c = findFromKeystore(certificate.getSubject(),certificate.getType());
        File file  = writeToFile(c);
        try {
            FileInputStream inStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }

    private java.security.cert.Certificate findFromKeystore(String subjectNum, String type) {

        String keyStoreFile = type.equals("End-entity") ? "endentity.jks" : "cert.jks" ;
        return keyStoreReader.readCertificate(keyStoreFile, "bsep20", subjectNum);
    }

    private File writeToFile(java.security.cert.Certificate cert) {
        File file = new File("certificate.cer");
        FileOutputStream os = null;
        byte[] buf = new byte[0];
        System.out.println(cert+ "    Cert print ");
        try {
            buf = cert.getEncoded();
            os = new FileOutputStream(file);
            os.write(buf);
            Writer wr = new OutputStreamWriter(os, Charset.forName("UTF-8"));
            wr.write(new BASE64Encoder().encode(buf));
            wr.flush();
            os.close();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }



}
