package com.capstone.apistry.utils;

import org.springframework.http.ResponseEntity;

public class ResponseFormatter {

    private static String getReasonPhrase(int statusCode) {
        return switch (statusCode) {
            case 100 -> "Continue";
            case 101 -> "Switching Protocols";
            case 102 -> "Processing";
            case 200 -> "OK";
            case 201 -> "Created";
            case 202 -> "Accepted";
            case 204 -> "No Content";
            case 301 -> "Moved Permanently";
            case 302 -> "Found";
            case 304 -> "Not Modified";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            case 409 -> "Conflict";
            case 415 -> "Unsupported Media Type";
            case 429 -> "Too Many Requests";
            case 500 -> "Internal Server Error";
            case 502 -> "Bad Gateway";
            case 503 -> "Service Unavailable";
            default -> "Unknown";
        };
    }

    public static String formatStatus(ResponseEntity<String> response) {
        int status = response.getStatusCode().value();
        return "Status: " + status + " " + getReasonPhrase(status);
    }

    public static String formatHeaders(ResponseEntity<String> response) {
        return "Headers: " + response.getHeaders().toString();
    }

    public static String formatBody(ResponseEntity<String> response) {
        return "Body: " + response.getBody();
    }
}