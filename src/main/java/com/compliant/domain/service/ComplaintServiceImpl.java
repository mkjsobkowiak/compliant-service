package com.compliant.domain.service;


import com.compliant.application.exception.ComplaintNotFoundException;
import com.compliant.domain.ComplaintRepository;
import com.compliant.domain.model.Complaint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;

    public Complaint createComplaint(Complaint complaint) {
        Optional<Complaint> existingComplaint = complaintRepository.findByProductIdAndReportedBy(complaint.getProductId(), complaint.getReportedBy());

        if (existingComplaint.isPresent()) {
            Complaint existing = existingComplaint.get();
            existing.setSubmissionCount(existing.getSubmissionCount() + 1);
            complaintRepository.save(existing);
            return existing;
        } else {
            complaint.setSubmissionCount(1);
            complaint.setCreatedAt(LocalDateTime.now());
        }

        return complaintRepository.save(complaint);
    }

    public Complaint updateComplaint(Long complaintId, String content) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ComplaintNotFoundException(complaintId));
        complaint.setContent(content);
        return complaintRepository.save(complaint);
    }

    public Complaint getComplaint(Long complaintId) {
        return complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ComplaintNotFoundException(complaintId));
    }
}
