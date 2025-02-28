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
import static org.mockito.ArgumentMatchers.any;
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
    void testCreatePage() {
        String viewName = carController.createPage(model);
        assertEquals("CreateCar", viewName);
        verify(model).addAttribute(eq("car"), any(Car.class));
    }

    @Test
    void testCreatePost() {
        Car car = new Car();
        String viewName = carController.createPost(car);
        assertEquals("redirect:/car/list", viewName);
        verify(carService).create(car);
    }

    @Test
    void testListPage() {
        List<Car> carList = Arrays.asList(new Car(), new Car());
        when(carService.findAll()).thenReturn(carList);

        String viewName = carController.listPage(model);
        assertEquals("CarList", viewName);
        verify(model).addAttribute("cars", carList);
        verify(carService).findAll();
    }

    @Test
    void testEditPage() {
        String carId = "123";
        Car car = new Car();
        when(carService.findById(carId)).thenReturn(car);

        String viewName = carController.editPage(carId, model);
        assertEquals("EditCar", viewName);
        verify(model).addAttribute("car", car);
        verify(carService).findById(carId);
    }

    @Test
    void testEditPost() {
        Car car = new Car();
        String viewName = carController.editPost(car);
        assertEquals("redirect:/car/list", viewName);
        verify(carService).update(car);
    }

    @Test
    void testDelete() {
        String carId = "123";
        String viewName = carController.delete(carId);
        assertEquals("redirect:/car/list", viewName);
        verify(carService).deleteById(carId);
    }
}
