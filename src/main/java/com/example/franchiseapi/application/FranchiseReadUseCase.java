package com.example.franchiseapi.application;

import com.example.franchiseapi.dto.TopProductResponse;
import reactor.core.publisher.Flux;

public interface FranchiseReadUseCase {

    Flux<TopProductResponse> getTopProducts(Long franchiseId);
}
