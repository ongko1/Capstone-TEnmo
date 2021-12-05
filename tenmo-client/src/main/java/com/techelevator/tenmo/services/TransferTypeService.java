package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferTypeService {
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();

    public TransferTypeService(String url) {
        this.baseUrl = url;
    }


    public TransferType getTransferType(AuthenticatedUser authenticatedUser, String description) {
        TransferType transferType = null;

        try {
            String url = baseUrl + "/transfertype/filter?description=" + description;
            transferType = restTemplate.exchange(url, HttpMethod.GET, makeEntity(authenticatedUser), TransferType.class).getBody();
        } catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Server network issue. Please try again.");
        }
        return transferType;
    }

    public TransferType getTransferTypeFromId(AuthenticatedUser authenticatedUser, int transferTypeId) {
        TransferType transferType = null;

        try {
            String url = baseUrl + "transfertype/" + transferTypeId;
            transferType = restTemplate.exchange(url, HttpMethod.GET, makeEntity(authenticatedUser), TransferType.class).getBody();
        } catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Server network issue. Please try again.");
        }


        return transferType;
    }

    private HttpEntity makeEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity entity = new HttpEntity(headers);
        return entity;
    }
}
