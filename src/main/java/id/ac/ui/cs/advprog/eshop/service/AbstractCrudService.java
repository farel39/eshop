package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.repository.CrudRepository;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractCrudService<T, I> implements CrudService<T, I> {

    // Each concrete service must supply its own repository
    protected abstract CrudRepository<T, I> getRepository();

    @Override
    public T create(T entity) {
        getRepository().create(entity);
        return entity;
    }

    @Override
    public List<T> findAll() {
        Iterator<T> iterator = getRepository().findAll();
        List<T> allEntities = new ArrayList<>();
        iterator.forEachRemaining(allEntities::add);
        return allEntities;
    }

    @Override
    public T findById(I id) {
        return getRepository().findById(id);
    }

    @Override
    public T update(T entity) {
        getRepository().update(entity);
        return entity;
    }

    @Override
    public void deleteById(I id) {
        getRepository().deleteById(id);
    }
}
