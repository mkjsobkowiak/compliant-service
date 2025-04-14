package com.compliant.application;

import com.compliant.application.dto.ComplaintDto;
import com.compliant.application.dto.ComplaintUpdateDto;
import com.compliant.application.mapper.ComplaintMapper;
import com.compliant.domain.model.Complaint;
import com.compliant.domain.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ComplaintApplicationService {

    private final ComplaintService complaintService;
    private final ComplaintMapper complaintMapper;

    @Transactional
    public ComplaintDto createComplaint(ComplaintDto complaintDto) {
        Complaint complaint = complaintMapper.toDto(complaintDto);
        return complaintMapper.toEntity(complaintService.createComplaint(complaint));
    }

    @Transactional
    public ComplaintDto updateComplaint(Long complaintId, ComplaintUpdateDto updateDto) {
        return complaintMapper.toEntity(complaintService.updateComplaint(complaintId, updateDto.getContent()));
    }

    public ComplaintDto getComplaint(Long complaintId) {
        return complaintMapper.toEntity(complaintService.getComplaint(complaintId));
    }
}
