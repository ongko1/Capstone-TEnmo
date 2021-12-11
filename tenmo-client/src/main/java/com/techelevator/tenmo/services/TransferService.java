package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.view.ConsoleService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import static com.techelevator.tenmo.TenmoConstants.*;


public class TransferService {
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private ConsoleService console = new ConsoleService(System.in, System.out);

    public TransferService(String url) {
        this.baseUrl = url;
    }

    public String createTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity(transfer, headers);

        String url = baseUrl + "/transfers/" + transfer.getTransferId();
        String message = "";
        if(transfer.getTransferTypeId()==TRANSFER_TYPE_SEND) {
            message = "Your transfer is complete.";
        }
        else {
            message = "Your request is complete";
        }

        try {
            restTemplate.exchange(url, HttpMethod.POST, entity, Transfer.class);
        } catch(RestClientResponseException e) {
            if (e.getMessage().contains("Insufficient Funds")) {
                message="You don't have enough money for that transaction.";
            } else {
                message="Could not complete request. Code: " + e.getRawStatusCode();
            }
        } catch(ResourceAccessException e) {
            message="Server network issue. Please try again.";
        }
        return message;
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

    public String updateTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity(transfer, headers);

        String url = baseUrl + "/transfers/" + transfer.getTransferId();
        String message = "Your transaction is complete";

        try {
            restTemplate.exchange(url, HttpMethod.PUT, entity, Transfer.class);
        } catch(RestClientResponseException e) {
            if (e.getMessage().contains("Insufficient Funds")) {
                message = "You don't have enough money for that transaction.";
            } else {
                message = "Could not complete request. Code: " + e.getRawStatusCode();
            }
        } catch(ResourceAccessException e) {
            message = "Server network issue, please try again.";
        }
        return message;
    }

}
