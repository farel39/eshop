package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductController extends AbstractCrudController<Product, String> {

    public ProductController(ProductService productService) {
        super(productService);
    }

    @Override
    protected String getEntityName() {
        return "product";
    }

    @Override
    protected String getCreateView() {
        return "CreateProduct";
    }

    @Override
    protected String getListView() {
        return "ProductList";
    }

    @Override
    protected String getEditView() {
        return "EditProduct";
    }

    @Override
    protected String getRedirectListUrl() {
        return "redirect:/product/list";
    }

    @Override
    protected Product createNewEntity() {
        return new Product();
    }
}
