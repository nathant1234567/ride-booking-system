# RideShare Management System

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![JUnit](https://img.shields.io/badge/Testing-JUnit%204-green.svg)](https://junit.org/junit4/)
[![GUI](https://img.shields.io/badge/GUI-Java%20Swing-blue.svg)](https://docs.oracle.com/javase/tutorial/uiswing/)

A desktop ride-sharing and transport booking management platform developed in Java. The system simulates a multi-passenger transport service supporting intelligent dynamic trip grouping, distance- and tariff-based pricing with promotional discounts, full booking lifecycle management (creation, amendment, cancellation), and mock payment processing.

Originally built as our 2nd year software engineering university project (achieving ~80%),showcases clean object-oriented architecture, modular design patterns, and cloud extensibility.

Since then I have added AWS secret key encryption 

---

## Key Features

- **Booking Management**:
  - Create, view, amend, and cancel ride bookings.
  - Multi-passenger capacity handling and luggage allocation.
  - Transparent cancellation and amendment fee calculation.
- **Dynamic Group Trip Allocation**:
  - Automatically matches and groups individual bookings heading to identical destinations at similar times into shared `Trip` pools based on vehicle capacity (Standard, MPV, Minibus).
  - Simulates dynamic journey duration based on passenger count, luggage count, and peak-hour traffic multipliers.
- **Dynamic Tariffs & Discounts**:
  - Distance estimation in miles/km with tiered vehicle tariffs.
  - Time-of-day demand pricing (rush hour penalties vs. off-peak discounts).
  - Promo codes (e.g., `SAVE10`), student/university loyalty discounts (`@kent.ac.uk`), and group travel discounts (5+ passengers).
- **Notification Engine**:
  - Observer-style notification service dispatching booking confirmations, amendment receipts, and passenger trip updates (dialog popups and console logging).
- **Payment Processing**:
  - Mock payment gateway checkout validating card details and generating transactional price breakdowns.
- **Admin & Fleet Oversight**:
  - Dedicated administrative dashboard to inspect active grouped trips, passenger manifests, and vehicle occupancies.

---

## Architectural Overview

The application adheres to a clean layered architecture with decoupled responsibilities:

```
src/
├── Main.java                        # Application entrypoint and bootstrapping
├── model/                           # Core domain entities
│   ├── Booking.java                 # Booking model (user, itinerary, vehicle type)
│   ├── Trip.java                    # Grouped trip container with capacity constraints
│   ├── User.java                    # Customer profile representation
│   ├── Payment.java                 # Transaction record
│   └── PriceBreakdown.java          # Itemized pricing (base, fees, discounts, final total)
├── logic/                           # Pure algorithmic calculations
│   ├── TariffCalculator.java        # Mileage, vehicle, day-of-week & peak tariff logic
│   └── DiscountCalculator.java      # Off-peak and late-night discount models
├── repository/                      # Data persistence layer
│   ├── BookingRepository.java       # Booking data storage and query operations
│   ├── TripRepository.java          # Grouped trip querying & matching algorithms
│   └── UserRepository.java          # User store
├── service/                         # Application business services
│   ├── BookingService.java          # Booking workflows, trip assignment & duration calculation
│   ├── NotificationService.java     # Notification strategy interface
│   ├── WindowNotificationService.java # GUI popup notification implementation
│   └── ConsoleNotificationService.java# Console output notification implementation
└── ui/                              # Java Swing desktop presentation layer
    ├── MainUI.java                  # Parent tabbed interface
    ├── BookingUI.java               # New booking creation view
    ├── ManageBookingsUI.java        # Search, edit & cancel bookings view
    ├── ActiveTripsUI.java           # Admin live trip monitoring view
    ├── TariffUI.java                # Tariff lookup & estimation tool
    ├── DiscountUI.java              # Off-peak discount guide
    ├── PaymentUI.java               # Payment checkout modal
    └── AmendBookingUI.java          # Booking amendment dialog
```

---

## Design Patterns & Engineering Highlights

- **Strategy / Observer Pattern**: [`NotificationService`](src/service/NotificationService.java) abstracts alert delivery, enabling switching between GUI dialogs (`WindowNotificationService`), console streams (`ConsoleNotificationService`), or cloud pub/sub without touching business logic.
- **Repository Pattern**: Centralized in-memory repositories decouple business workflows from underlying data stores, providing clear migration points for relational databases (PostgreSQL/MySQL) or cloud NoSQL (DynamoDB).
- **Model-View-Controller Separation**: Swing UI classes handle user interactions and dispatch actions directly to stateless logic calculators and services without embedding business rules in UI callbacks.
- **Defensive Data Handling & Validation**: Vehicle capacity limits, luggage constraints, and overlapping booking conflicts are validated before state commits.

---

## Getting Started

### Prerequisites
- **Java Development Kit (JDK)**: Version 17 or higher
- **IDE**: IntelliJ IDEA (recommended), Eclipse, or any standard Java IDE

### Installation & Execution

1. **Clone the repository**:
   ```bash
   git clone <YOUR_GITHUB_REPO_URL>
   cd comp5009a2
   ```

2. **Open in IntelliJ IDEA**:
   - Open IntelliJ IDEA and select **Open**.
   - Select the root `comp5009a2` directory.
   - Ensure the Project SDK is configured to Java 17+ (**File > Project Structure > Project > SDK**).
   - Verify that all `.jar` files in `lib/` (JUnit 4, Hamcrest) are added as project libraries.

3. **Run the Application**:
   - Navigate to `src/Main.java`.
   - Right-click and select **Run 'Main.main()'**.

---

## Running Automated Tests

The project includes an extensive suite of JUnit test suites covering business rules, tariff computations, repository behaviors, and trip grouping:

```bash
# In IntelliJ IDEA:
# Right-click the 'test' directory -> Run 'All Tests'
```

### Test Suite Highlights
- [`TariffCalculatorTest`](test/logic/TariffCalculatorTest.java) & [`DiscountCalculatorTest`](test/logic/DiscountCalculatorTest.java): Edge case tests for time-of-day multipliers and mileage bands.
- [`BookingServiceTest`](test/service/BookingServiceTest.java): Booking lifecycle tests (creation, capacity overflow, amendment, cancellation fee formulas).
- [`TripSyncTest`](test/service/TripSyncTest.java): Tests concurrent passenger additions and automatic trip splitting.

---

## Roadmap & Cloud Extensions

Planned extensions to evolve the system into a cloud-native platform:
- [ ] **AWS Secrets Manager & KMS**: Externalize payment keys and third-party API configurations with least-privilege IAM policies.
- [ ] **Amazon DynamoDB**: Migrate in-memory repository layer to persistent cloud NoSQL storage.
- [ ] **Amazon SNS / SES**: Wire real-time SMS and email dispatchers to [`NotificationService`](src/service/NotificationService.java).
- [ ] **RESTful API Backend**: Refactor domain services into a Spring Boot microservice.

---

## Authors & Acknowledgments

Developed as part of the **COMP5009 Software Engineering** module:
- **Nathan** - Booking management, trip assignment architecture & notification services
- **Jeremy** - Cancellation workflows & payment handling
- **Ibitola** - Booking amendments & payment integrations
- **MJ** - Tariff calculations & discount rules
