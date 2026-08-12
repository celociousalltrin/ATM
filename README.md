# 🏦 ATM – Spring Boot Banking & ATM Simulation

A backend ATM and banking simulation built with **Java 17 and Spring Boot**.

The project provides REST APIs for:

* User login/logout
* User lookup
* Bank account creation and lookup
* Deposits
* Withdrawals
* Account-to-account transfers
* Debt creation and tracking
* Debt payments
* Transaction recording
* Centralized API response handling
* Validation and business-rule enforcement
* DTO ↔ Entity mapping using MapStruct
* Unit and controller testing

The project is designed as a **practice backend application** for understanding how a real-world banking/ATM backend can
be structured using Spring Boot layered architecture.

---

## 📌 Repository

GitHub:

https://github.com/celociousalltrin/ATM

---

# 🏗️ Architecture

The application follows a layered architecture.

```text
Client
  │
  ▼
Controller
  │
  ▼
Service
  │
  ▼
Repository
  │
  ▼
Database / Persistence
```

DTO mapping is handled separately:

```text
Request JSON
    │
    ▼
Request DTO
    │
    ▼
MapStruct Mapper
    │
    ▼
Entity
    │
    ▼
Repository
    │
    ▼
Entity
    │
    ▼
MapStruct Mapper
    │
    ▼
Response DTO
    │
    ▼
ApiResponse
    │
    ▼
JSON
```

The source tree is organized into feature-based modules:

```text
com.example.atm
│
├── account
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── mapper
│   ├── repository
│   └── service
│
├── user
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── mapper
│   ├── repository
│   └── service
│
├── transaction
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── mapper
│   ├── repository
│   └── service
│
├── debt
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── mapper
│   ├── repository
│   └── service
│
└── common
    ├── exception
    └── response
```

The repository currently follows this feature-oriented structure for Account, Transaction, User, and Debt modules.

---

# 🛠️ Technology Stack

| Technology        | Purpose                         |
|-------------------|---------------------------------|
| Java 17           | Programming language            |
| Spring Boot 3.4.2 | Application framework           |
| Spring Web        | REST APIs                       |
| Spring Validation | Request validation              |
| Spring WebSocket  | WebSocket support               |
| Spring AOP        | Cross-cutting concerns          |
| Lombok            | Boilerplate reduction           |
| MapStruct 1.6.3   | DTO ↔ Entity mapping            |
| Maven             | Build and dependency management |
| JUnit 5           | Testing                         |
| Mockito           | Mocking                         |
| MockMvc           | Controller/API testing          |

The Maven configuration currently uses Spring Boot `3.4.2`, Java `17`, MapStruct `1.6.3`, and Lombok.

---

# 🚀 Running the Application

## Prerequisites

Install:

* Java 17+
* Maven 3.8+
* Git

Verify Java:

```bash
java -version
```

Verify Maven:

```bash
mvn -version
```

---

## Clone the repository

```bash
git clone https://github.com/celociousalltrin/ATM.git
cd ATM
```

---

## Run the Application

### macOS / Linux

Make sure you have Git installed, then clone the repository and run:

```bash
chmod +x start.sh
./start.sh
```

The `start.sh` script will automatically:

* Check for **Java 17**.
* If Java 17 is missing, ask whether you want to install it.

    * **macOS:** installs OpenJDK 17 using Homebrew.
    * **Linux:** installs OpenJDK 17 using `apt`, `dnf`, or `yum`.
* Check whether **port 8080** is already in use.
* If port 8080 is occupied, ask whether you want to stop the process using it.
* Build the application using the Maven wrapper.
* Start the ATM application on:

```text
http://localhost:8080
```

### Prompts During Startup

Depending on your system, the script may ask:

```text
Would you like to install OpenJDK 17? (y/n):
```

or:

```text
Would you like to stop the process using port 8080? (y/n):
```

Enter `y` to allow the script to perform the requested action, or `n` to stop the startup process.

> **Note:** The script supports **macOS and Linux**. Windows users should use the Maven commands directly instead of
`start.sh`.

### Windows

`start.sh` is not supported on Windows. Run the application using the Maven wrapper instead.

Make sure **Java 17** is installed and configured in your `PATH`, then open Command Prompt or PowerShell in the project
directory and run:

