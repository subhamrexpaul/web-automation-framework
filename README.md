# 🧪 Web Automation Testing Framework

A production-grade **Selenium WebDriver** automation framework for testing web applications, built with **Java**, **TestNG**, **Maven**, and integrated with **Jenkins CI** pipeline.

![Java](https://img.shields.io/badge/Java-11-orange?style=flat-square&logo=openjdk)
![Selenium](https://img.shields.io/badge/Selenium-4.15-green?style=flat-square&logo=selenium)
![TestNG](https://img.shields.io/badge/TestNG-7.8-blue?style=flat-square)
![Maven](https://img.shields.io/badge/Maven-3.9-red?style=flat-square&logo=apache-maven)
![Jenkins](https://img.shields.io/badge/Jenkins-CI%2FCD-D24939?style=flat-square&logo=jenkins)

---

## 📋 Project Overview

| Feature | Details |
|---------|---------|
| **Application Under Test** | [SauceDemo](https://www.saucedemo.com/) - E-commerce Web App |
| **Design Pattern** | Page Object Model (POM) |
| **Total Test Cases** | 33+ (Functional + Regression) |
| **Parallel Execution** | Supported via TestNG XML |
| **Reporting** | ExtentReports with screenshots on failure |
| **CI/CD** | Jenkins declarative pipeline |
| **Data-Driven Testing** | TestNG `@DataProvider` + Apache POI (Excel) |
| **Cross-Browser** | Chrome, Firefox, Edge |

---

## 🏗️ Architecture

```
web-automation-framework/
├── pom.xml                          # Maven configuration & dependencies
├── Jenkinsfile                      # CI/CD pipeline definition
├── src/
│   ├── main/
│   │   ├── java/com/webautomation/
│   │   │   ├── base/
│   │   │   │   ├── BaseTest.java         # Test lifecycle (setup/teardown)
│   │   │   │   └── DriverFactory.java    # WebDriver factory (Chrome/FF/Edge)
│   │   │   ├── config/
│   │   │   │   └── ConfigReader.java     # Configuration loader (singleton)
│   │   │   ├── pages/                    # Page Object Model classes
│   │   │   │   ├── LoginPage.java
│   │   │   │   ├── HomePage.java
│   │   │   │   ├── ProductPage.java
│   │   │   │   ├── CartPage.java
│   │   │   │   ├── CheckoutPage.java
│   │   │   │   └── SearchResultsPage.java
│   │   │   ├── utils/
│   │   │   │   ├── WaitUtils.java        # Explicit wait helpers
│   │   │   │   ├── ScreenshotUtils.java  # Screenshot capture
│   │   │   │   └── ExcelDataReader.java  # Excel test data reader
│   │   │   └── reports/
│   │   │       ├── ExtentReportManager.java  # Report initialization
│   │   │       └── TestListener.java         # TestNG listener for reporting
│   │   └── resources/
│   │       ├── config.properties         # Framework configuration
│   │       └── log4j2.xml               # Logging configuration
│   └── test/
│       ├── java/com/webautomation/tests/
│       │   ├── LoginTests.java           # 7 login test cases
│       │   ├── HomePageTests.java        # 6 homepage/sort test cases
│       │   ├── ProductTests.java         # 4 product detail test cases
│       │   ├── CartTests.java            # 6 shopping cart test cases
│       │   ├── CheckoutTests.java        # 7 checkout flow test cases
│       │   └── SearchTests.java          # 3 filter/regression test cases
│       └── resources/
│           ├── smoke-suite.xml           # Smoke test suite
│           ├── regression-suite.xml      # Full regression suite
│           └── full-suite.xml            # Parallel execution suite
├── reports/                              # Generated HTML reports
├── screenshots/                          # Failure screenshots
└── logs/                                 # Log4j2 log files
```

---

## 🚀 Getting Started

### Prerequisites

- **Java JDK 11+** installed and `JAVA_HOME` configured
- **Maven 3.6+** installed and added to `PATH`
- **Chrome / Firefox / Edge** browser installed
- **Git** for version control

### Setup

```bash
# Clone the repository
git clone https://github.com/your-username/web-automation-framework.git
cd web-automation-framework

# Install dependencies
mvn clean install -DskipTests
```

### Running Tests

```bash
# Run smoke tests (quick validation)
mvn test -DsuiteXmlFile=src/test/resources/smoke-suite.xml

# Run full regression suite
mvn test -DsuiteXmlFile=src/test/resources/regression-suite.xml

# Run parallel execution suite
mvn test -DsuiteXmlFile=src/test/resources/full-suite.xml

# Run with specific browser
mvn test -Dbrowser=firefox -DsuiteXmlFile=src/test/resources/regression-suite.xml

# Run in headed mode (visible browser)
mvn test -Dheadless=false -DsuiteXmlFile=src/test/resources/smoke-suite.xml
```

---

## 📊 Test Cases Summary

| Module | Test Cases | Type |
|--------|-----------|------|
| **Login** | Valid login, invalid password, empty fields, locked user, logout, data-driven | Smoke + Regression |
| **Home Page** | Product count, sort A-Z/Z-A, sort by price, burger menu | Smoke + Regression |
| **Product** | Detail navigation, info display, add to cart, back button | Regression |
| **Cart** | Add single/multiple, remove, item details, continue shopping, empty cart | Smoke + Regression |
| **Checkout** | Valid checkout, missing fields (3 tests), cancel, complete order, confirmation | Smoke + Regression |
| **Search/Filter** | Filter reset on re-login, add all items, remove toggle | Regression |
| **Total** | **33+ test cases** | |

---

## 📈 Reporting

After test execution, an HTML report is generated at `reports/AutomationReport.html`.

**Report Features:**
- ✅ Pass/fail/skip status with execution time
- 📸 Automatic screenshots on test failure
- 📊 Dashboard with test statistics
- 🏷️ Test categorization by groups (smoke, regression)
- 💻 System info (OS, Java version, browser)

---

## 🔄 CI/CD Integration (Jenkins)

The `Jenkinsfile` defines a declarative pipeline:

1. **Checkout** — Clone source from SCM
2. **Build** — Compile the project (`mvn clean compile`)
3. **Smoke Tests** — Run smoke suite (every build)
4. **Regression Tests** — Run full regression (main branch only)
5. **Report & Archive** — Publish HTML report + screenshots

### Jenkins Setup

1. Install plugins: Maven Integration, TestNG Results, HTML Publisher
2. Create a new Pipeline job
3. Point SCM to this repository
4. The `Jenkinsfile` handles the rest automatically

---

## 🛠️ Key Design Patterns

- **Page Object Model (POM)** — Each page has its own class with locators and actions
- **Factory Pattern** — `DriverFactory` creates browser-specific WebDriver instances
- **Singleton Pattern** — `ConfigReader` and `ExtentReportManager` use singleton
- **Fluent Interface** — Page methods return `this` for chained calls
- **ThreadLocal** — Thread-safe WebDriver for parallel execution
- **Listener Pattern** — `TestListener` implements `ITestListener` for report hooks

---

## 📝 Technologies Used

| Technology | Purpose |
|-----------|---------|
| Java 11 | Programming language |
| Selenium WebDriver 4.15 | Browser automation |
| TestNG 7.8 | Test framework & assertions |
| Maven 3.9 | Build & dependency management |
| WebDriverManager 5.6 | Automatic driver binary management |
| ExtentReports 5.1 | Rich HTML test reporting |
| Apache POI 5.2 | Excel data reading for data-driven tests |
| Log4j2 2.22 | Structured logging |
| Jenkins | CI/CD pipeline |

---

## 📄 License

This project is licensed under the MIT License.
