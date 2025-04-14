package com.compliant.api;

import com.compliant.application.ComplaintApplicationService;
import com.compliant.application.dto.ComplaintDto;
import com.compliant.application.dto.ComplaintUpdateDto;
import com.compliant.infrastructure.country.CountryClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintApplicationService applicationService;
    private final CountryClient countryClient;

    @PostMapping
    public ResponseEntity<ComplaintDto> createComplain(HttpServletRequest httpServletRequest, @Valid @RequestBody ComplaintDto complaintDto) {
        String countryCode = countryClient.getCountryCode(httpServletRequest.getRemoteAddr());
        complaintDto.setCountry(countryCode);
        ComplaintDto createdComplaint = applicationService.createComplaint(complaintDto);
        return ResponseEntity.ok(createdComplaint);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComplaintDto> updateComplaint(@PathVariable Long id, @Valid @RequestBody ComplaintUpdateDto complaintDto) {
        ComplaintDto updatedComplaint = applicationService.updateComplaint(id, complaintDto);
        return ResponseEntity.ok(updatedComplaint);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplaintDto> getComplaint(@PathVariable Long id) {
        ComplaintDto ComplaintDto = applicationService.getComplaint(id);
        return ResponseEntity.ok(ComplaintDto);
    }

}
