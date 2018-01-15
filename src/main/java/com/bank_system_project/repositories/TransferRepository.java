package com.bank_system_project.repositories;

import com.bank_system_project.models.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    List<Transfer> findAllByUserUsername(String username);
}
