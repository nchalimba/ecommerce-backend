# ECommerce Backend

This REST API serves as the backend of my e commerce application. This project is currently still in development.

## Technologies

This application is implemented with the following technologies:

- Java
- Spring Boot
- Spring Security
- JJWT and http only cookies
- Spring Data JPA
- PostgreSQL

## Features

The app contains the following endpoints:

### Public Endpoints

- Login via local authentication
- Registration
- Logout

## Authenticated Endpoints

- Get current user
- Update current user
- Get all categories
- Get a category by id
- Get all products
- Get a product by id

## Admin Endpoints

- Get user by id
- Update user by id
- Delete user by id
- Create category
- Update category by id
- Delete category by id
- Create product
- Update product by id
- Add category to product
- Remove category from product
- Delete product by id

## Future Features

The following features are still under development:

- Image upload endpoints for product and category
- Image download endpoints for product and category
- Shopping cart endpoints
- Order endpoints
- OAuth authentication with Google
- Password confirmation flow with email
- PayPal and Stripe integration
