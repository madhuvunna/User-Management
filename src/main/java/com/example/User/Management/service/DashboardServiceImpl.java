package com.example.User.Management.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.User.Management.dto.QuoteApiResponseDTO;

public class DashboardServiceImpl implements DashboardService {

	private String quoteApiURL = "https://dummyjson.com/quotes/random";

	@Override
	public QuoteApiResponseDTO getQuote() {

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<QuoteApiResponseDTO> responseEntity = restTemplate.getForEntity(quoteApiURL,
				QuoteApiResponseDTO.class);
		QuoteApiResponseDTO body = responseEntity.getBody();
		return body;
	}

}
