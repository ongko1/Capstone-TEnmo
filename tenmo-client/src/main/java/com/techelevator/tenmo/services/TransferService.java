package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();


    public TransferService(String url) {
        this.baseUrl = url;
    }

    public void createTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity(transfer, headers);

        String url = baseUrl + "/transfers/" + transfer.getTransferId();

        try {
            restTemplate.exchange(url, HttpMethod.POST, entity, Transfer.class);
        } catch(RestClientResponseException e) {
            if (e.getMessage().contains("Insufficient Funds")) {
                System.out.println("You don't have enough money for that transaction.");
            } else {
                System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
            }
        } catch(ResourceAccessException e) {
            System.out.println("Server network issue. Please try again.");
        }
    }

    public Transfer[] getTransfersFromUserId(AuthenticatedUser authenticatedUser, int userId) {
        Transfer[] transfers = null;
        try {
            transfers = restTemplate.exchange(baseUrl + "/transfers/user/" + userId,
                    HttpMethod.GET,
                    makeEntity(authenticatedUser),
                    Transfer[].class
            ).getBody();
        } catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Server network issue. Please try again.");
        }
        return transfers;
    }

    private HttpEntity makeEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity entity = new HttpEntity(headers);
        return entity;
    }

    public Transfer[] getPendingTransfersByUserId(AuthenticatedUser authenticatedUser) {
        Transfer[] transfers = null;
        try {
            transfers = restTemplate.exchange(baseUrl + "/transfers/user/" + authenticatedUser.getUser().getId() + "/pending",
                    HttpMethod.GET,
                    makeEntity(authenticatedUser),
                    Transfer[].class
            ).getBody();
        } catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Server network issue. Please try again.");
        }
        return transfers;
    }

    public void updateTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity(transfer, headers);

        String url = baseUrl + "/transfers/" + transfer.getTransferId();

        try {
            restTemplate.exchange(url, HttpMethod.PUT, entity, Transfer.class);
        } catch(RestClientResponseException e) {
            if (e.getMessage().contains("Insufficient Funds")) {
                System.out.println("You don't have enough money for that transaction.");
            } else {
                System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
            }
        } catch(ResourceAccessException e) {
            System.out.println("Server network issue. Please try again.");
        }
    }

}
