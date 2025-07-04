package com.cawa.demo;

//import org.springframework.stereotype.Component;
//
//public class async_retry {
//}

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import io.camunda.zeebe.spring.client.annotation.VariablesAsType;
import io.camunda.zeebe.spring.common.exception.ZeebeBpmnError;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
//import io.camunda.zeebe.spring.client.exception.ZeebeBpmnError;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Component
public class async_retry {
    private final RestTemplate restTemplate = new RestTemplate();


    @JobWorker(type = "heart-beat")
    public void heartbeat(){

        System.out.println("checking heart beat");
        try{
            long id = 1L;
            String url = "http://localhost:8081/orders/heartbeat";
            System.out.println("checking heart beat1");
            String userJson = restTemplate.getForObject(url, String.class,id);
            System.out.println("User JSON : " + userJson);
            System.out.println("checking heart beat2");
        } catch(RestClientException e){
            throw new ZeebeBpmnError("app_error","Application not working"+e.getMessage(),null);
        }
    }
    @JobWorker(type = "retry")
    public void retry(){

        System.out.println("retrying jobs");
        try{
//            long id = 1L;
//            String url = "http://localhost:8081/orders/retry-failed";
//            String userJson = restTemplate.getForObject(url, String.class,id);
//            System.out.println("User JSON : " + userJson);
        Long id = 1L;
        String url = "http://localhost:8081/orders/retry-failed";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Long> request = new HttpEntity<>(id, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        System.out.println("Response: " + response.getBody());

        } catch(RestClientException e){
            throw new ZeebeBpmnError("retry_error","RETRY CALL FAILED"+e.getMessage(),null);
        }
    }
}
