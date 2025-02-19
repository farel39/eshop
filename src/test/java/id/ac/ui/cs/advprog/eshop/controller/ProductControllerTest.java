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
    void testCreateProductPage() {
        String viewName = productController.createProductPage(model);
        assertEquals("createProduct", viewName);
        verify(model).addAttribute(eq("product"), any(Product.class));
    }

    @Test
    void testCreateProductPost() {
        Product product = new Product();
        String viewName = productController.createProductPost(product, model);
        assertEquals("redirect:list", viewName);
        verify(productService).create(product);
    }

    @Test
    void testProductListPage() {
        List<Product> productList = Arrays.asList(new Product(), new Product());
        when(productService.findAll()).thenReturn(productList);

        String viewName = productController.productListPage(model);
        assertEquals("productList", viewName);
        verify(model).addAttribute("products", productList);
        verify(productService).findAll();
    }

    @Test
    void testEditProductPage() {
        String productId = "123";
        Product product = new Product();
        when(productService.findById(productId)).thenReturn(product);

        String viewName = productController.editProductPage(productId, model);
        assertEquals("editProduct", viewName);
        verify(model).addAttribute("product", product);
        verify(productService).findById(productId);
    }

    @Test
    void testEditProductPost() {
        Product product = new Product();
        String viewName = productController.editProductPost(product, model);
        assertEquals("redirect:list", viewName);
        verify(productService).update(product);
    }

    @Test
    void testDeleteProduct() {
        String productId = "123";
        String viewName = productController.deleteProduct(productId);
        assertEquals("redirect:/product/list", viewName);
        verify(productService).deleteById(productId);
    }
}