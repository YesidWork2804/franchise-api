package com.example.franchiseapi.infrastructure.persistence;

import com.example.franchiseapi.model.Branch;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface SpringDataBranchRepository extends ReactiveCrudRepository<Branch, Long> {

    Mono<Branch> findByIdAndFranchiseId(Long id, Long franchiseId);
}
