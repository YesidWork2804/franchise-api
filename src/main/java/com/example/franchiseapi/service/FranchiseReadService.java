package com.example.franchiseapi.service;

import com.example.franchiseapi.dto.TopProductResponse;
import com.example.franchiseapi.repository.FranchiseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FranchiseReadService {

    private static final String TOP_PRODUCTS_SQL = """
            SELECT p.nombre AS nombre_producto,
                   p.stock AS stock,
                   b.nombre AS nombre_sucursal
            FROM product p
            INNER JOIN branch b ON b.id = p.branch_id
            INNER JOIN (
                SELECT p2.branch_id, MAX(p2.stock) AS max_stock
                FROM product p2
                INNER JOIN branch b2 ON b2.id = p2.branch_id
                WHERE b2.franchise_id = :franchiseId
                GROUP BY p2.branch_id
            ) top_products ON top_products.branch_id = p.branch_id
                         AND top_products.max_stock = p.stock
            WHERE b.franchise_id = :franchiseId
            ORDER BY b.id, p.id
            """;

    private final FranchiseRepository franchiseRepository;
    private final DatabaseClient databaseClient;

    public FranchiseReadService(FranchiseRepository franchiseRepository, DatabaseClient databaseClient) {
        this.franchiseRepository = franchiseRepository;
        this.databaseClient = databaseClient;
    }

    public Flux<TopProductResponse> getTopProducts(Long franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Franchise not found")))
                .flatMapMany(franchise -> databaseClient.sql(TOP_PRODUCTS_SQL)
                        .bind("franchiseId", franchiseId)
                        .map((row, metadata) -> new TopProductResponse(
                                row.get("nombre_producto", String.class),
                                row.get("stock", Integer.class),
                                row.get("nombre_sucursal", String.class)
                        ))
                        .all());
    }
}
