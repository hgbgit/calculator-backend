@Calculate
Feature: Create a new operation
  Users wnat to create a new operation

  Scenario Outline: Signup a new user and do a operation: <operation>
    Given that the database contains the following roles:
      | id | name       |
      | 1  | ROLE_USER  |
      | 2  | ROLE_ADMIN |
    And the database contains the following operations:
      | id | type           | cost |
      | 1  | ADDITION       | 1.00 |
      | 2  | SUBTRACTION    | 1.00 |
      | 3  | MULTIPLICATION | 2.00 |
      | 4  | DIVISION       | 2.00 |
      | 5  | SQUARE_ROOT    | 2.50 |
      | 6  | RANDOM_STRING  | 3.00 |
    And the random string server is up and will return the following string: "FnH3RtkIjk"
    And the user wants to make a new signup with the following values:
      | username | <userName> |
      | email    | <email>    |
      | password | <password> |
    And this user signin into calculator
    And this user want to create a new operation "<operation>" with a as "<valuea>" and b as "<valueb>":
    When this user request the new operation
    Then the result of the operation will be: "<result>"
    And the calculator backend service will return status: <httpStatus>
    Examples:
      | description               | userName | email              | password    | operation      | valuea | valueb | result     | httpStatus |
      | add operation             | jhonDoe  | jhon@gmail.com     | anotherPass | ADDITION       | 10     | 30     | 40         | 200        |
      | subtract operation        | fakeName | fakeName@gmail.com | myPass      | SUBTRACTION    | 45     | 10.2   | 34.8       | 200        |
      | multiplication operations | mariah   | mariah@gmail.com   | otherpass   | MULTIPLICATION | 10     | 30     | 300        | 200        |
      | division operation        | rocky    | rocky@hotmail.com  | rockyPass   | DIVISION       | 36     | 9      | 4          | 200        |
      | square root operation     | diego    | diego@loanpro.com  | diegopass   | SQUARE_ROOT    | 9      | 10     | 3          | 200        |
      | random string operation   | minion   | minion@gmail.com   | minionPass  | RANDOM_STRING  | 10     | 2      | FnH3RtkIjk | 200        |