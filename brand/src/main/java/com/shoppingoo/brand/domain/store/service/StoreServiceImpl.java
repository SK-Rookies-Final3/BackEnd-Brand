package com.shoppingoo.brand.domain.store.service;

import com.shoppingoo.brand.db.store.Store;
import com.shoppingoo.brand.db.store.StoreRepository;
import com.shoppingoo.brand.domain.store.dto.StoreRequest;
import com.shoppingoo.brand.domain.store.dto.StoreResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public StoreServiceImpl(StoreRepository storeRepository, RestTemplate restTemplate) {
        this.storeRepository = storeRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public StoreResponse storeRegister(int userId, StoreRequest storeRequest) {

        // 매장 등록
        Store store = new Store();
        store.setUserId(userId);
        store.setName(storeRequest.getName());
        store.setLicenseNumber(storeRequest.getLicenseNumber());
        store.setLogoUrl(storeRequest.getLogoUrl());

        Store savedStore = storeRepository.save(store);

        // StoreResponse 객체 생성 및 반환
        return new StoreResponse();
    }

    private boolean userLogin(String authorization) {
        String url = "http://localhost:8080/open-api/user/login"; // 로그인 API URL

        // 로그인 요청 객체
        LoginRequest loginRequest = new LoginRequest(authorization);

        // POST 요청 보내기
        ResponseEntity<String> response = restTemplate.postForEntity(url, loginRequest, String.class);

        // 응답을 기반으로 인증 성공 여부 판단
        return response.getStatusCode().is2xxSuccessful(); // 2xx 상태 코드 확인
    }

    // LoginRequest DTO
    private static class LoginRequest {
        private String authorization;

        public LoginRequest(String authorization) {
            this.authorization = authorization;
        }

        public String getAuthorization() {
            return authorization;
        }

        public void setAuthorization(String authorization) {
            this.authorization = authorization;
        }
    }

    public List<StoreResponse> getAllStores() {
        return storeRepository.findAll().stream() // Store 리스트를 스트림으로 변환
                .map(store -> new StoreResponse( // 각 Store를 StoreResponse로 변환
                        store.getId(),
                        store.getUserId(),
                        store.getName(),
                        store.getLicenseNumber(),
                        store.getLogoUrl()))
                .collect(Collectors.toList()); // 리스트로 수집
    }







}
