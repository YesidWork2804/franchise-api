package com.example.franchiseapi.mapper;

import com.example.franchiseapi.dto.BranchResponse;
import com.example.franchiseapi.dto.FranchiseResponse;
import com.example.franchiseapi.dto.ProductResponse;
import com.example.franchiseapi.model.Branch;
import com.example.franchiseapi.model.Franchise;
import com.example.franchiseapi.model.Product;

public final class FranchiseMapper {

    private FranchiseMapper() {
    }

    public static FranchiseResponse toResponse(Franchise franchise) {
        return new FranchiseResponse(franchise.getId(), franchise.getNombre());
    }

    public static BranchResponse toResponse(Branch branch) {
        return new BranchResponse(branch.getId(), branch.getNombre(), branch.getFranchiseId());
    }

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getNombre(), product.getStock(), product.getBranchId());
    }
}
