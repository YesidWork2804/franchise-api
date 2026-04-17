package com.example.franchiseapi.infrastructure.persistence;

import com.example.franchiseapi.model.Franchise;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SpringDataFranchiseRepository extends ReactiveCrudRepository<Franchise, Long> {
}
