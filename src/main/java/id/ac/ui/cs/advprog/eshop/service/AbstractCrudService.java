package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.repository.CrudRepository;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractCrudService<T, ID> implements CrudService<T, ID> {

    // Each concrete service must supply its own repository
    protected abstract CrudRepository<T, ID> getRepository();

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
    public T findById(ID id) {
        return getRepository().findById(id);
    }

    @Override
    public T update(T entity) {
        getRepository().update(entity);
        return entity;
    }

    @Override
    public void deleteById(ID id) {
        getRepository().deleteById(id);
    }
}
