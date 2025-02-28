package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/car")
public class CarController extends AbstractCrudController<Car, String> {

    public CarController(CarService carService) {
        super(carService,
                "car",              // Entity name
                "CreateCar",        // Create view
                "CarList",          // List view
                "EditCar",          // Edit view
                "redirect:/car/list", // Redirect URL
                Car::new);          // Supplier for a new Car instance
    }
}
