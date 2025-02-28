package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Identifiable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public abstract class AbstractRepository<T extends Identifiable<String>> implements CrudRepository<T, String> {
    protected List<T> data = new ArrayList<>();

    @Override
    public T create(T entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }
        data.add(entity);
        return entity;
    }

    @Override
    public Iterator<T> findAll() {
        return data.iterator();
    }

    @Override
    public T findById(String id) {
        for (T entity : data) {
            if (entity.getId().equals(id)) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public T update(T updatedEntity) {
        for (int i = 0; i < data.size(); i++) {
            T entity = data.get(i);
            if (entity.getId().equals(updatedEntity.getId())) {
                data.set(i, updatedEntity);
                return updatedEntity;
            }
        }
        return null;
    }

    @Override
    public void deleteById(String id) {
        data.removeIf(entity -> entity.getId().equals(id));
    }
}
