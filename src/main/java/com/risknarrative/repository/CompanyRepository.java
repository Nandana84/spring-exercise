package com.risknarrative.repository;

import com.risknarrative.entity.Companies;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Companies, Long> {
}
