package com.mightyjava.resource.impl;

import com.mightyjava.domain.Certificate;
import com.mightyjava.resource.Resource;
import com.mightyjava.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

@RestController
@RequestMapping("/certificates")
@CrossOrigin(origins="http://localhost:3000")
public class CertificateResourceImpl implements Resource<Certificate> {

    @Autowired
    private IService<Certificate> certificateIService;

    @Override
    public ResponseEntity<Page<Certificate>> findAll(Pageable pageable, String searchText) {
        return new ResponseEntity<>(certificateIService.findAll(pageable, searchText), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Page<Certificate>> findAll(int pageNumber, int pageSize) {
        return new ResponseEntity<>(certificateIService.findAll(
                PageRequest.of(
                        pageNumber, pageSize
                )
        ), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Certificate> findById(Long id) {
        return new ResponseEntity<>(certificateIService.findById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Certificate> save(Certificate certificate) {
        return new ResponseEntity<>(certificateIService.saveOrUpdate(certificate), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Certificate> update(Certificate book) {
        return new ResponseEntity<>(certificateIService.saveOrUpdate(book), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteById(Long id) {
        return new ResponseEntity<>(certificateIService.deleteById(id), HttpStatus.OK);
    }

    @GetMapping("/type")
    public  ResponseEntity<Set<String>> findAllTypes() {
        return new ResponseEntity<>(new TreeSet<>(Arrays.asList("Root", "Intermediate", "End-entity")), HttpStatus.OK);
    }

}