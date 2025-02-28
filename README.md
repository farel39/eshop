- [Deployment URL](https://involved-renata-farel39-60683431.koyeb.app/)
- [Module 1 Reflection](#module-1)
- [Module 2 Reflection](#module-2)
- [Module 3 Reflection](#module-3)

# Module 1
## Reflection 1
Below is how i implement clean code and secure coding practices

### 1. Clean Code Principles
#### 1.1 Meaningful Names
Applied:

- Class names like Product, ProductRepository, ProductService, and ProductController are meaningful and describe their purpose.

- Method names like create, findAll, findById, update, and deleteById are clear and indicate their functionality.

- Variable names like productData, productId, productName, and productQuantity are descriptive.

#### 1.2 Functions
Applied:

- Methods in the ProductRepository, ProductService, and ProductController are small and focused on a single responsibility.

- The create, findAll, findById, update, and deleteById methods are concise and perform one task each.

#### 1.3 Comments
Applied:

- The code is mostly self-explanatory, so there are no unnecessary comments.

#### 1.4 Objects and Data Structures
Applied:

- The Product class is a simple data structure with getters and setters, adhering to the principles of encapsulation.

- The ProductRepository uses a List to store products, which is appropriate for this use case.


#### 1.5 Error Handling
Applied:

- The findById method in ProductRepository returns null if the product is not found, which is a simple way to handle missing data.

### 2. Secure Coding Practices
#### 2.1 Authentication
Applied:

- Not implemented in the current code.

#### 2.2 Authorization
Applied:

- Not implemented in the current code.

#### 2.3 Input Data Validation
Applied:

- Basic validation is performed in the HTML forms using th:field and input types like text and number.


#### 2.4 Output Data Encoding
Applied:

- Thymeleaf automatically encodes output data to prevent XSS (Cross-Site Scripting) attacks.

I did not find any mistakes in the source code but there could be improvements on it. This include implementing authentication, authorization, input data validation, etc.

## Reflection 2
### 1. Unit Test
#### How do i feel after writing the unit test?
- I feel more secure and safe about my code because unit test gives confidence that the code behaves as expected and helps catch issues early. It also forces me to think about edge cases and how my code might break.

#### How many unit tests should be made in a class?
- There’s no fixed number of unit tests for a class. It depends on the complexity of the class, number of methods, edge cases, and behavioral requirements. But as a rule of thumb, aim to test all public methods and all significant logic paths in your class

#### How to make sure that our unit tests are enough to verify our program?
- Test all scenarios
- Use Code Coverage Tools
- Check for Edge Cases
- Refactor Tests
- Peer Review

#### If you have 100% code coverage, does that mean your code has no bugs or errors?
- No, 100% code coverage does not guarantee bug-free code. Coverage Only Measures Execution. I might have 100% coverage but still miss edge cases or invalid inputs.
### 2. Functional Test

#### What do you think about the cleanliness of the code of the new functional test suite? Will the new code reduce the code quality? Identify the potential clean code issues, explain the reasons, and suggest possible improvements to make the code cleaner!

Issues:
- Code Duplication. It violates the DRY (Don't Repeat Yourself) principle, making the code harder to maintain. If the setup logic changes, you would need to update it in multiple places.
- It can make the test class harder to read and understand.
- It violates the Single Responsibility Principle (SRP)
- Causes Tight coupling that reduces flexibility and makes it harder to modify or extend the test suite in the future.

Improvements:
- Extract Common Setup Logic into a Base Class

```java
package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
public abstract class BaseFunctionalTest {

    @LocalServerPort
    protected int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    protected String testBaseUrl;

    protected String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }
}
```
Now, the CreateProductFunctionalTest and the new test suite can extend this base class like below
``` java
class CreateProductFunctionalTest extends BaseFunctionalTest {
    // Test methods
}

class ProductListFunctionalTest extends BaseFunctionalTest {
    // Test methods
}
```
- Use Helper Methods for Repeated Actions
```java
protected void createProduct(ChromeDriver driver, String productName, String quantity) {
    driver.get(baseUrl + "/product/create");
    driver.findElement(By.id("nameInput")).sendKeys(productName);
    driver.findElement(By.id("quantityInput")).sendKeys(quantity);
    driver.findElement(By.cssSelector("button[type='submit']")).click();
}
```
- Follow the Single Responsibility Principle (SRP)
- Use Descriptive Test Method Names
- Avoid Hardcoding Values

# Module 2
## 1. List the code quality issue(s) that you fixed during the exercise and explain your strategy on fixing them.

- Maintainability issue due to no default implementation in function. I fix it by adding nested comments.
```java
    @Test
void contextLoads() {
    // This method is intentionally left empty.
    // Nested Explanation:
    // - This is a standard test in Spring Boot applications.
    // - It verifies that the application context loads without any issues.
    // - If the application fails to start due to configuration issues, this test will fail.
}
```
```java
@BeforeEach
void setUp() {
    // No setup required for now;
}
```

- Maintainability issue due to variables getting hidden because I created a new object with the same name and type. I fixed it removing the new object and just using the old one. I deleted every line of code like the one below in ProductRepositoryTest.java

```java
   ProductRepository productRepository = new ProductRepository();
```

- Maintainability issue where a unit test doesn't use assertion in EshopApplicationTests.java. I fixed it by adding an assertion.

Previous code:
```java
@Test
void testMain() {
    SpringApplication mockSpringApp = mock(SpringApplication.class);
    EshopApplication.main(new String[]{});
}

```

New code:
```java
@Test
void testMain() {
    assertDoesNotThrow(() -> EshopApplication.main(new String[]{}));
}
```

- Maintainability issue where there is useless assignment to local variable "model". I fixed it by removing this line of code in HomeControllerTest.java
```java
Model model = mock(Model.class); // Mock the Model object
```

- Maintainability issue where there is field injection but instead use constructor injection instead. I fixed it by removing replacing the code below with a new one in ProductController.java and ProductServiceImpl.java

Previous code (ProductController.java):
```java
@Autowired
private ProductService service;
```

New code:
```java
private final ProductService service;

public ProductController(ProductService service) {
    this.service = service;
}

```

Previous code (ProductServiceImpl.java):
```java
@Autowired
private ProductRepository productRepository;
```

New code:
```java

private final ProductRepository productRepository;

public ProductServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
}
```


- Maintainability issue where there is unused import. I discard unused imports which are lines of codes below:
```java
// In CreateProductFunctionalTest.java
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;

// In EshopApplicationTests.java
import org.springframework.boot.SpringApplication;

// In HomeControllerTest.java
import org.springframework.ui.Model;
import static org.mockito.Mockito.mock;
```

- Maintainability issue where there is declaration of thrown exception, but it cannot be thrown in the method. I fixed it by removing "throw Exception" on every method below:

```java
// In CreateProductFunctionalTest.java
void createProduct_andVerifyInList(ChromeDriver driver) throws Exception {};

// HomePageFunctionalTest.java
void pageTitle_isCorrect(ChromeDriver driver) throws Exception {}
void welcomeMessage_homePage_isCorrect(ChromeDriver driver) throws Exception {}
```

- Maintainability issue where i shouldn't use lambda with method reference. I fixed it by changing the lines of code below in ProductListFunctionalTest.java

Previous code:
```java
driver.findElements(By.cssSelector("a[href^='/product/delete/']")).forEach(deleteButton -> {
        deleteButton.click();
        });
```

New code:
```java
driver.findElements(By.cssSelector("a[href^='/product/delete/']")).forEach(WebElement::click);
    }

```
- Maintainability issue where the comments shouldn't end with ";" as it indicate a commented line of code. I removed the ";" in ProductRepositoryTest.java.

Previous code:
```java
@BeforeEach
void setUp() {
    // No setup required for now;
}
```

New code:
```java
@BeforeEach
void setUp() {
    // No setup required for now
}
```

- Maintainability issue where i should group dependencies by their destination. I fixed it by grouping it.

## 2. Look at your CI/CD workflows (GitHub)/pipelines (GitLab). Do you think the current implementation has met the definition of Continuous Integration and Continuous Deployment? Explain the reasons (minimum 3 sentences)!
My current implementation meets the definition of Continuous Integration (CI) and Continuous Deployment (CD) for several reasons.

- Automated Testing and Code Quality Checks (CI): My workflow runs unit tests on every push and pull request, ensuring that new changes do not introduce regressions. Additionally, SonarCloud performs static code analysis to detect bugs, vulnerabilities, and code smells before deployment, reinforcing best practices in software quality.

- Security & Compliance Checks (CI): The OSSF Scorecard workflow regularly analyzes my repository for security risks and enforces branch protection, reducing the likelihood of supply chain attacks. This aligns with CI principles by continuously validating code integrity before merging changes.

- Automated Deployment on Main Branch Updates (CD): Since Koyeb automatically deploys new changes when updates are pushed to the main branch, my pipeline supports Continuous Deployment (CD). This means every approved and merged change is immediately available in production without manual intervention, ensuring fast and reliable releases.

- Infrastructure Automation (CD): By leveraging GitHub Actions to handle builds, tests, and security checks, combined with Koyeb’s automatic deployment, my pipeline minimizes human intervention. This ensures a seamless and scalable CI/CD workflow, enabling rapid iterations and reducing downtime.

# Module 3

## 1. Explain what principles you apply to your project!


---

### a. Single Responsibility Principle (SRP)

#### Overview
SRP states that every class or module should have only one reason to change. This leads to a clear separation of concerns and simplifies maintenance.

#### Application in My Project

- **Controller Layer:**  
  Each controller is dedicated for only handling HTTP requests for its specific domain.
    - **Before SRP:**
      ```java
      public class CarController extends ProductController {
          // Combined product and car-specific logic
      }
      ```
    - **After SRP:**
      ```java
      public class CarController {
          // Dedicated car-related request handling
      }
      ```

- **Service Layer:**  
  Service classes like `CarServiceImpl` and `ProductServiceImpl` only encapsulate business logic.


- **Repository Layer:**  
  Each repository (e.g., `CarRepository`, `ProductRepository`) only manages data persistence for its respective entity.


- **Model Layer:**  
  Model classes such as `Car` and `Product` define core data structures with minimal domain logic.


---

### b. Liskov Substitution Principle (LSP)
#### Overview
The Liskov Substitution Principle (LSP) requires that objects of a superclass should be replaceable with objects of a subclass without affecting the correctness of the program. In other words, every subclass must adhere to the contract defined by its superclass so that substituting the superclass with any subclass does not lead to unexpected behaviors or errors.

#### Application in My Project

- **Repository Layer:**
    - `AbstractRepository` defines a generic CRUD interface, establishing a clear contract for data operations.
    - Both `CarRepository` and `ProductRepository` extend `AbstractRepository` but don't change the basic CRUD operations (legacy methods), ensuring they can be substituted seamlessly in any context where an `AbstractRepository` is required.

- **Service Layer:**
    - `AbstractCrudService` offers standard CRUD operations and defines abstract methods that set a precise contract for data handling.
    - Concrete services (e.g., `CarServiceImpl` and `ProductServiceImpl`) use the standard CRUD operations provided by `AbstractCrudService` and simply implement the abstract `getRepository()` method, adhering strictly to its contract. This guarantees that any service using `AbstractCrudService` can reliably work with these concrete implementations, in line with the LSP.

- **Model Layer:**
    - The abstract `Item` class defines common properties (e.g., `id`, `name`, `quantity`), serving as a blueprint for all items in the system.
    - Subclasses (`Car` and `Product`) extend `Item` and implement these properties while preserving the behavior specified by `Item`. This allows objects of `Car` or `Product` to be used interchangeably with `Item`, fully satisfying the LSP.

- **Controller Layer:**
    - `AbstractCrudController` defines a generic controller with common CRUD endpoints and enforces a strict contract via abstract methods for view names, model attributes, and URL redirections.
    - Concrete controllers (e.g., `CarController` and `ProductController`) extend `AbstractCrudController` and implement the required abstract methods without altering the expected behavior of CRUD operations. This ensures that these controllers can be substituted wherever a generic CRUD controller is expected, thereby adhering to the LSP.



---


### c. Open/Closed Principle (OCP)

#### Overview
OCP states that software entities (classes, modules, functions, etc.) should be open for extension but closed for modification. In practice, this means that the behavior of a system can be extended without altering its existing source code.

#### Application in My Project

- **Repository Layer:**
    - `AbstractRepository` provides common CRUD functionality as a stable foundation.
    - Concrete repositories (e.g., `CarRepository` and `ProductRepository`) extend this base class, allowing them to introduce additional behavior without changing the core implementation.

- **Service Layer:**
    - `AbstractCrudService` offers default CRUD implementations that encapsulate standard operations.
    - Concrete service classes (e.g., `CarServiceImpl` and `ProductServiceImpl`) extend this service and supply their specific repository implementations without changing the core implementation of `AbstractCrudService`.

- **Model Layer:**
    - The `Item` class serves as a foundational structure for all entities, establishing a consistent base.
    - Subclasses like `Car` and `Product` extend `Item` and introduce specialized attributes (e.g., `Car` adds a `color` property). This approach lets me extend entity functionality in a modular way without impacting the base model, ensuring that enhancements or changes are isolated and manageable.

- **Controller Layer:**
    - The new `AbstractCrudController` encapsulates common CRUD behavior for controllers, providing a stable and extensible foundation.
    - Concrete controllers (e.g., `CarController` and `ProductController`) extend `AbstractCrudController` and implement abstract methods for specific behavior (such as view names, model attributes, and URL redirections) without altering the base controller logic. This design allows controllers to be extended with new functionality while keeping the core logic intact.

---

### d. Dependency Inversion Principle (DIP)

#### Overview
DIP advises that high-level modules should depend on abstractions rather than concrete implementations, reducing coupling and enhancing flexibility.

#### Application in My Project

- **Controller Layer Example:**
    - **Before DIP:**  
      The `CarController` depended on a concrete class (`CarServiceImpl`):
      ```java
      @Controller
      @RequestMapping("/car")
      public class CarController extends ProductController {
      
          private final CarServiceImpl carservice;
      
          public CarController(ProductService service, CarServiceImpl carservice) {
              super(service);
              this.carservice = carservice;
          }
          // Additional controller logic
      }
      ```
    - **After DIP:**  
      The constructor now accepts a `CarService` interface:
      ```java
      @Controller
      @RequestMapping("/car")
      public class CarController {
      
          private final CarService carService;
      
          public CarController(CarService carService) {
              this.carService = carService;
          }
          // Additional controller logic
      }
      ```

## 2. Advantages of Applying SOLID Principles to My Project

Applying SOLID principles brings significant benefits to the structure, maintainability, and flexibility of my project. Below are the key advantages—with concrete examples from my project—that illustrate why these principles are so valuable:

---

### 1. Improved Maintainability and Clarity

**Single Responsibility Principle (SRP):**
- **Advantage:** By ensuring that each class or module has one well-defined purpose, I minimize unintended side effects when making changes.
- **Example:** By separating car-related logic into its own `CarController` (instead of mixing it with generic product logic), I ensure that when a requirement changes for handling car-specific HTTP requests, only the dedicated controller is affected. This clear separation simplifies debugging and future modifications.

---

### 2. Enhanced Interchangeability and Robustness

**Liskov Substitution Principle (LSP):**
- **Advantage:** LSP guarantees that derived classes (e.g., `Car` and `Product`) can seamlessly replace their base class (`Item`) without causing errors. This enables me to work with a unified interface, confident that the underlying implementations adhere to expected behavior.
- **Example:** In my repository layer, both `CarRepository` and `ProductRepository` extend a common `AbstractRepository` that defines CRUD operations. As a result, any service relying on this abstract repository can substitute one repository for another without modifying the consuming code. This not only enhances code reliability but also supports future extensions where new item types can be introduced without breaking existing functionality.
  ```java
  public class Main {
      public static void main(String[] args) {
          // Substitute CarRepository where an AbstractRepository is expected
          AbstractRepository<Car> carRepository = new CarRepository();
      }
  }
  ```

---

### 3. Increased Extensibility Without Risk

**Open/Closed Principle (OCP):**
- **Advantage:** OCP ensures that my system’s components can be extended with new behavior without modifying the existing, well-tested code. This reduces the risk of regressions and allows my project to evolve gracefully.
- **Example:** The design of `AbstractCrudService` provides a stable foundation for CRUD operations. When I need to introduce a new type of entity or additional functionality, I can create a new service that extends this abstract class. The new service only needs to supply its specific repository via an overridden method, leaving the core CRUD logic untouched and secure.
  ```java
	  @Service
	public class CarServiceImpl extends AbstractCrudService<Car, String> implements CarService {
	
	    private final CarRepository carRepository;
	
	    public CarServiceImpl(CarRepository carRepository) {
	        this.carRepository = carRepository;
	    }
	    // Only needs to supply its specific repository via an overridden method
	    @Override
	    protected CrudRepository<Car, String> getRepository() {
	        return carRepository;
	    }

            // The core CRUD logic remain untouched and secure
	}
  ```

---

### 4. Reduced Coupling and Increased Flexibility

**Dependency Inversion Principle (DIP):**
- **Advantage:** By depending on abstractions rather than concrete implementations, my project becomes more modular. This decoupling makes unit testing easier and allows for swapping out implementations with minimal disruption.
- **Example:** The refactored `CarController` now accepts a `CarService` interface instead of a concrete `CarServiceImpl`. This means that the controller is insulated from changes in the service’s implementation and can work with any class that fulfills the `CarService` contract. As a result, I can introduce improvements or replacements to the service layer without needing to alter the controller, which simplifies testing and future enhancements.

---

### 5. Reduced Code Duplication

**Open/Closed Principle (OCP):**
- **Advantage:** By adhering to the Open/Closed Principle and leveraging abstract CRUD classes, my project minimizes code duplication across the service and repository layers. This design ensures that standard CRUD operations are implemented once and reused across various entities, reducing the risk of errors and easing maintenance.
- **Example:** Instead of rewriting CRUD operations for each new entity, I use `AbstractRepository` and `AbstractCrudService` as the base classes. For instance, `CarRepository` and `ProductRepository` extend `AbstractRepository` to inherit common data access methods, while `CarServiceImpl` and `ProductServiceImpl` extend `AbstractCrudService` to reuse standard service logic. This modular approach allows for easy extension and maintenance without redundant code changes.



## 3. Disadvantages of Not Applying SOLID Principles to My Project

When I don't apply SOLID principles, my project faces several challenges. Below are the key disadvantages along with examples that illustrate the potential pitfalls:

### 1. Poor Maintainability and Increased Complexity

- **Lack of Single Responsibility:**  
  Without SRP, classes or modules tend to handle multiple responsibilities, leading to bloated code that is hard to understand and maintain.
    - **Example:**  
      A `CarController` that not only handles HTTP requests but also processes business logic and manages data access can quickly become a tangled mess. This makes debugging difficult and increases the likelihood of introducing bugs when changes are made.


### 2. Code Duplication

- **Violating the Open/Closed Principle (OCP):**  
  When code is duplicated across modules, it indicates a failure to extend existing abstractions for new functionality. Duplicated logic requires modifications in multiple places for even small changes, leading to increased maintenance effort and risk of inconsistencies.
    - **Example:**  
      In my before-solid branch `CarServiceImpl` and `ProductServiceImpl` both contain their own versions of CRUD logic. Any alteration to the validation or persistence mechanisms must be replicated across all implementations, increasing the likelihood of errors and making future enhancements more cumbersome.
      ``` java
      // CarServiceImpl.java
      public class CarServiceImpl implements CarService {
          // Duplicate CRUD methods for Car entity
          public Car create(Car car) { /* implementation */ }
          public List<Car> findAll() { /* implementation */ }
          // Additional CRUD methods...
      }
  
      ```
      ```java
      // ProductServiceImpl.java
      public class ProductServiceImpl implements ProductService {
          // Duplicate CRUD methods for Product entity
          public Product create(Product product) { /* implementation */ }
          public List<Product> findAll() { /* implementation */ }
          // Additional CRUD methods...
      ```

### 3. Tight Coupling and Reduced Modularity

- **Ignoring the Dependency Inversion Principle (DIP):**  
  When high-level modules depend on concrete implementations rather than abstractions, the code becomes tightly coupled. This tight coupling makes it difficult to test and evolve the system.
    - **Example:**  
      If my `CarController` directly depends on a specific implementation like `CarServiceImpl` instead of a more abstract `CarService` interface, any change in the service's implementation could require corresponding changes in the controller. This not only complicates unit testing but also reduces flexibility for future enhancements.









  
