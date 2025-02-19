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
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {

        Iterator<Product> productIterator = productRepository.findAll();

        assertFalse(productIterator.hasNext());

    }



    @Test

    void testFindAllIfMoreThanOneProduct() {

        Product product1 = new Product();

        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");

        product1.setProductName("Sampo Cap Bambang");

        product1.setProductQuantity(100);

        productRepository.create(product1);



        Product product2 = new Product();

        product2.setProductId("a0f9e46-90b1-437d-a0bf-d0821dde9096");

        product2.setProductName("Sampo Cap Usep");

        product2.setProductQuantity(50);

        productRepository.create(product2);



        Iterator<Product> productIterator = productRepository.findAll();

        assertTrue(productIterator.hasNext());

        Product savedProduct = productIterator.next();

        assertEquals(product1.getProductId(), savedProduct.getProductId());

        savedProduct = productIterator.next();

        assertEquals(product2.getProductId(), savedProduct.getProductId());

        assertFalse(productIterator.hasNext());

    }

    @Test
    void testUpdateProduct() {
        ProductRepository productRepository = new ProductRepository();
        Product product = new Product();
        product.setProductName("Test Product");
        product.setProductQuantity(10);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId(product.getProductId());
        updatedProduct.setProductName("Updated Product");
        updatedProduct.setProductQuantity(20);

        Product result = productRepository.update(updatedProduct);

        assertNotNull(result);
        assertEquals("Updated Product", result.getProductName());
        assertEquals(20, result.getProductQuantity());

        Product retrievedProduct = productRepository.findById(product.getProductId());
        assertNotNull(retrievedProduct);
        assertEquals("Updated Product", retrievedProduct.getProductName());
        assertEquals(20, retrievedProduct.getProductQuantity());
    }

    @Test
    void testUpdateNonExistentProduct() {
        ProductRepository productRepository = new ProductRepository();
        Product product = new Product();
        product.setProductName("Test Product");
        product.setProductQuantity(10);
        productRepository.create(product);

        Product nonExistentProduct = new Product();
        nonExistentProduct.setProductId("non-existent-id");
        nonExistentProduct.setProductName("Non Existent Product");
        nonExistentProduct.setProductQuantity(30);

        Product result = productRepository.update(nonExistentProduct);

        assertNull(result);
    }

    @Test
    void testDeleteProduct() {
        ProductRepository productRepository = new ProductRepository();
        Product product = new Product();
        product.setProductName("Test Product");
        product.setProductQuantity(10);
        productRepository.create(product);

        productRepository.deleteById(product.getProductId());

        Product retrievedProduct = productRepository.findById(product.getProductId());
        assertNull(retrievedProduct);

        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testDeleteNonExistentProduct() {
        ProductRepository productRepository = new ProductRepository();
        Product product = new Product();
        product.setProductName("Test Product");
        product.setProductQuantity(10);
        productRepository.create(product);

        productRepository.deleteById("non-existent-id");

        Product retrievedProduct = productRepository.findById(product.getProductId());
        assertNotNull(retrievedProduct);
    }

    @Test
    void testFindByIdWhenProductDoesNotExist() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product foundProduct = productRepository.findById("non-existent-id");

        assertNull(foundProduct);
    }



}
