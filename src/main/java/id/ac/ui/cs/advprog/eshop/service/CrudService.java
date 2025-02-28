package id.ac.ui.cs.advprog.eshop.service;

import java.util.List;

public interface CrudService<T, ID> {
    T create(T entity);
    T findById(ID id);
    List<T> findAll();
    T update(T entity);
    void deleteById(ID id);
}

