# Franquicias API - Nequi Technical Test

API REST para la gestión de franquicias, sucursales y productos desarrollada en **Spring Boot**, con persistencia en **Supabase (PostgreSQL)**.

---

## 📋 Tabla de Contenidos

- [Descripción](#descripción)
- [Arquitectura y Tecnologías](#arquitectura-y-tecnologías)
- [Requisitos Previos](#requisitos-previos)
- [Instalación y Despliegue](#instalación-y-despliegue)
- [Documentación de API](#documentación-de-api)
- [Ejemplos de Uso](#ejemplos-de-uso)
- [Pruebas](#pruebas)
- [Buenas Prácticas Implementadas](#buenas-prácticas-implementadas)

---

## 🎯 Descripción

Sistema que permite gestionar franquicias con sus respectivas sucursales y productos.  
Cada franquicia puede tener múltiples sucursales, y cada sucursal maneja un catálogo de productos con su respectivo stock.

### Características Principales

✅ CRUD completo de franquicias, sucursales y productos  
✅ Gestión de stock de productos  
✅ Consulta de productos con mayor stock por sucursal  
✅ API REST con validaciones robustas  
✅ Persistencia en **Supabase (PostgreSQL gestionado)**  
✅ Despliegue en la nube con Terraform y AWS ECS  
✅ Arquitectura limpia y escalable

---

## 🏗️ Arquitectura y Tecnologías

### Stack Tecnológico

- **Java 17**
- **Spring Boot 3.2.0**
- **Supabase (PostgreSQL)**
- **Maven** - Gestión de dependencias
- **Docker Desktop** - Ejecución de contenedores
- **Lombok** - Reducción de código boilerplate

### Estructura del Proyecto

```
src/
├── main/java/com/nequi/franquicias/
│   ├── controller/     # Controladores REST
│   ├── service/        # Lógica de negocio
│   ├── repository/     # Acceso a datos (Spring Data JPA)
│   ├── model/          # Entidades de dominio
│   ├── dto/            # Data Transfer Objects
│   ├── exception/      # Manejo de excepciones
│   └── mapper/         # Conversión de entidades y DTOs
└── resources/
```

## 📦 Requisitos Previos
### Opción 1: Con Docker Desktop

Docker Desktop instalado y en ejecución

Imagen de Java 17 o contenedor base disponible

### Opción 2: Sin Docker

Java 17+

Maven 3.9+

🗄️ La base de datos está alojada en Supabase, por lo que solo necesitas configurar la conexión.

## ⚙️ Configuración de Supabase

Crea un proyecto en https://supabase.com

Obtén tus credenciales:

- `host`

- `port`

- `database`

- `user`

- `password`

Configura tu archivo application.properties:
```bash
spring.datasource.url=jdbc:postgresql://<host>:<port>/<database>
spring.datasource.username=<user>
spring.datasource.password=<password>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## 🚀 Instalación y Despliegue
### Clonar el Repositorio

```bash
git clone <repository-url>
cd franquicias-api
```

### Ejecución con Maven

```bash
mvn clean install
mvn spring-boot:run
```

### Ejecución con Docker Desktop (opcional)

Puedes crear una imagen local y levantarla con:

```bash
docker build -t franquicias-api .
docker run -p 8080:8080 franquicias-api
```

### 📚 Documentación de API

#### 1️⃣ Franquicias

```http
Método	Endpoint	Descripción
POST	/api/franquicias	Crear franquicia
PUT	/api/franquicias/{id}	Actualizar nombre de franquicia
```

#### 2️⃣ Sucursales

```http
Método	Endpoint	Descripción
POST	/api/franquicias/{franquiciaId}	Agregar sucursal
PUT	/api/sucursales/{id}	Actualizar nombre
```

#### 3️⃣ Productos

```http
Método	Endpoint	Descripción
POST	/api/sucursal/{sucursalId}	Crear producto
DELETE	/api/sucursal/{sucursalId}/producto/{productoId}	Eliminar producto
PUT	/api/productos/{id}/stock	Modificar stock
PUT	/api/productos/{id}/nombre	Actualizar nombre
```

#### 4️⃣ Consultas

```http
Método	Endpoint	Descripción
GET	/api/max-stock/franquicia/{franquiciaId}	Productos con más stock por sucursal
```

## 💡 Ejemplo de flujo completo (cURL)

```bash
# 1. Crear franquicia
curl -X POST http://localhost:8080/api/franquicias \
-H "Content-Type: application/json" \
-d '{"nombre":"Franquicia Nacional"}'

# 2. Crear sucursal
curl -X POST http://localhost:8080/api/franquicias/1 \
-H "Content-Type: application/json" \
-d '{"nombre":"Sucursal Centro"}'

# 3. Agregar producto
curl -X POST http://localhost:8080/api/sucursal/1 \
-H "Content-Type: application/json" \
-d '{"nombre":"Laptop Dell", "stock":50}'

# 4. Modificar stock
curl -X PUT http://localhost:8080/api/productos/1/stock \
-H "Content-Type: application/json" \
-d '{"nuevoStock":100}'
```

## ✨ Buenas Prácticas Implementadas


✅ Arquitectura por capas

✅ Validaciones @NotNull, @Min, etc.

✅ Manejo global de excepciones

✅ DTOs con MapStruct o MapperUtils

✅ Código limpio con Lombok

✅ Compatible con despliegue en AWS ECS

## 👨‍💻 Autor

- Desarrollado por: Esteban Castaño
- Prueba Técnica - Nequi (2025)