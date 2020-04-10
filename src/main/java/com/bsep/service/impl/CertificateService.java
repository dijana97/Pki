package com.bsep.service.impl;

import com.bsep.certificates.CertificateGenerator;
import com.bsep.data.IssuerData;
import com.bsep.data.SubjectData;
import com.bsep.domain.Certificate;
import com.bsep.keystores.KeyStoreReader;
import com.bsep.keystores.KeyStoreWriter;
import com.bsep.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.stereotype.Service;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private KeyStoreWriter keyStoreWriter;

    @Autowired
    private KeyStoreReader keyStoreReader;

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
        Date startDate= null;
        try {
            startDate = new SimpleDateFormat("dd/MM/yyyy").parse(certificate.getStartDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date endDate= null;
        try {
            endDate = new SimpleDateFormat("dd/MM/yyyy").parse(certificate.getEndDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String serialNumber = certificate.getSubject();

        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, certificate.getName() + " " + certificate.getSurname());
        builder.addRDN(BCStyle.SURNAME, certificate.getName());
        builder.addRDN(BCStyle.GIVENNAME, certificate.getSurname());
        builder.addRDN(BCStyle.E, certificate.getEmail());

        return new SubjectData(pk, builder.build(), serialNumber, startDate, endDate);
    }

    public boolean createRootCertificate(Certificate certificate, String type){

        if(type.equals("Root")){
            Certificate cer = certificateRepository.save(certificate);
            KeyPair selfKey = getKeyPair();
            SubjectData subjectData = getSubjectData(cer,selfKey.getPublic());

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
        }
        else if(type.equals("Intermediate")){

            Certificate cer = certificateRepository.save(certificate);
            IssuerData issuerData = keyStoreReader.readIssuerFromStore("cert.jks", certificate.getIssuer(), "bsep20".toCharArray(), "bsep20".toCharArray());
            KeyPair subjectKey = getKeyPair();
            SubjectData subjectData = getSubjectData(cer,subjectKey.getPublic());
            CertificateGenerator certGenerator = new CertificateGenerator();
            X509Certificate certX509 = certGenerator.generateCertificate(subjectData, issuerData);
            String keyStoreFile = "";
            keyStoreFile = "cert.jks";

            KeyStoreWriter kw = new KeyStoreWriter();
            kw.loadKeyStore(keyStoreFile, "bsep20".toCharArray());
            kw.write(subjectData.getSerialNumber(), subjectKey.getPrivate(), "bsep20".toCharArray(), certX509);
            kw.saveKeyStore(keyStoreFile, "bsep20".toCharArray());
            return true;
        }else if(type.equals("End-entity")){

            Certificate cer = certificateRepository.save(certificate);
            IssuerData issuerData = keyStoreReader.readIssuerFromStore("cert.jks", certificate.getIssuer(), "bsep20".toCharArray(), "bsep20".toCharArray());
            KeyPair subjectKey = getKeyPair();
            SubjectData subjectData = getSubjectData(cer,subjectKey.getPublic());
            CertificateGenerator certGenerator = new CertificateGenerator();
            X509Certificate certX509 = certGenerator.generateCertificate(subjectData, issuerData);
            String keyStoreFile = "";
            keyStoreFile = "endentity.jks";

            KeyStoreWriter kw = new KeyStoreWriter();
            kw.loadKeyStore(keyStoreFile, "bsep20".toCharArray());
            kw.write(subjectData.getSerialNumber(), subjectKey.getPrivate(), "bsep20".toCharArray(), certX509);
            kw.saveKeyStore(keyStoreFile, "bsep20".toCharArray());
            return true;
        }else{
            return false;
        }


    }


}
