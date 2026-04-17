package com.example.franchiseapi.application;

import com.example.franchiseapi.model.Branch;
import reactor.core.publisher.Mono;

public interface BranchPersistencePort {

    Mono<Branch> save(Branch branch);

    Mono<Branch> findByIdAndFranchiseId(Long id, Long franchiseId);
}
