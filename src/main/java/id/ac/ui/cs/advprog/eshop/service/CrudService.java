package id.ac.ui.cs.advprog.eshop.service;

import java.util.List;

public interface CrudService<T, I> {
    T create(T entity);
    T findById(I id);
    List<T> findAll();
    T update(T entity);
    void deleteById(I id);
}
