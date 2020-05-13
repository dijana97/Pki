package com.bsep.resource.impl;

import com.bsep.domain.Admin;
import com.bsep.repository.CertificateRepository;
import com.bsep.resource.Resource;
import com.bsep.domain.Certificate;
import com.bsep.service.IService;
import com.bsep.service.impl.CertificateService;
import org.omg.CORBA.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/certificates")
@CrossOrigin(origins="http://localhost:3000")
public class CertificateResourceImpl implements Resource<Certificate> {

    @Autowired
    private IService<Certificate> certificateIService;

    @Autowired
    private CertificateRepository certificateRepsoitory;

    @Autowired
    private CertificateService certificateService;

    @RequestMapping("/dashboard")
    public String firstPage() {
        return "success";
    }

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
        if(certificateService.validation(certificate)) {
            boolean retValue = certificateService.createRootCertificate(certificate, certificate.getType());

            return new ResponseEntity<>(certificateIService.saveOrUpdate(certificate), HttpStatus.CREATED);
        }
        return  new ResponseEntity<>(HttpStatus.BAD_GATEWAY);

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

    @GetMapping("/aimroot")
    public  ResponseEntity<Set<String>> aimRoot() {
        return new ResponseEntity<>(new TreeSet<>(Arrays.asList(
                "Signing the certificate", "Withdrawal of certificate",
                "Signature and withdrawal")), HttpStatus.OK);
    }

    @PostMapping("/login")
    Admin admin(@RequestBody Admin admin) {
        System.out.println("username je : " + admin.getUsername());
        return admin;
    }

    @GetMapping("/issuers")
    public  ResponseEntity<Set<String>> issuers() {
        List<Certificate> nova=certificateRepsoitory.findAll();
        List<String> padajuca=new List<String>() {
            @Override
            public int size() {

                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<String> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(String s) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends String> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, Collection<? extends String> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public String get(int index) {
                return null;
            }

            @Override
            public String set(int index, String element) {
                return null;
            }

            @Override
            public void add(int index, String element) {

            }

            @Override
            public String remove(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<String> listIterator() {
                return null;
            }

            @Override
            public ListIterator<String> listIterator(int index) {
                return null;
            }

            @Override
            public List<String> subList(int fromIndex, int toIndex) {
                return null;
            }
        };


        ArrayList<String> a=new ArrayList<String>();

        for(int i=0; i<nova.size(); i++){

            Boolean valid=validiranje(nova.get(i));


            if(nova.get(i).getType().equals("Intermediate") || nova.get(i).getType().equals("Root")){
             //   if(a.contains(nova.get(i).getSubject())) {

                    if (nova.get(i).getType().equals("Root") && nova.get(i).isWithdrawn() == false) {
                        padajuca.add(nova.get(i).getSubject());
                        a.add(nova.get(i).getSubject());
                    }else if(nova.get(i).getType().equals("Intermediate") && valid==true && nova.get(i).isWithdrawn() == false){
                        padajuca.add(nova.get(i).getSubject());
                        a.add(nova.get(i).getSubject());
               // }
                    }
            }

            }

        return new ResponseEntity<>(new TreeSet<>(a), HttpStatus.OK);
    }


    public Boolean validiranje(Certificate c){

        Boolean revoked=null;
        if(c.getType().equals("Intermediate")){
            Certificate cc=certificateRepsoitory.findBySubject(c.getIssuer());
            System.out.println("Validiranje " + cc.getSubject());
            System.out.println("Povucen " + cc.isWithdrawn());
            if(cc.isWithdrawn()==true){
                c.setWithdrawn(true);
                revoked= false;
            }else {
                if (cc.getType().equals("Intermediate")) {
                    Certificate ccc = certificateRepsoitory.findBySubject(cc.getIssuer());
                    validiranje(ccc);
                }
                revoked = true;
            }
        }
        return revoked;
    }

    @GetMapping("/download/{id}")
    public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable Long id){
        System.out.println("id cert je:  " + id);
        File downloadCer = certificateService.downloadCertificate(id);
        response.setContentType("application/pkix-cert");
        response.setContentLength((int) downloadCer.length());
        response.addHeader("Content-Disposition", "attachment; filename="+ downloadCer.getName());

        try {
            Files.copy(Paths.get(downloadCer.getPath()), response.getOutputStream() );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}