package com.compliant.application;

import com.compliant.application.dto.ComplaintDto;
import com.compliant.application.dto.ComplaintUpdateDto;
import com.compliant.application.mapper.ComplaintMapper;
import com.compliant.domain.model.Complaint;
import com.compliant.domain.service.ComplaintService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ComplaintApplicationServiceTest {

    private static final String CONTENT = "Updated content";
    private ComplaintService complaintService;
    private ComplaintMapper complaintMapper;
    private ComplaintApplicationService complaintApplicationService;

    @BeforeEach
    void setUp() {
        complaintService = mock(ComplaintService.class);
        complaintMapper = mock(ComplaintMapper.class);
        complaintApplicationService = new ComplaintApplicationService(complaintService, complaintMapper);
    }

    @Test
    void shouldCreateComplaintSuccessfully() {
        // given
        ComplaintDto inputDto = new ComplaintDto();
        Complaint mappedComplaint = new Complaint();
        Complaint createdComplaint = new Complaint();
        ComplaintDto expectedDto = new ComplaintDto();

        when(complaintMapper.toDto(inputDto)).thenReturn(mappedComplaint);
        when(complaintService.createComplaint(mappedComplaint)).thenReturn(createdComplaint);
        when(complaintMapper.toEntity(createdComplaint)).thenReturn(expectedDto);

        // when
        ComplaintDto result = complaintApplicationService.createComplaint(inputDto);

        // then
        assertThat(result).isEqualTo(expectedDto);
        verify(complaintMapper).toDto(inputDto);
        verify(complaintService).createComplaint(mappedComplaint);
        verify(complaintMapper).toEntity(createdComplaint);
    }

    @Test
    void shouldUpdateComplaintSuccessfully() {
        // given
        Long complaintId = 1L;
        ComplaintUpdateDto inputDto = new ComplaintUpdateDto();
        inputDto.setContent(CONTENT);
        Complaint mappedComplaint = new Complaint();
        mappedComplaint.setContent(CONTENT);

        Complaint updatedComplaint = new Complaint();
        ComplaintDto expectedDto = new ComplaintDto();

        when(complaintService.updateComplaint(complaintId, CONTENT)).thenReturn(updatedComplaint);
        when(complaintMapper.toEntity(updatedComplaint)).thenReturn(expectedDto);

        // when
        ComplaintDto result = complaintApplicationService.updateComplaint(complaintId, inputDto);

        // then
        assertThat(result).isEqualTo(expectedDto);
        verify(complaintService).updateComplaint(complaintId, CONTENT);
        verify(complaintMapper).toEntity(updatedComplaint);
    }

    @Test
    void shouldGetComplaintSuccessfully() {
        // given
        Long complaintId = 42L;
        Complaint complaint = new Complaint();
        ComplaintDto expectedDto = new ComplaintDto();

        when(complaintService.getComplaint(complaintId)).thenReturn(complaint);
        when(complaintMapper.toEntity(complaint)).thenReturn(expectedDto);

        // when
        ComplaintDto result = complaintApplicationService.getComplaint(complaintId);

        // then
        assertThat(result).isEqualTo(expectedDto);
        verify(complaintService).getComplaint(complaintId);
        verify(complaintMapper).toEntity(complaint);
    }
}