```cmd
mvnw.cmd clean package -DskipTests
mvnw.cmd spring-boot:run
```

The application will start at:

```text
http://localhost:8080
```

> **Note:** Java **17** is required. Make sure port **8080** is available before starting the application.


---

# 🌐 Base URL

By default:

```text
http://localhost:8080
```

Therefore:

```text
POST http://localhost:8080/login
```

etc.

---

# 📦 Common API Response Format

All APIs use the common `ApiResponse<T>` wrapper.

The actual response class contains:

```text
code
message
responseData
status
errors
isBulkErrors
```

Null fields are excluded from JSON responses.

## Successful response

Example:

```json
{
  "code": "OK001",
  "message": "User Logged in Successfully",
  "responseData": {
    "id": "user-123",
    "userName": "john",
    "createdAt": "2026-08-12T10:15:30Z"
  },
  "status": "SUCCESS"
}
```

## Error response

Example:

```json
{
  "code": "ER004",
  "message": "Account not Exist",
  "status": "ERROR"
}
```

Validation errors may additionally contain:

```json
{
  "code": "ER998",
  "message": "Validation Errors",
  "status": "ERROR",
  "errors": [
    {
      "field": "amount",
      "message": "must not be null"
    }
  ],
  "isBulkErrors": true
}
```

---

# 🔢 Response Codes

## Success Codes

| Code    | Meaning                          |
|---------|----------------------------------|
| `OK001` | User logged in successfully      |
| `OK002` | User logged out successfully     |
| `OK003` | Account created successfully     |
| `OK004` | Amount deposited successfully    |
| `OK005` | Amount withdrawn successfully    |
| `OK006` | Transaction created successfully |
| `OK007` | Debt created successfully        |
| `OK008` | Debt updated                     |
| `OK999` | Request successful               |

These codes and messages are defined in `ResponseCode`.

## Error Codes

| Code    | Meaning                                |
|---------|----------------------------------------|
| `ER001` | Invalid user                           |
| `ER002` | User name not found                    |
| `ER003` | User ID not found                      |
| `ER004` | Account not found                      |
| `ER005` | Invalid/missing transaction type       |
| `ER006` | Invalid account ID                     |
| `ER007` | Same-account transfer not allowed      |
| `ER008` | Insufficient balance                   |
| `ER009` | Debt not found                         |
| `ER010` | Transaction amount does not match debt |
| `ER011` | Debt already paid                      |
| `ER996` | Endpoint not found                     |
| `ER997` | Invalid request body                   |
| `ER998` | Validation error                       |
| `ER999` | Internal server error                  |

---

# 👤 User APIs

The User controller currently exposes:

```text
POST /login
GET  /logout/{id}
GET  /user/{id}
```

---

## 1. Login / Create User

### Endpoint

```http
POST /login
```

### Request

```json
{
  "userName": "john"
}
```

`userName` is required and cannot be blank.

### cURL

```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{
    "userName": "john"
  }'
```

### Response

```json
{
  "code": "OK001",
  "message": "User Logged in Successfully",
  "responseData": {
    "id": "user-123",
    "userName": "john",
    "createdAt": "2026-08-12T10:00:00Z"
  },
  "status": "SUCCESS"
}
```

The UserResponse contains:

```text
id
userName
createdAt
```

### Important behavior

The service uses `getOrCreate()`.

Therefore, login can effectively behave as:

```text
User exists
    │
    └── Return existing user

User does not exist
    │
    └── Create user
        │
        └── Return new user
```

---

# 2. Logout

### Endpoint

```http
GET /logout/{id}
```

Example:

```http
GET /logout/user-123
```

### Response

```json
{
  "code": "OK002",
  "message": "User Logout Successfully",
  "status": "SUCCESS"
}
```

The logout endpoint returns no `responseData`.

---

# 3. Get User

### Endpoint

```http
GET /user/{id}
```

Example:

```http
GET /user/user-123
```

### Response

```json
{
  "code": "OK999",
  "message": "Request successful",
  "responseData": {
    "id": "user-123",
    "userName": "john",
    "createdAt": "2026-08-12T10:00:00Z"
  },
  "status": "SUCCESS"
}
```

---

# 🏦 Account APIs

