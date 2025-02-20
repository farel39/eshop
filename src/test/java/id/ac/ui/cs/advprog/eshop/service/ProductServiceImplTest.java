package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("1");
        product.setProductName("Test Product");
    }

    @Test
    void testCreate() {
        when(productRepository.create(product)).thenReturn(product);
        Product createdProduct = productService.create(product);
        assertNotNull(createdProduct);
        assertEquals("1", createdProduct.getProductId());
        verify(productRepository, times(1)).create(product);
    }

    @Test
    void testFindAll() {
        Iterator<Product> productIterator = Arrays.asList(product).iterator();
        when(productRepository.findAll()).thenReturn(productIterator);

        List<Product> products = productService.findAll();

        assertEquals(1, products.size());
        assertEquals("1", products.get(0).getProductId());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(productRepository.findById("1")).thenReturn(product);
        Product foundProduct = productService.findById("1");
        assertNotNull(foundProduct);
        assertEquals("1", foundProduct.getProductId());
        verify(productRepository, times(1)).findById("1");
    }

    @Test
    void testUpdate() {
        when(productRepository.update(product)).thenReturn(product);
        Product updatedProduct = productService.update(product);
        assertNotNull(updatedProduct);
        assertEquals("1", updatedProduct.getProductId());
        verify(productRepository, times(1)).update(product);
    }

    @Test
    void testDeleteById() {
        doNothing().when(productRepository).deleteById("1");
        productService.deleteById("1");
        verify(productRepository, times(1)).deleteById("1");
    }
}
