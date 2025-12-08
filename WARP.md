# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Project Overview

This is a full-stack restaurant ordering system ("Sistema de comandas para restaurante") with separate backend and frontend applications:

- **Backend**: Spring Boot 3.3.5 REST API with JWT authentication, WebSockets, and MySQL database
- **Frontend**: Angular 17 SPA with role-based routing and real-time notifications

The system supports four user roles: MESERO (waiter), COCINERO (cook), CAJERO (cashier), and ADMIN.

## Directory Structure

```
restaurant/
├── ordering-system-backn/     # Spring Boot backend
└── ordering-systems-front/    # Angular frontend
```

## Backend (ordering-system-backn/)

### Running the Backend

```bash
# Navigate to backend directory
cd ordering-system-backn

# Run with Maven wrapper
./mvnw spring-boot:run

# Build the project
./mvnw clean install

# Run tests
./mvnw test
```

The backend runs on port 8080 by default.

### Backend Architecture

**Tech Stack**: Java 17, Spring Boot 3.3.5, Spring Data JPA, Spring Security, JWT (jjwt 0.12.3), WebSockets (STOMP), MySQL

**Package Structure**:
- `config/` - Security, CORS, JWT filter, and WebSocket configuration
- `controller/` - REST endpoints organized by domain (Auth, Order, Menu, Payment, etc.)
- `service/` - Business logic layer
- `repository/` - JPA repositories for database access
- `model/` - JPA entities (User, Order, Bill, Payment, MenuItem, etc.)
- `dto/` - Request/response objects
- `enums/` - RoleType, OrderStatus, PaymentMethod
- `exception/` - Custom exceptions and global exception handler

**Key Components**:
- JWT authentication with stateless sessions
- WebSocket support via `/ws` endpoint with STOMP over SockJS
- Real-time notifications sent to `/topic/kitchen` and `/topic/waiter/{userId}`
- CORS configured for `http://localhost:4200`

**Security Notes**:
- Uses NoOpPasswordEncoder (not production-ready - passwords stored in plain text)
- JWT secret stored in application.properties (should be externalized)
- Public endpoints: `/api/auth/**`, `/ws/**`

**Database Configuration**:
- MySQL on localhost:3306 with database `restaurant_db`
- Auto-creates database if not exists
- Hibernate ddl-auto set to `update`
- Default credentials: root with empty password (configured in `application.properties`)

**Controllers**:
- AuthController, OrderController, MenuItemController, TableController
- BillController, PaymentController, CashRegisterController
- Admin controllers: AdminMenuController, AdminUserController, AdminTableController, AdminDashboardController, AdminStatisticsController
- ReportController, InventoryController

## Frontend (ordering-systems-front/)

### Running the Frontend

```bash
# Navigate to frontend directory
cd ordering-systems-front

# Install dependencies (first time only)
npm install

# Start development server
npm start
# OR
ng serve

# Build for production
npm run build
# OR
ng build

# Run tests
npm test
# OR
ng test

# Watch mode (auto-rebuild)
npm run watch
```

The frontend runs on `http://localhost:4200` by default.

### Frontend Architecture

**Tech Stack**: Angular 17, TypeScript 5.4, RxJS, STOMP/WebSocket, Jasmine/Karma

**Project Structure**:
- `app/core/` - Core services, guards, interceptors, and models
  - `services/` - HTTP services for API communication (auth, order, menu, payment, etc.)
  - `guards/` - AuthGuard and RoleGuard for route protection
  - `interceptors/` - HTTP interceptors (likely for JWT token injection)
  - `models/` - TypeScript interfaces/models
- `app/features/` - Feature modules organized by user role
  - `auth/` - Login functionality
  - `mesero/` - Waiter dashboard and order creation
  - `cocinero/` - Cook dashboard for order preparation
  - `cajero/` - Cashier dashboard, billing, transactions, cash register closing
  - `admin/` - Admin layout with nested routes for dashboard, menu, tables, users, statistics
