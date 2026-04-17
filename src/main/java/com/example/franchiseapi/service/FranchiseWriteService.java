package com.example.franchiseapi.service;

import com.example.franchiseapi.dto.BranchResponse;
import com.example.franchiseapi.dto.CreateBranchRequest;
import com.example.franchiseapi.dto.CreateFranchiseRequest;
import com.example.franchiseapi.dto.CreateProductRequest;
import com.example.franchiseapi.dto.FranchiseResponse;
import com.example.franchiseapi.dto.ProductResponse;
import com.example.franchiseapi.dto.UpdateNameRequest;
import com.example.franchiseapi.dto.UpdateProductStockRequest;
import com.example.franchiseapi.model.Branch;
import com.example.franchiseapi.model.Franchise;
import com.example.franchiseapi.model.Product;
import com.example.franchiseapi.repository.BranchRepository;
import com.example.franchiseapi.repository.FranchiseRepository;
import com.example.franchiseapi.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class FranchiseWriteService {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public FranchiseWriteService(
            FranchiseRepository franchiseRepository,
            BranchRepository branchRepository,
            ProductRepository productRepository
    ) {
        this.franchiseRepository = franchiseRepository;
        this.branchRepository = branchRepository;
        this.productRepository = productRepository;
    }

    public Mono<FranchiseResponse> createFranchise(CreateFranchiseRequest request) {
        Franchise franchise = Franchise.builder()
                .nombre(request.nombre().trim())
                .build();

        return franchiseRepository.save(franchise)
                .map(saved -> new FranchiseResponse(saved.getId(), saved.getNombre()));
    }

    public Mono<BranchResponse> createBranch(Long franchiseId, CreateBranchRequest request) {
        return requireFranchise(franchiseId)
                .flatMap(franchise -> branchRepository.save(Branch.builder()
                        .nombre(request.nombre().trim())
                        .franchiseId(franchise.getId())
                        .build()))
                .map(saved -> new BranchResponse(saved.getId(), saved.getNombre(), saved.getFranchiseId()));
    }

    public Mono<ProductResponse> createProduct(Long franchiseId, Long branchId, CreateProductRequest request) {
        return requireBranch(franchiseId, branchId)
                .flatMap(branch -> productRepository.save(Product.builder()
                        .nombre(request.nombre().trim())
                        .stock(request.stock())
                        .branchId(branch.getId())
                        .build()))
                .map(saved -> new ProductResponse(saved.getId(), saved.getNombre(), saved.getStock(), saved.getBranchId()));
    }

    public Mono<Void> deleteProduct(Long franchiseId, Long branchId, Long productId) {
        return requireBranch(franchiseId, branchId)
                .flatMap(branch -> productRepository.findByIdAndBranchId(productId, branch.getId())
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"))))
                .flatMap(productRepository::delete);
    }

    public Mono<ProductResponse> updateProductStock(
            Long franchiseId,
            Long branchId,
            Long productId,
            UpdateProductStockRequest request
    ) {
        return requireBranch(franchiseId, branchId)
                .flatMap(branch -> productRepository.findByIdAndBranchId(productId, branch.getId())
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"))))
                .flatMap(product -> {
                    product.setStock(request.stock());
                    return productRepository.save(product);
                })
                .map(saved -> new ProductResponse(saved.getId(), saved.getNombre(), saved.getStock(), saved.getBranchId()));
    }

    public Mono<FranchiseResponse> updateFranchiseName(Long franchiseId, UpdateNameRequest request) {
        return requireFranchise(franchiseId)
                .flatMap(franchise -> {
                    franchise.setNombre(request.nombre().trim());
                    return franchiseRepository.save(franchise);
                })
                .map(saved -> new FranchiseResponse(saved.getId(), saved.getNombre()));
    }

    public Mono<BranchResponse> updateBranchName(Long franchiseId, Long branchId, UpdateNameRequest request) {
        return requireBranch(franchiseId, branchId)
                .flatMap(branch -> {
                    branch.setNombre(request.nombre().trim());
                    return branchRepository.save(branch);
                })
                .map(saved -> new BranchResponse(saved.getId(), saved.getNombre(), saved.getFranchiseId()));
    }

    public Mono<ProductResponse> updateProductName(Long franchiseId, Long branchId, Long productId, UpdateNameRequest request) {
        return requireBranch(franchiseId, branchId)
                .flatMap(branch -> productRepository.findByIdAndBranchId(productId, branch.getId())
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"))))
                .flatMap(product -> {
                    product.setNombre(request.nombre().trim());
                    return productRepository.save(product);
                })
                .map(saved -> new ProductResponse(saved.getId(), saved.getNombre(), saved.getStock(), saved.getBranchId()));
    }

    private Mono<Franchise> requireFranchise(Long franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Franchise not found")));
    }

    private Mono<Branch> requireBranch(Long franchiseId, Long branchId) {
        return requireFranchise(franchiseId)
                .flatMap(franchise -> branchRepository.findByIdAndFranchiseId(branchId, franchise.getId())
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found"))));
    }
}