The Account controller provides:

```text
POST /account
GET  /account/{id}
GET  /account/user/{userId}
```

---

# 4. Create Account

### Endpoint

```http
POST /account
```

### Request

```json
{
  "userId": "user-123"
}
```

`userId` is required.

### cURL

```bash
curl -X POST http://localhost:8080/account \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user-123"
  }'
```

### Response

```json
{
  "code": "OK003",
  "message": "Account Created Successfully",
  "responseData": {
    "id": "account-123",
    "userId": "user-123",
    "balance": 0.00,
    "createdAt": "2026-08-12T10:05:00Z",
    "updatedAt": "2026-08-12T10:05:00Z"
  },
  "status": "SUCCESS"
}
```

The AccountResponse contains:

```text
id
userId
balance
createdAt
updatedAt
```

### Important behavior

The controller first checks whether the user already has an account.

```text
POST /account
      │
      ▼
Find account by userId
      │
      ├── Account exists
      │      │
      │      └── Return existing account
      │
      └── Account doesn't exist
             │
             ▼
       Create account
             │
             ▼
       Return account
```

The service also verifies that the referenced user exists before creating the account.

---

# 5. Get Account By ID

### Endpoint

```http
GET /account/{id}
```

Example:

```http
GET /account/account-123
```

### Response

```json
{
  "code": "OK999",
  "message": "Request successful",
  "responseData": {
    "id": "account-123",
    "userId": "user-123",
    "balance": 1500.00,
    "createdAt": "2026-08-12T10:05:00Z",
    "updatedAt": "2026-08-12T11:00:00Z"
  },
  "status": "SUCCESS"
}
```

If the account does not exist:

```json
{
  "code": "ER004",
  "message": "Account not Exist",
  "status": "ERROR"
}
```

---

# 6. Get Account By User ID

### Endpoint

```http
GET /account/user/{userId}
```

Example:

```http
GET /account/user/user-123
```

### Response

```json
{
  "code": "OK999",
  "message": "Request successful",
  "responseData": {
    "id": "account-123",
    "userId": "user-123",
    "balance": 1500.00,
    "createdAt": "2026-08-12T10:05:00Z",
    "updatedAt": "2026-08-12T11:00:00Z"
  },
  "status": "SUCCESS"
}
```

---

# 💳 Transaction APIs

Transactions are handled through:

```http
POST /transaction
```

The transaction controller accepts a `TransactionRequest` and delegates transaction processing to `TransactionService`.

The supported transaction types are:

```text
DEPOSIT
WITHDRAW
TRANSFER
DEBT_PAYMENT
```

---

# Transaction Request

The request structure is:

```json
{
  "accountId": "account-123",
  "amount": 500.00,
  "targetAccountId": "account-456",
  "type": "TRANSFER"
}
```

Fields:

| Field             | Required | Description                              |
|-------------------|----------|------------------------------------------|
| `accountId`       | Yes      | Source/account performing transaction    |
| `amount`          | Yes      | Transaction amount                       |
| `targetAccountId` | Depends  | Target account for transfer/debt payment |
| `type`            | Yes      | Transaction type                         |

`amount` must be at least `0.01` and supports up to 10 integer digits and 2 decimal places.

---

# 7. Deposit

### Endpoint

```http
POST /transaction
```

### Request

```json
{
  "accountId": "account-123",
  "amount": 1000.00,
  "type": "DEPOSIT"
}
```

### Flow

```text
POST /transaction
       │
       ▼
TransactionService
       │
       ▼
Find account
       │
       ▼
Validate transaction
       │
       ▼
DEPOSIT
       │
       ▼
Credit account
       │
       ▼
Save transaction
       │
       ▼
Return TransactionResponse
```

The transaction service maps the request to an entity, validates the account and transaction, credits the account for
`DEPOSIT`, saves the transaction, and returns the transaction response.

### Response

```json
{
  "code": "OK006",
  "message": "Transaction Created Successfully",
  "responseData": {
    "id": "txn-123",
    "accountId": "account-123",
    "amount": 1000.00,
    "type": "DEPOSIT",
    "createdAt": "2026-08-12T11:00:00Z"
  },
  "status": "SUCCESS"
}
```

---

# 8. Withdraw

### Request

