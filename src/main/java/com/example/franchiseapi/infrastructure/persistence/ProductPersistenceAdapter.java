package com.example.franchiseapi.infrastructure.persistence;

import com.example.franchiseapi.application.ProductPersistencePort;
import com.example.franchiseapi.model.Product;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ProductPersistenceAdapter implements ProductPersistencePort {

    private final SpringDataProductRepository repository;

    public ProductPersistenceAdapter(SpringDataProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Product> save(Product product) {
        return repository.save(product);
    }

    @Override
    public Mono<Product> findByIdAndBranchId(Long id, Long branchId) {
        return repository.findByIdAndBranchId(id, branchId);
    }

    @Override
    public Mono<Void> delete(Product product) {
        return repository.delete(product);
    }
}
