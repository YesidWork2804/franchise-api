package com.example.franchiseapi.infrastructure.persistence;

import com.example.franchiseapi.application.FranchisePersistencePort;
import com.example.franchiseapi.model.Franchise;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class FranchisePersistenceAdapter implements FranchisePersistencePort {

    private final SpringDataFranchiseRepository repository;

    public FranchisePersistenceAdapter(SpringDataFranchiseRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return repository.save(franchise);
    }

    @Override
    public Mono<Franchise> findById(Long id) {
        return repository.findById(id);
    }
}
