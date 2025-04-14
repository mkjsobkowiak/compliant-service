package com.compliant.domain.service;

import com.compliant.domain.model.Complaint;

public interface ComplaintService {
    Complaint createComplaint(Complaint complaint);

    Complaint updateComplaint(Long complaintId, String content);

    Complaint getComplaint(Long complaintId);
}
