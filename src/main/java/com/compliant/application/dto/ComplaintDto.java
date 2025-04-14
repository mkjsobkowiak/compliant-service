package com.compliant.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintDto {

    private Long id;
    @NotBlank
    private String content;
    @NotBlank
    private String productId;
    private LocalDateTime createdAt;
    @NotBlank
    private String reportedBy;
    private String country;
    private int submissionCount;

}