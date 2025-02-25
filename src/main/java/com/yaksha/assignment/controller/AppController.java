package com.yaksha.assignment.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AppController {

	private final RestTemplate restTemplate;

	public AppController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@GetMapping("/authenticateApi")
	public String authenticateApiRequest(@RequestParam String apiUrl, @RequestParam String username,
			@RequestParam String password) {

		// Encode username and password into Base64 format for basic authentication
		String auth = username + ":" + password;
		String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes());

		// Set the Authorization header for Basic Authentication
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Basic " + encodedAuth);

		// Send the GET request with authentication header
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

		return response.getBody();
	}
}
