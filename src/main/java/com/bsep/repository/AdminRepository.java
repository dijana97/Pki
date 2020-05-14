package com.bsep.repository;

import com.bsep.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>{

    Admin findByUsername(String username);

    Admin findOneByUsername(String username);
}
