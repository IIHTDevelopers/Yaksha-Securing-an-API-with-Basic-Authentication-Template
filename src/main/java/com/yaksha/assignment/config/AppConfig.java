package com.yaksha.assignment.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	// Custom error handler
	private static class CustomErrorHandler extends DefaultResponseErrorHandler {
		@Override
		public boolean hasError(ClientHttpResponse response) throws IOException {
			// Handle only 4xx and 5xx status codes
			return (((HttpStatus) response.getStatusCode()).series() == HttpStatus.Series.CLIENT_ERROR
					|| ((HttpStatus) response.getStatusCode()).series() == HttpStatus.Series.SERVER_ERROR);
		}

		@Override
		public void handleError(ClientHttpResponse response) throws IOException {
			// Don't throw exceptions for 404 (Not Found) errors
			if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
				// Do nothing for 404, or you could log it if needed
				return;
			}
			// For other error codes, call the default error handling
			super.handleError(response);
		}
	}
}
