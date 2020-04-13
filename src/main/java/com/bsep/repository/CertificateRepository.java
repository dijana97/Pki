package com.bsep.repository;

import com.bsep.domain.Certificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateRepository extends PagingAndSortingRepository<Certificate, Long> {

    @Query("From Certificate c WHERE c.subject=:searchText OR c.aim=:searchText OR c.issuer=:searchText OR c.name=:searchText OR c.surname=:searchText")
    Page<Certificate> findAllCertificate(Pageable pageable, @Param("searchText") String searchText);

    public Certificate findByIssuer(String data);

    public List<Certificate> findAll();
}
