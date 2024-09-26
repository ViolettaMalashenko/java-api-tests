# TESTING PLAN FOR FASTAPI TO-DO APPLICATION

## OBJECTIVE

This test plan is designed to ensure the quality, functionality and reliability of the FastAPI-based task management (todo) API application.

- The main purpose of testing is to verify the correctness of the API endpoints and evaluate its ability to handle errors
- Check the processing of GET, POST, PUT, DELETE request types
- Check API protection against unauthorized access and possible vulnerabilities

To interact with the API we will implement java automation framework, addressing the endpoints at `http://localhost:8080/`.

## SCOPE OF TESTING

1. **Make sure that aplication can perform initial purpes**: user is able to create and store TODO actions
2. **Check that the API responses are correct**: Verify that the API returns the expected responses and has the correct code status.

- Validation API endpoinds using methods like: `GET`, `POST`, `PUT`, `DELETE`

3. **Evaluate Error Handling**: Check how the API responds on incorrect or incomplete data and ensure that it returns appropriate error messages
4. **Security**: Ensure that the API implements proper authorization mechanisms to protect sensitive endpoints and data. Ensure that access controls are enforced and check for potential vulnerabilities (e.g. unauthorized access without authorization).

## TEST OBJECT

Todo application API implemented on FastAPI that allows users to:

- Create tasks
- Save tasks
- Modify tasks
- Delete tasks

## TEST PHASES

During testing we will hightlight a few steps like:

1. **Analysis of documentation**: impossible due to its absence.
   Planing test cases, developing test cases for each endpoint based on the specification
2. **Functional Testing**:
   Testing all available API endpoints for correct functionality, including creating, retrieving, updating, and deleting tasks

3. **Bugs Funding**:
   Running tests with incorrect requests to evaluate the API's robustness to errors

4. **Testing method**: The black box method will be used in the testing process. This means that testing will be conducted without access to the internal structure and implementation of the software developed in the Python language. This approach is chosen because this programming language is unfamiliar to the tester, which makes it impossible to analyze and understand the internal logic of the system. The testing will focus on the input data and expected output data, which will allow an objective assessment of the product functionality

## Testing Tools

- **Visual Studio Code**: Used for writing Test Plans in markdown format
- **Postman**: Utilized for executing requests to APIs
- **Swagger UI**: Provides an interface for testing API functionality
- **IntelliJ IDEA (Java)**: Employed for test automation purposes
- **Allure Report**: Formats reports on the results of automated tests and assists in their analysis
- JAVA version 17
- Potential library HTTP request creation - RestAssured 5.4.0

## TESTING ARTIFACTS:

1. **Test Execution Logs**:
  Utilize the Logback logging framework alongside the SLF4J API to log messages and capture detailed outputs from test executions in the console.

2. **Test Report**:

   - A document that summarizes the results of testing. Includes information about the tests performed, status (pass/fail), defects found, and recommendations for improvement.

3. **Bug Reports**:

   - A record of bugs and failures identified, including information on how to reproduce each bug, priority, and status (open/in process of fixing/closed).

4. **Test Automation Documentation**:
   - A description of the tools and techniques used to automate API indicators, as well as codes for performing automated testing.

## TESTING SCHEDULE

All tests executions will be triggered by cron (attach cron rules later)

- Preparation of test cases: 15.09.2024 - 18.09.2024
- Executing testing: 01.10.2024 - 20.10.2024
- Analyzing the results and report: 21.10.2024 - 30.10.2024

## API ANALYSIS AND TESTING

## ENDPOINTS

- **Root**:
  - `GET`/ Read root
- **Todos**:
  - `GET`/ todo - Get All Todos
  - `POST`/ todo - Add Todo
  - `PUT`/ todo/{id}
  - `DELETE`/ todo/{id}

## PARAMETERS

- **Root**:
  - `GET`/ Read root – 200
- **Todos**:
  - `GET`/ todo Get Todos – no parameters
  - `POST`/ todo Add Todo – no parameters
  - `PUT`/ todo/{id} – Parameters – `id` (required) - integer
  - `DELETE`/ todo/{id}

## RESPONSES

all api documentation is available in file: `open_api.json`

