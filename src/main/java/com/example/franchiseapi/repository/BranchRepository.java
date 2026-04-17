package com.example.franchiseapi.repository;

import com.example.franchiseapi.model.Branch;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository extends ReactiveCrudRepository<Branch, Long> {

    Mono<Branch> findByIdAndFranchiseId(Long id, Long franchiseId);

    Flux<Branch> findAllByFranchiseId(Long franchiseId);
}
