package com.compliant.domain.service;

import com.compliant.application.exception.ComplaintNotFoundException;
import com.compliant.domain.ComplaintRepository;
import com.compliant.domain.model.Complaint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ComplaintServiceImplTest {

    private static final Long COMPLAINT_ID = 1L;
    private static final String CONTENT = "Updated content";
    private static final String PRODUCT_ID = "product-123";
    private static final String REPORTED_BY = "john.doe@example.com";

    private ComplaintRepository complaintRepository;
    private ComplaintServiceImpl complaintService;

    @BeforeEach
    void setUp() {
        complaintRepository = mock(ComplaintRepository.class);
        complaintService = new ComplaintServiceImpl(complaintRepository);
    }

    @Test
    void shouldCreateNewComplaintIfNoDuplicateExists() {
        // given
        Complaint newComplaint = new Complaint();
        newComplaint.setProductId(PRODUCT_ID);
        newComplaint.setReportedBy(REPORTED_BY);

        when(complaintRepository.findByProductIdAndReportedBy(PRODUCT_ID, REPORTED_BY))
                .thenReturn(Optional.empty());

        Complaint savedComplaint = new Complaint();
        savedComplaint.setSubmissionCount(1);
        savedComplaint.setCreatedAt(LocalDateTime.now());

        when(complaintRepository.save(newComplaint)).thenReturn(savedComplaint);

        // when
        Complaint result = complaintService.createComplaint(newComplaint);

        // then
        assertThat(result.getSubmissionCount()).isEqualTo(1);
        assertThat(result.getCreatedAt()).isNotNull();
        verify(complaintRepository).findByProductIdAndReportedBy(PRODUCT_ID, REPORTED_BY);
        verify(complaintRepository).save(newComplaint);
    }

    @Test
    void shouldUpdateSubmissionCountIfDuplicateComplaintExists() {
        // given
        Complaint existingComplaint = new Complaint();
        existingComplaint.setProductId(PRODUCT_ID);
        existingComplaint.setReportedBy(REPORTED_BY);
        existingComplaint.setSubmissionCount(2);

        Complaint newComplaint = new Complaint();
        newComplaint.setProductId(PRODUCT_ID);
        newComplaint.setReportedBy(REPORTED_BY);

        when(complaintRepository.findByProductIdAndReportedBy(PRODUCT_ID, REPORTED_BY))
                .thenReturn(Optional.of(existingComplaint));

        when(complaintRepository.save(existingComplaint)).thenReturn(existingComplaint);

        // when
        Complaint result = complaintService.createComplaint(newComplaint);

        // then
        assertThat(result.getSubmissionCount()).isEqualTo(3);
        verify(complaintRepository).findByProductIdAndReportedBy(PRODUCT_ID, REPORTED_BY);
        verify(complaintRepository).save(existingComplaint);
    }

    @Test
    void shouldUpdateComplaintContentSuccessfully() {
        // given
        Complaint complaint = new Complaint();
        complaint.setContent("Old content");

        when(complaintRepository.findById(COMPLAINT_ID)).thenReturn(Optional.of(complaint));
        when(complaintRepository.save(complaint)).thenReturn(complaint);

        // when
        Complaint result = complaintService.updateComplaint(COMPLAINT_ID, CONTENT);

        // then
        assertThat(result.getContent()).isEqualTo(CONTENT);
        verify(complaintRepository).findById(COMPLAINT_ID);
        verify(complaintRepository).save(complaint);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentComplaint() {
        // given
        when(complaintRepository.findById(COMPLAINT_ID)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> complaintService.updateComplaint(COMPLAINT_ID, CONTENT))
                .isInstanceOf(ComplaintNotFoundException.class)
                .hasMessageContaining(String.valueOf(COMPLAINT_ID));

        verify(complaintRepository).findById(COMPLAINT_ID);
        verify(complaintRepository, never()).save(any());
    }

    @Test
    void shouldReturnComplaintWhenExists() {
        // given
        Complaint complaint = new Complaint();
        when(complaintRepository.findById(COMPLAINT_ID)).thenReturn(Optional.of(complaint));

        // when
        Complaint result = complaintService.getComplaint(COMPLAINT_ID);

        // then
        assertThat(result).isEqualTo(complaint);
        verify(complaintRepository).findById(COMPLAINT_ID);
    }

    @Test
    void shouldThrowExceptionWhenComplaintNotFound() {
        // given
        when(complaintRepository.findById(COMPLAINT_ID)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> complaintService.getComplaint(COMPLAINT_ID))
                .isInstanceOf(ComplaintNotFoundException.class)
                .hasMessageContaining(String.valueOf(COMPLAINT_ID));

        verify(complaintRepository).findById(COMPLAINT_ID);
    }
}
