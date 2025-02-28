package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CarControllerTest {

    @Mock
    private CarService carService;

    @Mock
    private Model model;

    @InjectMocks
    private CarController carController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCarPage() {
        String viewName = carController.createCarPage(model);
        assertEquals("CreateCar", viewName);
        verify(model).addAttribute(eq("car"), any(Car.class));
    }

    @Test
    void testCreateCarPost() {
        Car car = new Car();
        String viewName = carController.createCarPost(car);
        assertEquals("redirect:/car/list", viewName);
        verify(carService).create(car);
    }

    @Test
    void testCarListPage() {
        List<Car> carList = Arrays.asList(new Car(), new Car());
        when(carService.findAll()).thenReturn(carList);

        String viewName = carController.carListPage(model);
        assertEquals("CarList", viewName);
        verify(model).addAttribute("cars", carList);
        verify(carService).findAll();
    }

    @Test
    void testEditCarPage() {
        String carId = "123";
        Car car = new Car();
        when(carService.findById(carId)).thenReturn(car);

        String viewName = carController.editCarPage(carId, model);
        assertEquals("EditCar", viewName);
        verify(model).addAttribute("car", car);
        verify(carService).findById(carId);
    }

    @Test
    void testEditCarPost() {
        Car car = new Car();
        String viewName = carController.editCarPost(car);
        assertEquals("redirect:/car/list", viewName);
        verify(carService).update(car);
    }

    @Test
    void testDeleteCar() {
        String carId = "123";
        String viewName = carController.deleteCar(carId);
        assertEquals("redirect:/car/list", viewName);
        verify(carService).deleteById(carId);
    }
}