- `app/shared/` - Shared components, directives, and pipes
- `environments/` - Environment configuration (API URL: `http://localhost:8080/api`, WebSocket: `http://localhost:8080/ws`)

**Key Features**:
- Role-based routing with guards protecting each role's routes
- WebSocket service for real-time order updates (kitchen orders and waiter notifications)
- Reconnects automatically with 5-second delay if connection drops

**Services**:
All services use `providedIn: 'root'` for singleton pattern:
- auth.service, order.service, menu.service, table.service
- payment.service, bill.service, cash-register.service
- admin-dashboard.service, admin-menu.service, admin-users.service, admin-tables.service, admin-statistics.service
- websocket.service

## Real-Time Communication

The system uses WebSockets (STOMP over SockJS) for real-time updates:

**Backend**:
- Endpoint: `/ws` with SockJS fallback
- Message broker prefix: `/topic`
- Application destination prefix: `/app`
- NotificationService sends messages to topics via SimpMessagingTemplate

**Frontend**:
- Connects to `http://localhost:8080/ws`
- Subscribes to `/topic/kitchen` for all kitchen orders
- Subscribes to `/topic/waiter/{userId}` for waiter-specific notifications (ready orders)

## User Roles and Permissions

The system has four role types defined in `RoleType` enum:

1. **MESERO** (Waiter): Create orders, view their assigned tables
2. **COCINERO** (Cook): View and update order status in kitchen
3. **CAJERO** (Cashier): Generate bills, process payments, manage cash register
4. **ADMIN**: Full access to manage menu, users, tables, view statistics and reports

Each role has its own dashboard and restricted routes enforced by RoleGuard.

## Order Workflow

Order status progression (OrderStatus enum):
1. PENDIENTE (Pending) - Order created by waiter
2. EN_PREPARACION (In preparation) - Being prepared in kitchen
3. LISTO (Ready) - Finished cooking
4. ENTREGADO (Delivered) - Delivered to table
5. ESPERANDO_CUENTA (Waiting for bill) - Customer requested bill
6. PAGADO (Paid) - Payment completed

## Development Workflow

When working on features:

1. **Backend changes**: Modify in `ordering-system-backn/`, run `./mvnw spring-boot:run`
2. **Frontend changes**: Modify in `ordering-systems-front/`, Angular CLI auto-reloads
3. **Database changes**: Update entities in `model/` package, let Hibernate handle schema updates
4. **API changes**: Update controllers, services, DTOs, then update corresponding Angular services
5. **Testing**: Backend tests with `./mvnw test`, Frontend tests with `ng test`

## Common Development Patterns

**Backend**:
- Controllers use `@RestController` with `@RequestMapping("/api/...")`
- Services use `@Service` and `@Autowired` for dependency injection
- Entities use JPA annotations (`@Entity`, `@Id`, `@ManyToOne`, etc.)
- DTOs separate request/response objects from entities
- Global exception handling via `@RestControllerAdvice` in GlobalExceptionHandler

**Frontend**:
- Services inject HttpClient and make API calls to `environment.apiUrl`
- Components subscribe to service observables
- Guards protect routes based on authentication and role
- WebSocket service provides observables for real-time updates
- Standalone components (Angular 17+ pattern based on imports)

## Environment Configuration

**Backend**: `ordering-system-backn/src/main/resources/application.properties`
- Database URL, credentials
- JWT secret and expiration
- Server port
- Logging levels

**Frontend**: `ordering-systems-front/src/environments/`
- `environment.ts` for development
- API and WebSocket URLs

## Important Notes

- The backend must be running before starting the frontend
- MySQL database must be running and accessible
- Default database will be auto-created on first run
- CORS is configured only for localhost:4200 in development
- WebSocket requires both HTTP and WebSocket connections to be available
- Password encoding uses NoOpPasswordEncoder (security risk - needs bcrypt for production)
