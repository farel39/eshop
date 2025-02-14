# Reflection 1
Below is how i implement clean code 

## 1. Clean Code Principles
### 1.1 Meaningful Names
Applied:

- Class names like Product, ProductRepository, ProductService, and ProductController are meaningful and describe their purpose.

- Method names like create, findAll, findById, update, and deleteById are clear and indicate their functionality.

- Variable names like productData, productId, productName, and productQuantity are descriptive.

### 1.2 Functions
Applied:

- Methods in the ProductRepository, ProductService, and ProductController are small and focused on a single responsibility.

- The create, findAll, findById, update, and deleteById methods are concise and perform one task each.

### 1.3 Comments
Applied:

- The code is mostly self-explanatory, so there are no unnecessary comments.

### 1.4 Objects and Data Structures
Applied:

- The Product class is a simple data structure with getters and setters, adhering to the principles of encapsulation.

- The ProductRepository uses a List to store products, which is appropriate for this use case.


### 1.5 Error Handling
Applied:

- The findById method in ProductRepository returns null if the product is not found, which is a simple way to handle missing data.

## 2. Secure Coding Practices
### 2.1 Authentication
Applied:

- Not implemented in the current code. 

### 2.2 Authorization
Applied:

- Not implemented in the current code. 

### 2.3 Input Data Validation
Applied:

- Basic validation is performed in the HTML forms using th:field and input types like text and number.


### 2.4 Output Data Encoding
Applied:

- Thymeleaf automatically encodes output data to prevent XSS (Cross-Site Scripting) attacks.

I did not find any mistakes in the source code but there could be improvements on it. This include implementing authentication, authorization, input data validation, etc.
