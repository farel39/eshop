package id.ac.ui.cs.advprog.eshop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class HomeControllerTest {

    @Test
    void testHomePage() {
        // Arrange
        HomeController homeController = new HomeController();

        // Act
        String viewName = homeController.homePage();

        // Assert
        assertEquals("HomePage", viewName);
    }
}