```json
{
  "accountId": "account-123",
  "amount": 200.00,
  "type": "WITHDRAW"
}
```

### Flow

```text
POST /transaction
       │
       ▼
Find account
       │
       ▼
Validate balance
       │
       ├── Insufficient
       │      └── ER008
       │
       └── Sufficient
              │
              ▼
          Debit account
              │
              ▼
        Save transaction
              │
              ▼
         Return success
```

For withdrawals, the service checks the account balance before debiting the requested amount.

### Response

```json
{
  "code": "OK006",
  "message": "Transaction Created Successfully",
  "responseData": {
    "id": "txn-124",
    "accountId": "account-123",
    "amount": 200.00,
    "type": "WITHDRAW",
    "createdAt": "2026-08-12T11:05:00Z"
  },
  "status": "SUCCESS"
}
```

---

# 9. Transfer

Transfer moves money from one account to another.

### Endpoint

```http
POST /transaction
```

### Request

```json
{
  "accountId": "account-123",
  "amount": 500.00,
  "targetAccountId": "account-456",
  "type": "TRANSFER"
}
```

### Normal transfer flow

```text
Account A
Balance = 2000

        │
        │ Transfer 500
        ▼

Account B
Balance = 1000
```

After the transaction:

```text
Account A = 1500
Account B = 1500
```

The transaction service debits the source account and credits the target account.

### Response

```json
{
  "code": "OK006",
  "message": "Transaction Created Successfully",
  "responseData": {
    "id": "txn-125",
    "accountId": "account-123",
    "amount": 500.00,
    "targetAccounId": "account-456",
    "type": "TRANSFER",
    "createdAt": "2026-08-12T11:10:00Z"
  },
  "status": "SUCCESS"
}
```

---

# ⚠️ Transfer With Insufficient Balance

This ATM has an interesting debt mechanism.

Suppose:

```text
Account A balance = 300
Account B balance = 1000
```

User requests:

```text
Transfer = 500
```

The account only has:

```text
300
```

The service calculates:

```text
Debt = requested amount - available balance

Debt = 500 - 300

Debt = 200
```

The current transaction service creates or updates a debt for the missing amount and reduces the actual transfer amount
to the available balance.

Therefore:

```text
Requested transfer = 500

Available balance = 300

Actual transfer = 300

Outstanding debt = 200
```

This allows the system to represent an unpaid amount instead of simply losing the transfer request.

---

# 💰 Debt APIs

Debt management is handled by:

```text
POST /debt
PUT  /debt/update/{id}
GET  /debt?accountId={accountId}
```

---

# 10. Create Debt

### Endpoint

```http
POST /debt
```

### Request

```json
{
  "owedBy": "account-123",
  "owedTo": "account-456",
  "amount": 200.00
}
```

The request requires:

```text
owedBy
owedTo
amount
```

and the amount must be at least `0.01`.

### Response

```json
{
  "code": "OK007",
  "message": "Debt Created Successfully",
  "responseData": {
    "id": "debt-123",
    "owedBy": "account-123",
    "owedTo": "account-456",
    "amount": 200.00,
    "status": "PENDING",
    "createdAt": "2026-08-12T11:20:00Z",
    "updatedAt": "2026-08-12T11:20:00Z"
  },
  "status": "SUCCESS"
}
```

---

# 11. Get Payable Debts

### Endpoint

```http
GET /debt?accountId=account-123
```

This returns debts where the supplied account is the account that owes money.

### Response

```json
{
  "code": "OK999",
  "message": "Request successful",
  "responseData": [
    {
      "debtId": "debt-123",
      "userName": "john",
      "owedBy": "account-123",
      "owedTo": "account-456",
      "amount": 200.00,
      "status": "PENDING",
      "createdAt": "2026-08-12T11:20:00Z"
    }
  ],
  "status": "SUCCESS"
}
```

The payable debt response contains:

```text
debtId
userName
owedBy
owedTo
amount
status
createdAt
```

---

# 12. Update Debt Status

### Endpoint

```http
PUT /debt/update/{id}
```

Example:

```http
PUT /debt/update/debt-123
```

### Response