- **Root**:
  - `GET`/ Read root:
    - 200 Successful Response
    - Media type – application/JSON
    - Value - string
- **Todos**:
  - `CET`/ todo Get Todos:
    - 200 Successful Response
    - Media type – application/JSON
    - Value - string
  - `POST`/ todo Add Todo – no parameters:
    - 200 Successful Response
    - 422 Validation Error
    - Media type – application/JSON
    - Value - string
  - `PUT`/ todo/{id} – Parameters – id (required):
    - 200 Successful Response
    - 422 Validation Error
    - Media type – application/JSON
    - Value - string
  - `DELETE`/ todo/{id}:
    - 200 Successful Response
    - 422 Validation Error
    - Media type – application/JSON
    - Value – string


## TEST CASES

## Authentication and Authorization Testing

| Test Case ID | HTTP Method | Endpoint   | Description                                                   | Parameters for Authorization              | Expected Result                                            |
|--------------|-------------|------------|---------------------------------------------------------------|------------------------------------------|---------------------------------------------------------|
| TA-1         | GET         | /      | Checking retrieval of the root resource without authentication.  |                                       | Unable to send request; receive response "Not authenticated". |
| TA-2         | GET         | /      | Checking successful access to the root resource with valid credentials. | Username: username <br>Password: password          | User successfully registered. Request sent successfully; receive response "Welcome to your todo list foo". |
| TA-3         | GET         | /      | Checking access to the root resource with invalid credentials.   | Username: user<br>Password: pass          | Receive response "Incorrect email or password".          |

## Functionality testing
| Test Case ID      | HTTP Method | Endpoint     | Description                                                                                | Request Body                           | Expected Status Code and Message                                     |
|-------------------|-------------|--------------|--------------------------------------------------------------------------------------------|---------------------------------------|-----------------------------------------------------------------------|
| TF-1              | POST        | /        | Checking the reception of the root resource with an incorrect POST method instead of GET.  |                                      | Status Code 405 Method Not Allowed.                                   |
| TF-2              | POST        | /todo/{1}    | Creating a resource with an already existing ID.                                          | id = 1                                | Status Code 404 Not Found.                                           |
| TF-3              | POST        | /todo        | Adding new data without a body.                                                            |                                       | 422 Unprocessable Entity. "msg": "Expecting property name enclosed in double quotes"  |
| TF-4              | POST        | /todo        | Adding new data with incorrect data. Syntax error.                                         | { "id": {"test": "value"}, "todo": "" }              | 422 Unprocessable Entity. "msg": "Expecting property name enclosed in double quotes"  |
| TF-5              | PUT         | /todo        | Checking the processing of a PUT request at an endpoint without support.                   | { "id": 6, "todo": "jogging" }      | Status 405 Method Not Allowed                                         |
| TF-6              | PUT         | /todo/{id}   | Updating a resource with a non-existent identifier.                                         | id = 999                              | Status Code 404 Not Found. Todo with id 999 not found.               |
| TF-7              | DELETE      | /todo/{id}   | Deleting a resource with a non-existent identifier.                                         | id = 800                              | Status Code 404 Not Found. Todo with id 800 not found.               |

### Boundary Cases

| **Test Case ID** | **Method** | **Endpoint** | **Description**                                                                | **Request Body**                               | **Expected Result**                                   |
|-------------------|------------|--------------|-----------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------|--------------------------------------|
| TB-1             | POST       | /todo        | Check adding a resource with the long length.   | { "id": 6, "todo": "drink water drink water drink water drink water drink water drink water drink water drink water drink water drink water drink water drink water drink water"} | Status code 200.                     |
| TB-2              | POST       | /todo        | Check adding a resource with a minimum number of charters. | { "id": 7,  "todo": ""}                                                                                              | Status code 200.                     |
| TB-3              | DELETE     | /todo/{id}   | Deleting a resource with the minimum possible identifier.                  | id = 0                                                                                                              | Status code 200.                     |
| TB-4              | DELETE     | /todo/{id}   | Deleting a resource with the maximum possible identifier.                  | id = 7                                                                                                              | Status code 200.                     |
## CONCLUSION

This test plan aims to effectively test OpenAPI using the black box method. It will provide comprehensive testing and improve the quality of the provided API.