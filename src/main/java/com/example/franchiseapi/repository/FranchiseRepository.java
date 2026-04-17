package com.example.franchiseapi.repository;

import com.example.franchiseapi.model.Franchise;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FranchiseRepository extends ReactiveCrudRepository<Franchise, Long> {
}
