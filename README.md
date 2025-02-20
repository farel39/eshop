- [Deployment URL](https://involved-renata-farel39-60683431.koyeb.app/)
- [Module 1 Reflection](#module-1)
- [Module 2 Reflection](#module-2)

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
## 2. Look at your CI/CD workflows (GitHub)/pipelines (GitLab). Do you think the current implementation has met the definition of Continuous Integration and Continuous Deployment? Explain the reasons (minimum 3 sentences)!
My current implementation meets the definition of Continuous Integration (CI) and Continuous Deployment (CD) for several reasons.

- Automated Testing and Code Quality Checks (CI): My workflow runs unit tests on every push and pull request, ensuring that new changes do not introduce regressions. Additionally, SonarCloud performs static code analysis to detect bugs, vulnerabilities, and code smells before deployment, reinforcing best practices in software quality.

- Security & Compliance Checks (CI): The OSSF Scorecard workflow regularly analyzes my repository for security risks and enforces branch protection, reducing the likelihood of supply chain attacks. This aligns with CI principles by continuously validating code integrity before merging changes.

- Automated Deployment on Main Branch Updates (CD): Since Koyeb automatically deploys new changes when updates are pushed to the main branch, my pipeline supports Continuous Deployment (CD). This means every approved and merged change is immediately available in production without manual intervention, ensuring fast and reliable releases.

- Infrastructure Automation (CD): By leveraging GitHub Actions to handle builds, tests, and security checks, combined with Koyeb’s automatic deployment, my pipeline minimizes human intervention. This ensures a seamless and scalable CI/CD workflow, enabling rapid iterations and reducing downtime.
