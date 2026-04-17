package com.example.franchiseapi.application;

import com.example.franchiseapi.model.Product;
import reactor.core.publisher.Mono;

public interface ProductPersistencePort {

    Mono<Product> save(Product product);

    Mono<Product> findByIdAndBranchId(Long id, Long branchId);

    Mono<Void> delete(Product product);
}
