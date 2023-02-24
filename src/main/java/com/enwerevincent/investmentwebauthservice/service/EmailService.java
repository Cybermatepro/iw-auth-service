package com.enwerevincent.investmentwebauthservice.service;

import com.enwerevincent.invest.vo.AppResponse;
import com.enwerevincent.invest.vo.MailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final RestTemplate restTemplate;

    @Async
    public AppResponse<Boolean> sendMail(MailRequest request){
        return restTemplate.postForObject("http://iw-messenger/mail" , getWrappedPayload(request) , AppResponse.class);
    }

    private HttpEntity<MailRequest> getWrappedPayload(MailRequest mailRequest){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MailRequest> entity = new HttpEntity<>(mailRequest , headers);
        return entity;
    }
}
