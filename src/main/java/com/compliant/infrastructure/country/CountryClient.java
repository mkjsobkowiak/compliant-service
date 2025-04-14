package com.compliant.infrastructure.country;

import com.compliant.infrastructure.country.dto.CountryDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class CountryClient {

    private static final String COUNTRY_CODE_URL = "https://api.country.is/";
    private final RestTemplate restTemplate = new RestTemplate();

    @CircuitBreaker(name = "countryApiCircuitBreaker", fallbackMethod = "fallback")
    public String getCountryCode(String ip) {
        String url = COUNTRY_CODE_URL + ip;
        CountryDto response = restTemplate.getForObject(url, CountryDto.class);
        return response != null ? response.country() : "UNKNOWN";
    }

    public String fallback(String ip, Throwable t) {
        log.warn("Fallback for country API called due to: {}, {}", ip, t.getMessage());
        return "UNKNOWN";
    }
}