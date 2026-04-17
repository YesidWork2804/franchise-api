package com.example.franchiseapi.infrastructure.persistence;

import com.example.franchiseapi.application.BranchPersistencePort;
import com.example.franchiseapi.model.Branch;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class BranchPersistenceAdapter implements BranchPersistencePort {

    private final SpringDataBranchRepository repository;

    public BranchPersistenceAdapter(SpringDataBranchRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Branch> save(Branch branch) {
        return repository.save(branch);
    }

    @Override
    public Mono<Branch> findByIdAndFranchiseId(Long id, Long franchiseId) {
        return repository.findByIdAndFranchiseId(id, franchiseId);
    }
}
