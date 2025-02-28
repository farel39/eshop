package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        // No setup required for now
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setName("Sampo Cap Bambang");
        product.setQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getId(), savedProduct.getId());
        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getQuantity(), savedProduct.getQuantity());
    }

    @Test
    void testFindAllIfEmpty() {

        Iterator<Product> productIterator = productRepository.findAll();

        assertFalse(productIterator.hasNext());

    }



    @Test

    void testFindAllIfMoreThanOneProduct() {

        Product product1 = new Product();

        product1.setId("eb558e9f-1c39-460e-8860-71af6af63bd6");

        product1.setName("Sampo Cap Bambang");

        product1.setQuantity(100);

        productRepository.create(product1);



        Product product2 = new Product();

        product2.setId("a0f9e46-90b1-437d-a0bf-d0821dde9096");

        product2.setName("Sampo Cap Usep");

        product2.setQuantity(50);

        productRepository.create(product2);



        Iterator<Product> productIterator = productRepository.findAll();

        assertTrue(productIterator.hasNext());

        Product savedProduct = productIterator.next();

        assertEquals(product1.getId(), savedProduct.getId());

        savedProduct = productIterator.next();

        assertEquals(product2.getId(), savedProduct.getId());

        assertFalse(productIterator.hasNext());

    }

    @Test
    void testUpdateProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setQuantity(10);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setId(product.getId());
        updatedProduct.setName("Updated Product");
        updatedProduct.setQuantity(20);

        Product result = productRepository.update(updatedProduct);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        assertEquals(20, result.getQuantity());

        Product retrievedProduct = productRepository.findById(product.getId());
        assertNotNull(retrievedProduct);
        assertEquals("Updated Product", retrievedProduct.getName());
        assertEquals(20, retrievedProduct.getQuantity());
    }

    @Test
    void testUpdateNonExistentProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setQuantity(10);
        productRepository.create(product);

        Product nonExistentProduct = new Product();
        nonExistentProduct.setId("non-existent-id");
        nonExistentProduct.setName("Non Existent Product");
        nonExistentProduct.setQuantity(30);

        Product result = productRepository.update(nonExistentProduct);

        assertNull(result);
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setQuantity(10);
        productRepository.create(product);

        productRepository.deleteById(product.getId());

        Product retrievedProduct = productRepository.findById(product.getId());
        assertNull(retrievedProduct);

        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testDeleteNonExistentProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setQuantity(10);
        productRepository.create(product);

        productRepository.deleteById("non-existent-id");

        Product retrievedProduct = productRepository.findById(product.getId());
        assertNotNull(retrievedProduct);
    }

    @Test
    void testFindByIdWhenProductDoesNotExist() {
        Product product = new Product();
        product.setId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setName("Sampo Cap Bambang");
        product.setQuantity(100);
        productRepository.create(product);

        Product foundProduct = productRepository.findById("non-existent-id");

        assertNull(foundProduct);
    }



}
