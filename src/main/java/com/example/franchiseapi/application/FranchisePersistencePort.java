package com.example.franchiseapi.application;

import com.example.franchiseapi.model.Franchise;
import reactor.core.publisher.Mono;

public interface FranchisePersistencePort {

    Mono<Franchise> save(Franchise franchise);

    Mono<Franchise> findById(Long id);
}
