package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {

    Car car;

    @BeforeEach
    void setUp() {
        this.car = new Car();
        this.car.setId("abc123-def456-ghi789");
        this.car.setName("Toyota Supra");
        this.car.setQuantity(5);
        this.car.setColor("Red");
    }

    @Test
    void testGetCarId() {
        assertEquals("abc123-def456-ghi789", this.car.getId());
    }

    @Test
    void testGetCarName() {
        assertEquals("Toyota Supra", this.car.getName());
    }

    @Test
    void testGetCarQuantity() {
        assertEquals(5, this.car.getQuantity());
    }

    @Test
    void testGetCarColor() {
        assertEquals("Red", this.car.getColor());
    }
}
