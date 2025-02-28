package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private Model model;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePage() {
        String viewName = productController.createPage(model);
        assertEquals("CreateProduct", viewName);
        verify(model).addAttribute(eq("product"), any(Product.class));
    }

    @Test
    void testCreatePost() {
        Product product = new Product();
        String viewName = productController.createPost(product);
        assertEquals("redirect:/product/list", viewName);
        verify(productService).create(product);
    }

    @Test
    void testListPage() {
        List<Product> productList = Arrays.asList(new Product(), new Product());
        when(productService.findAll()).thenReturn(productList);

        String viewName = productController.listPage(model);
        assertEquals("ProductList", viewName);
        verify(model).addAttribute("products", productList);
        verify(productService).findAll();
    }

    @Test
    void testEditPage() {
        String productId = "123";
        Product product = new Product();
        when(productService.findById(productId)).thenReturn(product);

        String viewName = productController.editPage(productId, model);
        assertEquals("EditProduct", viewName);
        verify(model).addAttribute("product", product);
        verify(productService).findById(productId);
    }

    @Test
    void testEditPost() {
        Product product = new Product();
        String viewName = productController.editPost(product);
        assertEquals("redirect:/product/list", viewName);
        verify(productService).update(product);
    }

    @Test
    void testDelete() {
        String productId = "123";
        String viewName = productController.delete(productId);
        assertEquals("redirect:/product/list", viewName);
        verify(productService).deleteById(productId);
    }
}