```json
{
  "code": "OK008",
  "message": "Debt Created Successfully",
  "responseData": {
    "id": "debt-123",
    "owedBy": "account-123",
    "owedTo": "account-456",
    "amount": 200.00,
    "status": "COMPLETED",
    "createdAt": "2026-08-12T11:20:00Z",
    "updatedAt": "2026-08-12T11:30:00Z"
  },
  "status": "SUCCESS"
}
```

The debt service changes the status to `COMPLETED`. Attempting to complete an already completed debt results in `ER011`.

---

# 13. Debt Payment

Debt payment is performed through the Transaction API.

### Endpoint

```http
POST /transaction
```

### Request

```json
{
  "accountId": "account-123",
  "amount": 200.00,
  "targetAccountId": "account-456",
  "type": "DEBT_PAYMENT"
}
```

The service:

1. Validates both accounts.
2. Prevents transferring to the same account.
3. Checks that the account has sufficient balance.
4. Finds the debt between the two accounts.
5. Verifies the transaction amount equals the debt amount.
6. Marks the debt as completed.
7. Debits the payer.
8. Credits the receiver.
9. Saves the transaction.

This behavior is implemented directly in `TransactionService`.

---

# 🔄 Complete ATM Flow

The complete application flow can be understood through the following scenario.

---

## Step 1 – User Login

Client:

```http
POST /login
```

Request:

```json
{
  "userName": "john"
}
```

Response:

```json
{
  "code": "OK001",
  "message": "User Logged in Successfully",
  "responseData": {
    "id": "user-123",
    "userName": "john",
    "createdAt": "2026-08-12T10:00:00Z"
  },
  "status": "SUCCESS"
}
```

The client stores:

```text
userId = user-123
```

---

# Step 2 – Create/Get Account

Client:

```http
POST /account
```

Request:

```json
{
  "userId": "user-123"
}
```

Response:

```json
{
  "code": "OK003",
  "message": "Account Created Successfully",
  "responseData": {
    "id": "account-123",
    "userId": "user-123",
    "balance": 0.00,
    "createdAt": "2026-08-12T10:05:00Z",
    "updatedAt": "2026-08-12T10:05:00Z"
  },
  "status": "SUCCESS"
}
```

The client now knows:

```text
accountId = account-123
```

---

# Step 3 – Deposit Money

Client:

```http
POST /transaction
```

Request:

```json
{
  "accountId": "account-123",
  "amount": 5000.00,
  "type": "DEPOSIT"
}
```

Account:

```text
Before = 0
Deposit = 5000
After  = 5000
```

Transaction is recorded.

---

# Step 4 – Withdraw Money

Request:

```json
{
  "accountId": "account-123",
  "amount": 1000.00,
  "type": "WITHDRAW"
}
```

Account:

```text
Before = 5000
Withdraw = 1000
After = 4000
```

---

# Step 5 – Transfer Money

Suppose another user has:

```text
accountId = account-456
balance = 1000
```

John transfers:

```text
500
```

Request:

```json
{
  "accountId": "account-123",
  "amount": 500.00,
  "targetAccountId": "account-456",
  "type": "TRANSFER"
}
```

Result:

```text
John:

4000 - 500 = 3500


Target:

1000 + 500 = 1500
```

---

# Step 6 – Insufficient Transfer

Suppose:

```text
John balance = 300
```

John requests:

```text
Transfer = 500
```

The service calculates:

```text
Available = 300
Requested = 500

Debt = 500 - 300
Debt = 200
```

The actual transfer becomes:

```text
300
```

and a debt of:

```text
200
```

is created/updated between the accounts.

---

# Step 7 – View Payable Debts

Client:

```http
GET /debt?accountId=account-123
```

Response contains the outstanding debt.

Example:

```json
{
  "code": "OK999",
  "message": "Request successful",
  "responseData": [
    {
      "debtId": "debt-123",
      "userName": "john",
      "owedBy": "account-123",
      "owedTo": "account-456",
      "amount": 200.00,
      "status": "PENDING",
      "createdAt": "2026-08-12T11:20:00Z"
    }
  ],
  "status": "SUCCESS"
}
```

---

# Step 8 – Pay Debt

Client sends:

```http
POST /transaction
```

Request:

```json
{
  "accountId": "account-123",
  "amount": 200.00,
  "targetAccountId": "account-456",
  "type": "DEBT_PAYMENT"
}
```

