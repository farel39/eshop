package id.ac.ui.cs.advprog.eshop.repository;

import java.util.Iterator;

public interface CrudRepository<T, ID> {
    T create(T entity);
    Iterator<T> findAll();
    T findById(ID id);
    T update(T entity);
    void deleteById(ID id);
}

