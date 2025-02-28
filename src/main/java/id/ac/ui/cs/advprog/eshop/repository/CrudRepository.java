package id.ac.ui.cs.advprog.eshop.repository;

import java.util.Iterator;

public interface CrudRepository<T, I> {
    T create(T entity);
    Iterator<T> findAll();
    T findById(I id);
    T update(T entity);
    void deleteById(I id);
}
