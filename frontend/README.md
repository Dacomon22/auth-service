# Auth Frontend - Angular

## Overview

This project is a frontend authentication application built with Angular.
It supports login with email and password, as well as a simulated SSO (Single Sign-On) flow.

The application communicates with a backend service and properly handles success and error responses.

---

## Features

* Login with email and password
* Form validation
* Backend error handling
* SSO flow simulation
* JWT token handling
* Loading state management

---

## Architecture

The project follows a feature-based structure:

* `core/`: services and models
* `features/`: business logic (authentication)
* `shared/ui/`: reusable UI components

UI components are structured using Atomic Design:

* Atoms: small reusable components (e.g. feedback messages)
* Molecules: composed components (e.g. login form)
* Organisms: layout components (e.g. auth layout)
* Pages: feature-level components (e.g. login page)

---

## Styling

The project uses SCSS with BEM (Block Element Modifier) naming convention.

Examples:

* `login-form__button`
* `login-form__button--primary`
* `auth-layout__panel`

This improves readability and scalability of styles.

---

## SSO Flow

1. User clicks "Login with SSO"
2. Frontend requests authorization URL from backend
3. User is redirected (simulated)
4. Backend validates the code:

   * valid-code → success
   * invalid-code → error
5. Frontend displays the result

---

## Tech Stack

* Angular (Standalone Components)
* TypeScript
* RxJS
* Angular Material
* SCSS (BEM)

---

## Testing

Unit tests are implemented using Jest.

To run tests:

npm run test

---

## Run the project

Install dependencies:

npm install

Run the application:

ng serve

Open in browser:

http://localhost:4200

---

## Notes

* UI is separated from business logic
* Components are reusable and scalable
* Backend error messages are displayed to the user

---

## Author

David Castiblanco
