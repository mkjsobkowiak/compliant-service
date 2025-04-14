package com.compliant.api;

import com.compliant.application.ComplaintApplicationService;
import com.compliant.application.dto.ComplaintDto;
import com.compliant.application.dto.ComplaintUpdateDto;
import com.compliant.infrastructure.country.CountryClient;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ComplaintControllerTest {

    private static final Long COMPLAINT_ID = 100L;
    private static final String IP_ADDRESS = "192.168.0.1";
    private static final String COUNTRY_CODE = "US";

    private ComplaintApplicationService applicationService;
    private CountryClient countryClient;
    private HttpServletRequest request;

    private ComplaintController controller;

    @BeforeEach
    void setUp() {
        applicationService = mock(ComplaintApplicationService.class);
        countryClient = mock(CountryClient.class);
        request = mock(HttpServletRequest.class);
        controller = new ComplaintController(applicationService, countryClient);
    }

    @Test
    void shouldCreateComplaintAndReturnResponse() {
        // given
        ComplaintDto inputDto = new ComplaintDto();
        ComplaintDto resultDto = new ComplaintDto();
        resultDto.setCountry(COUNTRY_CODE);

        when(request.getRemoteAddr()).thenReturn(IP_ADDRESS);
        when(countryClient.getCountryCode(IP_ADDRESS)).thenReturn(COUNTRY_CODE);
        when(applicationService.createComplaint(inputDto)).thenReturn(resultDto);

        // when
        ResponseEntity<ComplaintDto> response = controller.createComplain(request, inputDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(resultDto);
        verify(request).getRemoteAddr();
        verify(countryClient).getCountryCode(IP_ADDRESS);
        verify(applicationService).createComplaint(inputDto);
        assertThat(inputDto.getCountry()).isEqualTo(COUNTRY_CODE);
    }

    @Test
    void shouldUpdateComplaintAndReturnResponse() {
        // given
        ComplaintUpdateDto inputDto = new ComplaintUpdateDto();
        ComplaintDto resultDto = new ComplaintDto();

        when(applicationService.updateComplaint(COMPLAINT_ID, inputDto)).thenReturn(resultDto);

        // when
        ResponseEntity<ComplaintDto> response = controller.updateComplaint(COMPLAINT_ID, inputDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(resultDto);
        verify(applicationService).updateComplaint(COMPLAINT_ID, inputDto);
    }

    @Test
    void shouldGetComplaintAndReturnResponse() {
        // given
        ComplaintDto resultDto = new ComplaintDto();

        when(applicationService.getComplaint(COMPLAINT_ID)).thenReturn(resultDto);

        // when
        ResponseEntity<ComplaintDto> response = controller.getComplaint(COMPLAINT_ID);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(resultDto);
        verify(applicationService).getComplaint(COMPLAINT_ID);
    }
}
