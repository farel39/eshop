package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.service.CrudService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.function.Supplier;

public abstract class AbstractCrudController<T, I> {

    protected final CrudService<T, I> service;
    private final String entityName;
    private final String createView;
    private final String listView;
    private final String editView;
    private final String redirectListUrl;
    private final Supplier<T> entitySupplier;

    protected AbstractCrudController(CrudService<T, I> service,
                                     String entityName,
                                     String createView,
                                     String listView,
                                     String editView,
                                     String redirectListUrl,
                                     Supplier<T> entitySupplier) {
        this.service = service;
        this.entityName = entityName;
        this.createView = createView;
        this.listView = listView;
        this.editView = editView;
        this.redirectListUrl = redirectListUrl;
        this.entitySupplier = entitySupplier;
    }

    @GetMapping("/create")
    public String createPage(Model model) {
        model.addAttribute(entityName, entitySupplier.get());
        return createView;
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute T entity) {
        service.create(entity);
        return redirectListUrl;
    }

    @GetMapping("/list")
    public String listPage(Model model) {
        // Assume pluralizing by appending "s". Adjust if needed.
        model.addAttribute(entityName + "s", service.findAll());
        return listView;
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable("id") I id, Model model) {
        T entity = service.findById(id);
        model.addAttribute(entityName, entity);
        return editView;
    }

    @PostMapping("/edit")
    public String editPost(@ModelAttribute T entity) {
        service.update(entity);
        return redirectListUrl;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") I id) {
        service.deleteById(id);
        return redirectListUrl;
    }
}
