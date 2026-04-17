package com.example.franchiseapi.infrastructure.persistence;

import com.example.franchiseapi.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface SpringDataProductRepository extends ReactiveCrudRepository<Product, Long> {

    Mono<Product> findByIdAndBranchId(Long id, Long branchId);
}
