package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import id.ac.ui.cs.advprog.eshop.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class CarServiceImpl extends AbstractCrudService<Car, String> implements CarService {

    private final CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    protected CrudRepository<Car, String> getRepository() {
        return carRepository;
    }
}
