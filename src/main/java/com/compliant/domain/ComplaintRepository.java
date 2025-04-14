package com.compliant.domain;

import com.compliant.domain.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    Optional<Complaint> findByProductIdAndReportedBy(String id, String reporter);
}
