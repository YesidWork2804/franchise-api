package com.example.franchiseapi.controller;

import com.example.franchiseapi.dto.ApiResponse;
import com.example.franchiseapi.dto.BranchResponse;
import com.example.franchiseapi.dto.CreateBranchRequest;
import com.example.franchiseapi.dto.CreateFranchiseRequest;
import com.example.franchiseapi.dto.CreateProductRequest;
import com.example.franchiseapi.dto.FranchiseResponse;
import com.example.franchiseapi.dto.ProductResponse;
import com.example.franchiseapi.dto.TopProductResponse;
import com.example.franchiseapi.dto.UpdateNameRequest;
import com.example.franchiseapi.dto.UpdateProductStockRequest;
import com.example.franchiseapi.service.FranchiseReadService;
import com.example.franchiseapi.service.FranchiseWriteService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franchises")
public class FranchiseController {

    private final FranchiseReadService franchiseReadService;
    private final FranchiseWriteService franchiseWriteService;

    public FranchiseController(
            FranchiseReadService franchiseReadService,
            FranchiseWriteService franchiseWriteService
    ) {
        this.franchiseReadService = franchiseReadService;
        this.franchiseWriteService = franchiseWriteService;
    }

    @PostMapping
    public Mono<ResponseEntity<ApiResponse<FranchiseResponse>>> createFranchise(
            @Valid @RequestBody CreateFranchiseRequest request
    ) {
        return franchiseWriteService.createFranchise(request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.success("Franchise created successfully", response)));
    }

    @PatchMapping("/{id}")
    public Mono<ResponseEntity<ApiResponse<FranchiseResponse>>> updateFranchiseName(
            @PathVariable Long id,
            @Valid @RequestBody UpdateNameRequest request
    ) {
        return franchiseWriteService.updateFranchiseName(id, request)
                .map(response -> ResponseEntity.ok(ApiResponse.success("Franchise name updated successfully", response)));
    }

    @PostMapping("/{id}/branches")
    public Mono<ResponseEntity<ApiResponse<BranchResponse>>> createBranch(
            @PathVariable Long id,
            @Valid @RequestBody CreateBranchRequest request
    ) {
        return franchiseWriteService.createBranch(id, request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.success("Branch created successfully", response)));
    }

    @PatchMapping("/{id}/branches/{branchId}")
    public Mono<ResponseEntity<ApiResponse<BranchResponse>>> updateBranchName(
            @PathVariable Long id,
            @PathVariable Long branchId,
            @Valid @RequestBody UpdateNameRequest request
    ) {
        return franchiseWriteService.updateBranchName(id, branchId, request)
                .map(response -> ResponseEntity.ok(ApiResponse.success("Branch name updated successfully", response)));
    }

    @PostMapping("/{id}/branches/{branchId}/products")
    public Mono<ResponseEntity<ApiResponse<ProductResponse>>> createProduct(
            @PathVariable Long id,
            @PathVariable Long branchId,
            @Valid @RequestBody CreateProductRequest request
    ) {
        return franchiseWriteService.createProduct(id, branchId, request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.success("Product created successfully", response)));
    }

    @DeleteMapping("/{id}/branches/{branchId}/products/{productId}")
    public Mono<ResponseEntity<ApiResponse<Void>>> deleteProduct(
            @PathVariable Long id,
            @PathVariable Long branchId,
            @PathVariable Long productId
    ) {
        return franchiseWriteService.deleteProduct(id, branchId, productId)
                .thenReturn(ResponseEntity.ok(ApiResponse.<Void>success("Product deleted successfully", null)));
    }

    @PatchMapping("/{id}/branches/{branchId}/products/{productId}/stock")
    public Mono<ResponseEntity<ApiResponse<ProductResponse>>> updateProductStock(
            @PathVariable Long id,
            @PathVariable Long branchId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductStockRequest request
    ) {
        return franchiseWriteService.updateProductStock(id, branchId, productId, request)
                .map(response -> ResponseEntity.ok(ApiResponse.success("Product stock updated successfully", response)));
    }

    @PatchMapping("/{id}/branches/{branchId}/products/{productId}")
    public Mono<ResponseEntity<ApiResponse<ProductResponse>>> updateProductName(
            @PathVariable Long id,
            @PathVariable Long branchId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateNameRequest request
    ) {
        return franchiseWriteService.updateProductName(id, branchId, productId, request)
                .map(response -> ResponseEntity.ok(ApiResponse.success("Product name updated successfully", response)));
    }

    @GetMapping("/{id}/top-products")
    public Mono<ResponseEntity<ApiResponse<List<TopProductResponse>>>> getTopProducts(@PathVariable Long id) {
        return franchiseReadService.getTopProducts(id)
                .collectList()
                .map(response -> ResponseEntity.ok(ApiResponse.success("Top products retrieved successfully", response)));
    }
}
