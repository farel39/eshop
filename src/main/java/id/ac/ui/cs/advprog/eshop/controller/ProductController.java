package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductController extends AbstractCrudController<Product, String> {

    public ProductController(ProductService productService) {
        super(productService,
                "product",              // Entity name
                "CreateProduct",        // Create view
                "ProductList",          // List view
                "EditProduct",          // Edit view
                "redirect:/product/list", // Redirect URL
                Product::new);          // Supplier for a new Product instance
    }
}