The service validates that:

```text
Debt amount = 200
Transaction amount = 200
```

Then:

```text
Account 123
200 deducted

        │
        ▼

Account 456
200 credited

        │
        ▼

Debt
PENDING → COMPLETED
```

The transaction is then recorded.

---

# 🔁 End-to-End Flow Diagram

```text
                    ┌──────────────┐
                    │    Client    │
                    └──────┬───────┘
                           │
                           ▼
                    ┌──────────────┐
                    │   REST API   │
                    └──────┬───────┘
                           │
          ┌────────────────┼────────────────┐
          │                │                │
          ▼                ▼                ▼
       User API        Account API    Transaction API
          │                │                │
          ▼                ▼                ▼
       UserService     AccountService  TransactionService
          │                │                │
          │                │                ├── AccountService
          │                │                │
          │                │                ├── DebtService
          │                │                │
          │                │                └── TransactionRepository
          │                │
          ▼                ▼
       User DB         Account DB

                           │
                           ▼
                       DebtService
                           │
                           ▼
                        Debt DB
```

---

# 🧠 Transaction Architecture

The most important part of the application is the `TransactionService`.

The high-level logic is:

```text
createTransaction()
        │
        ▼
Convert DTO → Entity
        │
        ▼
Find source account
        │
        ▼
Validate transaction
        │
        ▼
Update account balance
        │
        ├── DEPOSIT
        │      └── Credit account
        │
        ├── WITHDRAW
        │      └── Debit account
        │
        ├── TRANSFER
        │      ├── Debit source
        │      ├── Credit target
        │      └── Create/update debt if required
        │
        └── DEBT_PAYMENT
               ├── Validate debt
               ├── Complete debt
               ├── Debit payer
               └── Credit receiver
        │
        ▼
Save Transaction
        │
        ▼
Entity → Response DTO
        │
        ▼
ApiResponse
```

This is implemented in the current `TransactionService`.

---

# 🔐 Business Rules

## Account Rules

* A user must exist before an account can be created.
* A user can be checked for an existing account before creating one.
* Account lookup returns `ER004` when the account does not exist.
* Account balance is maintained by credit/debit operations.

---

## Transaction Rules

### Deposit

```text
Amount must be >= 0.01
```

Deposits increase the account balance.

### Withdraw

The account must have enough balance.

Otherwise:

```text
ER008
```

### Transfer

The target account must exist.

The source and target accounts cannot be the same.

Otherwise:

```text
ER007
```

### Debt Payment

The target account must exist.

The debt must exist.

The transaction amount must equal the debt amount.

Otherwise:

```text
ER010
```

An already completed debt cannot be paid again:

```text
ER011
```

These rules are enforced by `TransactionService` and `DebtService`.

---

# 🧩 DTOs

## UserRequest

```json
{
  "userName": "john"
}
```

## AccountRequest

```json
{
  "userId": "user-123"
}
```

## TransactionRequest

```json
{
  "accountId": "account-123",
  "amount": 500.00,
  "targetAccountId": "account-456",
  "type": "TRANSFER"
}
```

## DebtRequest

```json
{
  "owedBy": "account-123",
  "owedTo": "account-456",
  "amount": 200.00
}
```

---

# 📤 Response DTOs

## UserResponse

```text
id
userName
createdAt
```

## AccountResponse

```text
id
userId
balance
createdAt
updatedAt
```

## TransactionResponse

```text
id
accountId
amount
targetAccounId
type
createdAt
```

## DebtResponse

```text
id
owedBy
owedTo
amount
status
createdAt
updatedAt
```

## DebtPayableResponse

```text
debtId
userName
owedBy
owedTo
amount
status
createdAt
```

---

# 🧪 Testing

The project includes Spring Boot testing support through:

```xml
spring-boot-starter-test
```

The test stack can be used for:

* JUnit 5
* Mockito
* MockMvc
* Controller tests
* Service tests

The recommended controller testing pattern is:

```text
@WebMvcTest(AccountController.class)
        │
        ▼
MockMvc
        │
        ▼
HTTP request
        │
        ▼
Controller
        │
        ▼
Mocked Service
```

Example:

```java
@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;
}
```

---

