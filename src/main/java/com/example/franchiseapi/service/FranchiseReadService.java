package com.example.franchiseapi.service;

import com.example.franchiseapi.application.FranchisePersistencePort;
import com.example.franchiseapi.application.FranchiseReadUseCase;
import com.example.franchiseapi.application.TopProductQueryPort;
import com.example.franchiseapi.dto.TopProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FranchiseReadService implements FranchiseReadUseCase {

    private final FranchisePersistencePort franchisePersistencePort;
    private final TopProductQueryPort topProductQueryPort;

    public FranchiseReadService(FranchisePersistencePort franchisePersistencePort, TopProductQueryPort topProductQueryPort) {
        this.franchisePersistencePort = franchisePersistencePort;
        this.topProductQueryPort = topProductQueryPort;
    }

    @Override
    public Flux<TopProductResponse> getTopProducts(Long franchiseId) {
        return franchisePersistencePort.findById(franchiseId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Franchise not found")))
                .flatMapMany(franchise -> topProductQueryPort.findTopProductsByFranchiseId(franchiseId));
    }
}
