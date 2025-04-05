package com.rosist.kardex.security4.repository;

import com.rosist.kardex.security4.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {

    @Query("select o from Operation o where o.permitAll = true")
    List<Operation> findByPublicAccess();
}
