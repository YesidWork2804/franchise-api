package com.example.franchiseapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.franchiseapi.application.BranchPersistencePort;
import com.example.franchiseapi.application.FranchisePersistencePort;
import com.example.franchiseapi.application.ProductPersistencePort;
import com.example.franchiseapi.dto.CreateFranchiseRequest;
import com.example.franchiseapi.dto.CreateProductRequest;
import com.example.franchiseapi.dto.UpdateProductStockRequest;
import com.example.franchiseapi.model.Branch;
import com.example.franchiseapi.model.Franchise;
import com.example.franchiseapi.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class FranchiseWriteServiceTest {

    @Mock
    private FranchisePersistencePort franchisePersistencePort;

    @Mock
    private BranchPersistencePort branchPersistencePort;

    @Mock
    private ProductPersistencePort productPersistencePort;

    @InjectMocks
    private FranchiseWriteService franchiseWriteService;

    private Franchise franchise;
    private Branch branch;
    private Product product;

    @BeforeEach
    void setUp() {
        franchise = Franchise.builder()
                .id(1L)
                .nombre("Franquicia Centro")
                .build();

        branch = Branch.builder()
                .id(2L)
                .nombre("Sucursal Norte")
                .franchiseId(1L)
                .build();

        product = Product.builder()
                .id(3L)
                .nombre("Laptop")
                .stock(10)
                .branchId(2L)
                .build();
    }

    @Test
    void createFranchiseShouldTrimNameAndReturnResponse() {
        when(franchisePersistencePort.save(any(Franchise.class))).thenAnswer(invocation -> {
            Franchise franchiseToSave = invocation.getArgument(0);
            franchiseToSave.setId(1L);
            return Mono.just(franchiseToSave);
        });

        StepVerifier.create(franchiseWriteService.createFranchise(new CreateFranchiseRequest("  Franquicia Demo  ")))
                .assertNext(response -> {
                    assertEquals(1L, response.id());
                    assertEquals("Franquicia Demo", response.nombre());
                })
                .verifyComplete();

        ArgumentCaptor<Franchise> captor = ArgumentCaptor.forClass(Franchise.class);
        verify(franchisePersistencePort).save(captor.capture());
        assertEquals("Franquicia Demo", captor.getValue().getNombre());
    }

    @Test
    void createProductShouldReturnNotFoundWhenBranchDoesNotExist() {
        when(franchisePersistencePort.findById(1L)).thenReturn(Mono.just(franchise));
        when(branchPersistencePort.findByIdAndFranchiseId(99L, 1L)).thenReturn(Mono.empty());

        StepVerifier.create(franchiseWriteService.createProduct(1L, 99L, new CreateProductRequest("Mouse", 5)))
                .expectErrorSatisfies(error -> {
                    ResponseStatusException exception = (ResponseStatusException) error;
                    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
                    assertEquals("Branch not found", exception.getReason());
                })
                .verify();
    }

    @Test
    void updateProductStockShouldUpdateExistingProductStock() {
        when(franchisePersistencePort.findById(1L)).thenReturn(Mono.just(franchise));
        when(branchPersistencePort.findByIdAndFranchiseId(2L, 1L)).thenReturn(Mono.just(branch));
        when(productPersistencePort.findByIdAndBranchId(3L, 2L)).thenReturn(Mono.just(product));
        when(productPersistencePort.save(any(Product.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(franchiseWriteService.updateProductStock(1L, 2L, 3L, new UpdateProductStockRequest(25)))
                .assertNext(response -> {
                    assertEquals(3L, response.id());
                    assertEquals(25, response.stock());
                    assertEquals("Laptop", response.nombre());
                })
                .verifyComplete();

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productPersistencePort).save(captor.capture());
        assertEquals(25, captor.getValue().getStock());
    }

    @Test
    void deleteProductShouldReturnNotFoundWhenProductDoesNotExist() {
        when(franchisePersistencePort.findById(1L)).thenReturn(Mono.just(franchise));
        when(branchPersistencePort.findByIdAndFranchiseId(2L, 1L)).thenReturn(Mono.just(branch));
        when(productPersistencePort.findByIdAndBranchId(3L, 2L)).thenReturn(Mono.empty());

        StepVerifier.create(franchiseWriteService.deleteProduct(1L, 2L, 3L))
                .expectErrorSatisfies(error -> {
                    ResponseStatusException exception = (ResponseStatusException) error;
                    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
                    assertEquals("Product not found", exception.getReason());
                })
                .verify();
    }
}
