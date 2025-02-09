package com.risknarrative.repository;

import com.risknarrative.entity.Addresses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Addresses, Long> {
}
