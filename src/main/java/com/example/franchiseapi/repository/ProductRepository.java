package com.example.franchiseapi.repository;

import com.example.franchiseapi.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

    Mono<Product> findByIdAndBranchId(Long id, Long branchId);

    Flux<Product> findAllByBranchId(Long branchId);
}
