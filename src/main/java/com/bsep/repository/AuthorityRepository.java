package com.bsep.repository;

import com.bsep.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByName(String name);
}