# 📊 API Summary

| Method | Endpoint                 | Purpose                                |
|--------|--------------------------|----------------------------------------|
| POST   | `/login`                 | Login/create user                      |
| GET    | `/logout/{id}`           | Logout user                            |
| GET    | `/user/{id}`             | Get user                               |
| POST   | `/account`               | Create/get account                     |
| GET    | `/account/{id}`          | Get account                            |
| GET    | `/account/user/{userId}` | Get account by user                    |
| POST   | `/transaction`           | Deposit/withdraw/transfer/debt payment |
| POST   | `/debt`                  | Create debt                            |
| GET    | `/debt?accountId={id}`   | Get payable debts                      |
| PUT    | `/debt/update/{id}`      | Complete/update debt                   |

---

# 🧾 Recommended API Usage

For a frontend ATM application, the preferred transaction flow is:

```text
LOGIN
  ↓
GET/CREATE ACCOUNT
  ↓
DISPLAY ACCOUNT INFORMATION
  ↓
USER SELECTS OPERATION
  │
  ├── Deposit
  │      ↓
  │   POST /transaction
  │   type = DEPOSIT
  │
  ├── Withdraw
  │      ↓
  │   POST /transaction
  │   type = WITHDRAW
  │
  ├── Transfer
  │      ↓
  │   POST /transaction
  │   type = TRANSFER
  │
  └── Pay Debt
         ↓
      GET /debt
         ↓
      Select Debt
         ↓
      POST /transaction
      type = DEBT_PAYMENT
```

This keeps **account management** separate from **financial transactions**, which is a cleaner design than having
deposit and withdrawal directly inside the Account controller.

---

# 🏦 Example Complete Session

## Initial state

```text
User:
John

Account:
account-123

Balance:
0.00
```

### Deposit ₹5,000

```json
{
  "accountId": "account-123",
  "amount": 5000.00,
  "type": "DEPOSIT"
}
```

Balance:

```text
5000.00
```

### Withdraw ₹1,000

```json
{
  "accountId": "account-123",
  "amount": 1000.00,
  "type": "WITHDRAW"
}
```

Balance:

```text
4000.00
```

### Transfer ₹1,500

```json
{
  "accountId": "account-123",
  "amount": 1500.00,
  "targetAccountId": "account-456",
  "type": "TRANSFER"
}
```

Balance:

```text
2500.00
```

Target account receives:

```text
+1500.00
```

### Check debts

```http
GET /debt?accountId=account-123
```

If there are no debts:

```json
{
  "code": "OK999",
  "message": "Request successful",
  "responseData": [],
  "status": "SUCCESS"
}
```

---

# 📁 Project Design Principles

This project demonstrates several important backend concepts:

### Feature-based package structure

Instead of:

```text
controller/
service/
repository/
entity/
```

for the entire application, the project groups them by business feature:

```text
account/
user/
transaction/
debt/
```

This makes the project easier to scale.

### DTO separation

API requests do not directly expose entities.

```text
JSON
 ↓
DTO
 ↓
Entity
```

and:

```text
Entity
 ↓
DTO
 ↓
JSON
```

### MapStruct

MapStruct is used for compile-time DTO/entity mapping.

### Centralized response format

Instead of every controller creating a different JSON structure:

```json
{
  "success": true
}
```

the application consistently uses:

```json
{
  "code": "...",
  "message": "...",
  "responseData": {},
  "status": "SUCCESS"
}
```

### Centralized business errors

Business failures use predefined response codes such as:

```text
ER004
ER007
ER008
ER009
ER010
ER011
```

rather than arbitrary error messages.

---

# 🎯 Design Goal

The main goal of this project is to simulate the backend of an ATM while practicing **real-world Spring Boot backend
architecture**.

The central design is:

```text
User
 │
 └── Account
       │
       └── Transactions
              │
              ├── Deposit
              ├── Withdraw
              ├── Transfer
              └── Debt Payment
                       │
                       └── Debt
```

The application therefore separates:

```text
Identity
   ↓
User

Money ownership
   ↓
Account

Money movement
   ↓
Transaction

Outstanding obligation
   ↓
Debt
```

This separation keeps the domain model understandable and provides a foundation for extending the application into a
more complete banking simulation.
