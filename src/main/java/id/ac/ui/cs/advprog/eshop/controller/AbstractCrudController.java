package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.service.CrudService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

public abstract class AbstractCrudController<T, I> {

    protected final CrudService<T, I> service;

    protected AbstractCrudController(CrudService<T, I> service) {
        this.service = service;
    }

    // Abstract methods to be implemented by subclasses:
    protected abstract String getEntityName();      // e.g., "car" or "product"
    protected abstract String getCreateView();        // e.g., "CreateCar" or "CreateProduct"
    protected abstract String getListView();          // e.g., "CarList" or "ProductList"
    protected abstract String getEditView();          // e.g., "EditCar" or "EditProduct"
    protected abstract String getRedirectListUrl();   // e.g., "redirect:/car/list"
    protected abstract T createNewEntity();           // returns a new instance (e.g., new Car())

    @GetMapping("/create")
    public String createPage(Model model) {
        model.addAttribute(getEntityName(), createNewEntity());
        return getCreateView();
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute T entity) {
        service.create(entity);
        return getRedirectListUrl();
    }

    @GetMapping("/list")
    public String listPage(Model model) {
        model.addAttribute(getEntityName() + "s", service.findAll());
        return getListView();
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable("id") I id, Model model) {
        T entity = service.findById(id);
        model.addAttribute(getEntityName(), entity);
        return getEditView();
    }

    @PostMapping("/edit")
    public String editPost(@ModelAttribute T entity) {
        service.update(entity);
        return getRedirectListUrl();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") I id) {
        service.deleteById(id);
        return getRedirectListUrl();
    }
}
