package com.example.franchiseapi.infrastructure.persistence;

import com.example.franchiseapi.application.TopProductQueryPort;
import com.example.franchiseapi.dto.TopProductResponse;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class TopProductQueryAdapter implements TopProductQueryPort {

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

    private final DatabaseClient databaseClient;

    public TopProductQueryAdapter(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Flux<TopProductResponse> findTopProductsByFranchiseId(Long franchiseId) {
        return databaseClient.sql(TOP_PRODUCTS_SQL)
                .bind("franchiseId", franchiseId)
                .map((row, metadata) -> new TopProductResponse(
                        row.get("nombre_producto", String.class),
                        row.get("stock", Integer.class),
                        row.get("nombre_sucursal", String.class)
                ))
                .all();
    }
}
