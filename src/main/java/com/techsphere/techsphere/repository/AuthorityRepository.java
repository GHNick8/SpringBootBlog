package com.techsphere.techsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techsphere.techsphere.models.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long>{}