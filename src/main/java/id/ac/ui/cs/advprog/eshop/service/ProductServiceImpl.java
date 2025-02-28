package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import id.ac.ui.cs.advprog.eshop.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends AbstractCrudService<Product, String> implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    protected CrudRepository<Product, String> getRepository() {
        return productRepository;
    }
}
