package com.mightyjava.service.impl;

import com.mightyjava.domain.Certificate;
import com.mightyjava.repository.CertificateRepository;
import com.mightyjava.service.IService;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;
@Service
public class CertificateServiceImpl implements IService<Certificate> {

    @Autowired
    private CertificateRepository certificateRepository;

    Logger logger
            = Logger.getLogger(
            CertificateServiceImpl.class.getName());

    @Override
    public Page<Certificate> findAll(Pageable pageable, String searchText) {
        return certificateRepository.findAllCertificate(pageable,searchText);
    }

    @Override
    public Page<Certificate> findAll(Pageable pageable) {
        return certificateRepository.findAll(pageable);
    }

    @Override
    public Certificate findById(Long id) {
        return certificateRepository.findById(id).get();
    }

    @Override
    public Certificate saveOrUpdate(Certificate certificate) {
        return certificateRepository.save(certificate);
    }

    @Override
    public String deleteById(Long id) {
        JSONObject jsonObject = new JSONObject();
        try {
            certificateRepository.deleteById(id);
            jsonObject.put("message", "Certificate deleted successfully");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
