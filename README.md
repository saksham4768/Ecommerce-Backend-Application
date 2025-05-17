# 🛒 E-Commerce REST API - Spring Boot Project

A robust, modular, and **secure eCommerce backend application** built using **Spring Boot**. This project demonstrates key backend concepts including product management, cart handling, user registration/login, cookie-based JWT authentication, and role-based authorization.

---

## 📝 Project Summary

This project is a **Spring Boot-based RESTful API** for a basic eCommerce platform. It follows clean architecture and implements best practices like:

- 🔐 **Authentication & Authorization** using JWT (stored in cookies) and Spring Security
- 🧑‍💻 **Role-based access control** (`USER`, `ADMIN`)
- 📦 **Modular code structure** (Controllers, Services, DTOs, Models, Repositories)
- ⚠️ **Custom exception handling** with proper error responses
- 📄 **Pagination support** for scalable endpoints like product listing
- 📘 DTO mapping using **ModelMapper**
- 🔐 Endpoint protection using `UserDetailsService` and `@PreAuthorize`

---

## 🚀 Tech Stack

- 🧠 **Java 17**
- ⚙️ **Spring Boot**
- 🔒 **Spring Security**
- 🔐 **JWT Authentication** (with Cookies)
- 🗃️ **Spring Data JPA**
- 🔁 **ModelMapper**
- 📄 **DTO Pattern**
- 🧪 **H2 / MySQL** (In-memory or persistent DB)
- 🧰 **Lombok**

---

## 📚 Key Features

| Feature                         | Description                                                                 |
|---------------------------------|-----------------------------------------------------------------------------|
| 🔐 JWT + Cookie Authentication  | Secure user login using stateless JWT stored in cookies                    |
| 🧑‍💻 Role-Based Authorization   | Role-based control (`USER`, `ADMIN`) using Spring Security                 |
| 🧱 Modular Architecture         | Clean separation: Controller, Service, DTO, Entity, Repository             |
| 📄 DTO + ModelMapper            | Maps entities to DTOs for cleaner API contracts                            |
| ❌ Custom Exceptions             | Global exception handling with detailed structured responses               |
| 🔁 Pagination Support           | Product listing and others support Spring's `Pageable` interface           |
| 🛠️ CRUD Operations              | Full CRUD for Products, Categories, Orders, and Cart                       |
| 🔒 Secured Endpoints            | Only authorized roles can access protected resources                       |

---


---

## 🔐 Authentication & Security

- ✅ JWT stored in **HttpOnly Cookies** for enhanced security
- ✅ Integrated with **Spring Security**
- ✅ User data fetched using `UserDetailsService`
- ✅ Endpoints secured using `@PreAuthorize` and role-based logic
- ✅ Token validation and parsing handled via custom JWT utility classes

---

## 🔁 Pagination Example

List products with pagination:


Returns:
```json
{
  "content": [...],
  "pageNumber": 0,
  "pageSize": 2,
  "totalElements": 42,
  "totalPages": 21,
  "lastPage": false
}
```
## 🧾 Conclusion

This project showcases how to build a **secure, modular, and scalable RESTful API** using Spring Boot — covering all fundamental backend features for an eCommerce platform. From role-based access control and JWT cookie-based authentication to clean architecture with DTOs and pagination, it is an excellent reference for developers building production-grade APIs.

Whether you're a student, a developer learning Spring Boot, or someone preparing for backend interviews — this project can serve as a **hands-on guide** for real-world application development.

Feel free to fork the repo, explore the code, and expand the functionality by adding:
- Product image uploads
- Payment gateway integration
- Wishlist support
- Admin dashboards
- Swagger/OpenAPI documentation

Happy Coding! 💻✨

