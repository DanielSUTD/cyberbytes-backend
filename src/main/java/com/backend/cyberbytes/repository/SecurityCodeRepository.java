package com.backend.cyberbytes.repository;

import com.backend.cyberbytes.model.SecurityCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface SecurityCodeRepository extends JpaRepository<SecurityCode, BigInteger> {
    SecurityCode findByEmail(String email);
}
