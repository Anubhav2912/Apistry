package com.capstone.apistry.utils;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResponseErrorHandler;
import java.io.IOException;

import java.util.Map;

@Component
public class HttpClientUtil {

    private final RestTemplate restTemplate = new RestTemplate();

    public HttpClientUtil() {
        // Treat all HTTP statuses as non-errors so we can capture body and status
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(org.springframework.http.client.ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(org.springframework.http.client.ClientHttpResponse response) throws IOException {
                // No-op; errors are handled by inspecting status codes in callers
            }
        });
    }

    public ResponseEntity<String> sendRequest(
            String url,
            HttpMethod method,
            Map<String, String> headers,
            String body
    ) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            headers.forEach(httpHeaders::add);
        }

        HttpEntity<String> entity = new HttpEntity<>(body, httpHeaders);
        return restTemplate.exchange(url, method, entity, String.class);
    }
}
