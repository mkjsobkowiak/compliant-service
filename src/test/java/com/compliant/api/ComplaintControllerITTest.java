package com.compliant.api;

import com.compliant.application.dto.ComplaintDto;
import com.compliant.domain.ComplaintRepository;
import com.compliant.domain.model.Complaint;
import com.compliant.infrastructure.country.CountryClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ComplaintControllerITTest {

    private static final String BASE_PATH = "/api/v1/complaints";
    private static final String COUNTRY_CODE = "US";
    private static final String CLIENT_IP = "192.168.1.1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryClient countryClient;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        when(countryClient.getCountryCode(CLIENT_IP)).thenReturn(COUNTRY_CODE);
        complaintRepository.deleteAll();
    }

    @Test
    void shouldCreateComplaint() throws Exception {
        // given
        ComplaintDto requestDto = new ComplaintDto();
        requestDto.setProductId("123");
        requestDto.setReportedBy("1@test.com");
        requestDto.setContent("Broken item");

        // when / then
        mockMvc.perform(post(BASE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(req -> {
                            req.setRemoteAddr(CLIENT_IP);
                            return req;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Broken item"))
                .andExpect(jsonPath("$.country").value(COUNTRY_CODE));

        var saved = complaintRepository.findByProductIdAndReportedBy("123", "1@test.com");
        assertThat(saved).isPresent();
        assertThat(saved.get().getSubmissionCount()).isEqualTo(1);
    }

    @Test
    void shouldUpdateComplaint() throws Exception {
        // given
        Complaint savedComplaint = Complaint.builder()
                .productId("123")
                .reportedBy("1@test.com")
                .content("Old content")
                .createdAt(LocalDateTime.now())
                .country("UK")
                .build();

        savedComplaint = complaintRepository.save(savedComplaint);

        ComplaintDto updateDto = new ComplaintDto();
        updateDto.setContent("Updated content");

        // when / then
        mockMvc.perform(put(BASE_PATH + "/" + savedComplaint.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Updated content"));

        var updated = complaintRepository.findById(savedComplaint.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getContent()).isEqualTo("Updated content");
    }

    @Test
    void shouldGetComplaint() throws Exception {
        // given
        Complaint savedComplaint = Complaint.builder()
                .productId("456")
                .reportedBy("1@test.com")
                .content("Missing item")
                .createdAt(LocalDateTime.now())
                .country("UK")
                .build();

        savedComplaint = complaintRepository.save(savedComplaint);

        // when / then
        mockMvc.perform(get(BASE_PATH + "/" + savedComplaint.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value("456"))
                .andExpect(jsonPath("$.reportedBy").value("1@test.com"));
    }
}
