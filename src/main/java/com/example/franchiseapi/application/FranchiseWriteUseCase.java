package com.example.franchiseapi.application;

import com.example.franchiseapi.dto.BranchResponse;
import com.example.franchiseapi.dto.CreateBranchRequest;
import com.example.franchiseapi.dto.CreateFranchiseRequest;
import com.example.franchiseapi.dto.CreateProductRequest;
import com.example.franchiseapi.dto.FranchiseResponse;
import com.example.franchiseapi.dto.ProductResponse;
import com.example.franchiseapi.dto.UpdateNameRequest;
import com.example.franchiseapi.dto.UpdateProductStockRequest;
import reactor.core.publisher.Mono;

public interface FranchiseWriteUseCase {

    Mono<FranchiseResponse> createFranchise(CreateFranchiseRequest request);

    Mono<BranchResponse> createBranch(Long franchiseId, CreateBranchRequest request);

    Mono<ProductResponse> createProduct(Long franchiseId, Long branchId, CreateProductRequest request);

    Mono<Void> deleteProduct(Long franchiseId, Long branchId, Long productId);

    Mono<ProductResponse> updateProductStock(Long franchiseId, Long branchId, Long productId, UpdateProductStockRequest request);

    Mono<FranchiseResponse> updateFranchiseName(Long franchiseId, UpdateNameRequest request);

    Mono<BranchResponse> updateBranchName(Long franchiseId, Long branchId, UpdateNameRequest request);

    Mono<ProductResponse> updateProductName(Long franchiseId, Long branchId, Long productId, UpdateNameRequest request);
}
