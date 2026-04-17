package com.example.franchiseapi.service;

import com.example.franchiseapi.application.BranchPersistencePort;
import com.example.franchiseapi.application.FranchisePersistencePort;
import com.example.franchiseapi.application.FranchiseWriteUseCase;
import com.example.franchiseapi.application.ProductPersistencePort;
import com.example.franchiseapi.dto.BranchResponse;
import com.example.franchiseapi.dto.CreateBranchRequest;
import com.example.franchiseapi.dto.CreateFranchiseRequest;
import com.example.franchiseapi.dto.CreateProductRequest;
import com.example.franchiseapi.dto.FranchiseResponse;
import com.example.franchiseapi.dto.ProductResponse;
import com.example.franchiseapi.dto.UpdateNameRequest;
import com.example.franchiseapi.dto.UpdateProductStockRequest;
import com.example.franchiseapi.mapper.FranchiseMapper;
import com.example.franchiseapi.model.Branch;
import com.example.franchiseapi.model.Franchise;
import com.example.franchiseapi.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class FranchiseWriteService implements FranchiseWriteUseCase {

    private final FranchisePersistencePort franchisePersistencePort;
    private final BranchPersistencePort branchPersistencePort;
    private final ProductPersistencePort productPersistencePort;

    public FranchiseWriteService(
            FranchisePersistencePort franchisePersistencePort,
            BranchPersistencePort branchPersistencePort,
            ProductPersistencePort productPersistencePort
    ) {
        this.franchisePersistencePort = franchisePersistencePort;
        this.branchPersistencePort = branchPersistencePort;
        this.productPersistencePort = productPersistencePort;
    }

    @Override
    public Mono<FranchiseResponse> createFranchise(CreateFranchiseRequest request) {
        Franchise franchise = Franchise.builder()
                .nombre(request.nombre().trim())
                .build();

        return franchisePersistencePort.save(franchise)
                .map(FranchiseMapper::toResponse);
    }

    @Override
    public Mono<BranchResponse> createBranch(Long franchiseId, CreateBranchRequest request) {
        return requireFranchise(franchiseId)
                .flatMap(franchise -> branchPersistencePort.save(Branch.builder()
                        .nombre(request.nombre().trim())
                        .franchiseId(franchise.getId())
                        .build()))
                .map(FranchiseMapper::toResponse);
    }

    @Override
    public Mono<ProductResponse> createProduct(Long franchiseId, Long branchId, CreateProductRequest request) {
        return requireBranch(franchiseId, branchId)
                .flatMap(branch -> productPersistencePort.save(Product.builder()
                        .nombre(request.nombre().trim())
                        .stock(request.stock())
                        .branchId(branch.getId())
                        .build()))
                .map(FranchiseMapper::toResponse);
    }

    @Override
    public Mono<Void> deleteProduct(Long franchiseId, Long branchId, Long productId) {
        return requireBranch(franchiseId, branchId)
                .flatMap(branch -> productPersistencePort.findByIdAndBranchId(productId, branch.getId())
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"))))
                .flatMap(productPersistencePort::delete);
    }

    @Override
    public Mono<ProductResponse> updateProductStock(
            Long franchiseId,
            Long branchId,
            Long productId,
            UpdateProductStockRequest request
    ) {
        return requireBranch(franchiseId, branchId)
                .flatMap(branch -> productPersistencePort.findByIdAndBranchId(productId, branch.getId())
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"))))
                .flatMap(product -> {
                    product.setStock(request.stock());
                    return productPersistencePort.save(product);
                })
                .map(FranchiseMapper::toResponse);
    }

    @Override
    public Mono<FranchiseResponse> updateFranchiseName(Long franchiseId, UpdateNameRequest request) {
        return requireFranchise(franchiseId)
                .flatMap(franchise -> {
                    franchise.setNombre(request.nombre().trim());
                    return franchisePersistencePort.save(franchise);
                })
                .map(FranchiseMapper::toResponse);
    }

    @Override
    public Mono<BranchResponse> updateBranchName(Long franchiseId, Long branchId, UpdateNameRequest request) {
        return requireBranch(franchiseId, branchId)
                .flatMap(branch -> {
                    branch.setNombre(request.nombre().trim());
                    return branchPersistencePort.save(branch);
                })
                .map(FranchiseMapper::toResponse);
    }

    @Override
    public Mono<ProductResponse> updateProductName(Long franchiseId, Long branchId, Long productId, UpdateNameRequest request) {
        return requireBranch(franchiseId, branchId)
                .flatMap(branch -> productPersistencePort.findByIdAndBranchId(productId, branch.getId())
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"))))
                .flatMap(product -> {
                    product.setNombre(request.nombre().trim());
                    return productPersistencePort.save(product);
                })
                .map(FranchiseMapper::toResponse);
    }

    private Mono<Franchise> requireFranchise(Long franchiseId) {
        return franchisePersistencePort.findById(franchiseId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Franchise not found")));
    }

    private Mono<Branch> requireBranch(Long franchiseId, Long branchId) {
        return requireFranchise(franchiseId)
                .flatMap(franchise -> branchPersistencePort.findByIdAndFranchiseId(branchId, franchise.getId())
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found"))));
    }
}
