## Digi Mart

Welcome to Digi Mart (Digital Market), an application designed to transform your shopping
experience.
Digi Mart is a state-of-the-art digital marketplace that brings your favorite stores and products
right to your fingertips.
Built with an intuitive ui, you can manage products, create sale and monitor status to completion of
sale.

## Features

* Product Management: Add, edit and delete Products
* Sale Management: Add products to cart for sale, create sale and track status (PENDING, CANCELLED,
  SOLD), complete payment for sales.
* Inventory Management: Manage and track inventory levels for products and sales.
* Sales Reporting: Generate report, display top products and performance, and also export reports
  as CSV files.

## Prerequisites

* Android Studio Jelly Fish
* Android gradle plugin version 8.4.2
* Kotlin version 1.9.20
* Java 17

## Getting Started

* Clone the repository git clone https://github.com/kelienzo/DigiMart.git
* Open the project in android studio
* Configure if any errors and allow to build
* Run the app

## Architecture Flow

* Data The data source, contains the database, Dao and Entity classes
  Each package holds related features
  *Product Package*,
  *Sales Package*,
  *Inventory Package*
  Each package contains
* Repository: Contains the methods which interacts with the data source
* UseCases: Represent a specific feature or functionality of an app.
* Presentation: Contains UI components and view model

## Technologies

* Kotlin: The programming language used (Google's preferred language for Android app development).
* MVVM: Cleanly separate an application's business and presentation logic from its user interface (
  UI)
* Jetpack Compose: Android’s recommended modern toolkit for building native UI.
* Dagger Hilt: Dependency injection library for Android that reduces the boilerplate of doing manual
  dependency injection in your project.
* View Model: Exposes state to the UI and encapsulates related business logic
* Room persistence library: Provides an abstraction layer over SQLite to allow fluent database
  access.
* Kotlin Coroutines: For asynchronous operations.
* Kotlin Flows: For reactive programming.
* Gradle Version Catalogs: Add and maintain dependencies and plugins in a scalable way.
* Junit: For unit testing
* Google truth Library: Library for performing assertions in tests

## Next Updates

* Inventory to manage stock levels for products and sales.
* Sales reports and top performing products.
* Integrate with payment gateways for sales completion.
* Authorisation and support multiple user accounts with roles and permissions.

