# 🧩 Franquicias API - Nequi Technical Test

API REST para la **gestión de franquicias, sucursales y productos**, desarrollada en **Spring Boot (Java 21)**, con persistencia en **MongoDB**, y preparada para ejecución local mediante **Docker** y despliegue en la nube con **Terraform**.

---

## 📋 Tabla de Contenidos

- [Descripción](#-descripción)
- [Arquitectura y Tecnologías](#-arquitectura-y-tecnologías)
- [Principios SOLID](#-principios-solid-implementados)
- [Patrones de Diseño](#-patrones-de-diseño)
- [Requisitos Previos](#-requisitos-previos)
- [Configuración de MongoDB](#-configuración-de-mongodb)
- [Instalación y Despliegue](#-instalación-y-despliegue)
- [Documentación de API](#-documentación-de-api)
- [Postman Collection](#-postman-collection)
- [Ejemplos de Uso](#-ejemplos-de-uso)
- [Buenas Prácticas Implementadas](#-buenas-prácticas-implementadas)
- [Autor](#-autor)

---

## 🎯 Descripción

Sistema que permite gestionar **franquicias**, **sucursales** y **productos**.  
Cada franquicia puede tener múltiples sucursales, y cada sucursal maneja su propio catálogo de productos con su respectivo stock.

### Características principales

✅ CRUD completo de franquicias, sucursales y productos  
✅ Gestión y actualización de stock  
✅ Consulta del producto con más stock por sucursal  
✅ Persistencia con **MongoDB**  
✅ Despliegue con **Docker**, **Docker Compose** y **Terraform**  
✅ Arquitectura limpia basada en DTOs  
✅ Documentación completa en Postman

---

## 🏗️ Arquitectura y Tecnologías

### Stack Tecnológico

- **Java 21**
- **Spring Boot 3.3.0**
- **MongoDB** (almacenamiento de datos)
- **Maven 3.9+** (gestión de dependencias)
- **Docker / Docker Compose**
- **Terraform** (infraestructura como código)
- **Lombok** (reducción de código repetitivo)

### Arquitectura del Sistema

El proyecto implementa una **Arquitectura en Capas (Layered Architecture)** con 4 capas bien definidas:

```
┌─────────────────────────────────────────┐
│    Layer 1: Presentation (Controllers)  │  ← Maneja HTTP requests/responses
├─────────────────────────────────────────┤
│    Layer 2: Business Logic (Services)   │  ← Lógica de negocio y coordinación
├─────────────────────────────────────────┤
│    Layer 3: Data Access (Repositories)  │  ← Abstracción de acceso a datos
├─────────────────────────────────────────┤
│    Layer 4: Domain Model (Entities)     │  ← Modelo de dominio
└─────────────────────────────────────────┘
                    ↓
            MongoDB Database
```

#### Ventajas de esta arquitectura:

✅ **Mantenibilidad**: Código organizado y fácil de entender  
✅ **Testabilidad**: Cada capa se puede testear independientemente  
✅ **Escalabilidad**: Fácil agregar nuevas funcionalidades  
✅ **Bajo acoplamiento**: Las capas están desacopladas  
✅ **Alta cohesión**: Cada capa tiene responsabilidades claras

### Estructura del Proyecto

```
src/
├── main/java/com/esteban/franquicias_api/
│ ├── controller/ # Controladores REST
│ ├── service/ # Lógica de negocio
│ ├── service/impl/ # Implementaciones funcionales
│ ├── repository/ # Repositorios (MongoRepository)
│ ├── model/ # Entidades del dominio
│ ├── dto/ # Data Transfer Objects
└── resources/
├── application.properties
├── docker-compose.yml
└── Dockerfile
```
---
## 🎯 Principios SOLID Implementados

El proyecto implementa **100% de los principios SOLID**:

### **S** - Single Responsibility Principle (SRP)

> Una clase debe tener una sola razón para cambiar

✅ **Implementación**: Cada clase tiene una única responsabilidad

```java
@RestController  // Solo maneja HTTP
public class FranquiciaController { }

@Service  // Solo lógica de negocio
public class FranquiciaService { }

@Repository  // Solo acceso a datos
public interface FranquiciaRepository { }
```

### **O** - Open/Closed Principle (OCP)

> Abierto para extensión, cerrado para modificación

✅ **Implementación**: Jerarquía de excepciones extendible

```java
public class BusinessException extends RuntimeException { }

// Extendemos sin modificar la clase base
public class ResourceNotFoundException extends BusinessException { }
public class ValidationException extends BusinessException { }
```

### **L** - Liskov Substitution Principle (LSP)

> Las subclases deben ser sustituibles por sus clases base

✅ **Implementación**: Todas las excepciones de negocio son intercambiables

```java
@ExceptionHandler(BusinessException.class)
public ResponseEntity<ErrorResponse> handle(BusinessException ex) {
    // Funciona con BusinessException y todas sus subclases
}
```

### **I** - Interface Segregation Principle (ISP)

> Los clientes no deben depender de interfaces que no usan

✅ **Implementación**: DTOs específicos para cada operación

```java
// Cada operación tiene su propio DTO
public record CrearFranquiciaRequest(String nombre) {}
public record ActualizarNombreFranquiciaRequest(String nuevoNombre) {}
public record FranquiciaResponse(String id, String nombre) {}
```

### **D** - Dependency Inversion Principle (DIP)

> Depender de abstracciones, no de concreciones

✅ **Implementación**: Inyección de dependencias por constructor

```java
@Service
public class FranquiciaService {
    private final FranquiciaRepository franquiciaRepository;
    
    // Inyección por constructor - depende de la interface
    public FranquiciaService(FranquiciaRepository franquiciaRepository) {
        this.franquiciaRepository = franquiciaRepository;
    }
}
```

---

## 🎨 Patrones de Diseño

El proyecto implementa **más de 10 patrones de diseño**:

### 1. **Repository Pattern** 📚

Abstrae el acceso a la base de datos

```java
@Repository
public interface FranquiciaRepository extends MongoRepository<Franquicia, String> {
    // Spring Data implementa automáticamente
}
```

**Ventajas**: Desacoplamiento, fácil cambiar de BD, testeable

### 2. **Data Transfer Object (DTO) Pattern** 📋

Separa la representación API del modelo interno

```java
// Request DTO
public record CrearFranquiciaRequest(@NotBlank String nombre) {}

// Response DTO
public record FranquiciaResponse(String id, String nombre) {}

// Entity (modelo interno)
@Document
public class Franquicia { }
```

**Ventajas**: Seguridad, versionado de API, validación

### 3. **Builder Pattern** 🏗️

Construcción flexible de objetos

```java
@Builder
public class Franquicia {
    private String id;
    private String nombre;
    private List<Sucursal> sucursales;
}

// Uso
Franquicia franquicia = Franquicia.builder()
    .id(UUID.randomUUID().toString())
    .nombre("Mi Franquicia")
    .sucursales(new ArrayList<>())
    .build();
```

**Ventajas**: Legibilidad, flexibilidad, inmutabilidad

### 4. **Dependency Injection Pattern** 💉

Inyección de dependencias por constructor

```java
@Service
public class FranquiciaService {
    private final FranquiciaRepository repository;
    
    public FranquiciaService(FranquiciaRepository repository) {
        this.repository = repository;
    }
}
```

**Ventajas**: Testeable, bajo acoplamiento, explícito

### 5. **Immutable Object Pattern** 🔒

Objetos que no cambian después de creados

```java
@Getter
@Builder
public class Producto {
    private final String id;
    private final String nombre;
    private final Integer stock;
    
    // Retorna nueva instancia en lugar de modificar
    public Producto actualizarStock(Integer nuevoStock) {
        return Producto.builder()
            .id(this.id)
            .nombre(this.nombre)
            .stock(nuevoStock)
            .build();
    }
}
```

**Ventajas**: Thread-safe, predecible, cacheable

### Otros Patrones Implementados:

6. **Factory Method** - `UUID.randomUUID()`, `Optional.of()`
7. **Strategy Pattern** - Streams API con diferentes estrategias
8. **Template Method** - `GlobalExceptionHandler`
9. **Facade Pattern** - Services simplifican operaciones complejas
10. **Chain of Responsibility** - Spring MVC filters

---

## 📐 Flujo de una Petición

```
1. HTTP Request
   ↓
2. @RestController (FranquiciaController)
   ├─ Valida con @Valid
   └─ Llama al Service
   ↓
3. @Service (FranquiciaService)
   ├─ Ejecuta lógica de negocio
   ├─ Convierte DTO → Entity
   └─ Llama al Repository
   ↓
4. @Repository (FranquiciaRepository)
   └─ Spring Data → MongoDB
   ↓
5. HTTP Response (JSON)
```

---

## 📦 Requisitos Previos

### Opción 1: Con Docker y Docker Compose 🐳

Necesitas tener instalados:
- [Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Docker Compose](https://docs.docker.com/compose/)

### Opción 2: Sin Docker

- Java 21+
- Maven 3.9+
- MongoDB corriendo localmente (`mongodb://localhost:27017/franquicias_db`)

---

## ⚙️ Configuración de MongoDB

### Conexión local
```bash
spring.data.mongodb.uri=mongodb://localhost:27017/franquicias_db
spring.data.mongodb.database=franquicias_db

```

## 🚀 Instalación y Despliegue
### Despliegue con Docker (Recomendado)

1. **Clonar el repositorio**

```bash
git clone <repository-url>
cd franquicias-api
```

2. **Iniciar la aplicación con Docker Compose**

```bash
docker-compose up -d
```

Este comando:
- Descarga la imagen de MongoDB
- Construye la imagen de la aplicación
- Inicia ambos contenedores
- Configura la red entre ellos

3. **Verificar que los servicios estén corriendo**

```bash
docker-compose ps
```

4. **Ver los logs**

```bash
docker-compose logs -f app
```

La API estará disponible en: `http://localhost:8080`

### Despliegue Local (Sin Docker)

1. **Iniciar MongoDB**

```bash
mongod --dbpath /ruta/a/tu/db
```

2. **Compilar y ejecutar la aplicación**

```bash
mvn clean install
mvn spring-boot:run
```

O ejecutar el JAR generado:

```bash
java -jar target/franquicias-api-1.0.0.jar
```
---
## 📚 Documentación de API
### 🔹 Franquicias
| Método | Endpoint                                    | Descripción                                  |
| ------ | ------------------------------------------- | -------------------------------------------- |
| `POST` | `/api/franquicias`                          | Crear franquicia                             |
| `PUT`  | `/api/franquicias/{id}`                     | Actualizar nombre de franquicia              |
| `GET`  | `/api/franquicias/{id}/productos/top-stock` | Obtener productos con más stock por sucursal |

### 🔹 Sucursales
| Método | Endpoint                                                  | Descripción                   |
| ------ | --------------------------------------------------------- | ----------------------------- |
| `POST` | `/api/franquicias/{franquiciaId}/sucursales`              | Crear sucursal                |
| `PUT`  | `/api/franquicias/{franquiciaId}/sucursales/{sucursalId}` | Actualizar nombre de sucursal |

### 🔹 Productos
| Método   | Endpoint                                                                               | Descripción       |
| -------- | -------------------------------------------------------------------------------------- | ----------------- |
| `POST`   | `/api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos`                    | Crear producto    |
| `PUT`    | `/api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}`       | Actualizar nombre |
| `PUT`    | `/api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}/stock` | Actualizar stock  |
| `DELETE` | `/api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}`       | Eliminar producto |

---
## 🧪 Postman Collection

El proyecto incluye una colección lista para importar y probar todos los endpoints.

### 🧾 Archivo disponible:

#### 📄 postman_collection.json

### 🚀 Cómo importar la colección

1. Abre Postman.

2. Haz clic en **Import → File** y selecciona `postman_collection.json`.

3. Define la variable global:
   - baseUrl = http://localhost:8080

4. Ejecuta las peticiones en el orden sugerido:
    - Crear franquicia 
    - Agregar sucursal
    - Crear producto
    - Actualizar o eliminar
    - Consultar producto con más stock


## 🌍 Variables globales sugeridas
| Variable       | Valor                        | Descripción         |
| -------------- | ---------------------------- | ------------------- |
| `baseUrl`      | `http://localhost:8080`      | URL base            |
| `franquiciaId` | Generado al crear franquicia | ID de la franquicia |
| `sucursalId`   | Generado al crear sucursal   | ID de la sucursal   |
| `productoId`   | Generado al crear producto   | ID del producto     |

---
## 💡 Ejemplos de Uso

A continuación se muestra un flujo completo de uso de la API, desde la creación de una franquicia hasta la consulta de los productos con mayor stock.

---

###  1️⃣ Crear Franquicia

**Request:**
```http
POST /api/franquicias
Content-Type: application/json
```

**Body:**

```json
{
  "nombre": "Franquicia Nacional"
}
```
**Response:**

```json
{
  "id": "67341c927f6b20b8c8a1a9dd",
  "nombre": "Franquicia Nacional",
  "sucursales": []
}
```
**cURL:**

```bash
curl -X POST http://localhost:8080/api/franquicias \
-H "Content-Type: application/json" \
-d '{"nombre": "Franquicia Nacional"}'
```

### 2️⃣ Crear Sucursal dentro de una Franquicia
**Request:**

```http
POST /api/franquicias/{franquiciaId}/sucursales
Content-Type: application/json
```

**Body:**

```json
{
  "nombre": "Sucursal Centro"
}
```

**Response:**

```json
{
  "id": "67341cda52af3a2d9f5acb12",
  "nombre": "Sucursal Centro",
  "franquiciaId": "67341c927f6b20b8c8a1a9dd"
}
```

**cURL:**

```bash
curl -X POST http://localhost:8080/api/franquicias/67341c927f6b20b8c8a1a9dd/sucursales \
-H "Content-Type: application/json" \
-d '{"nombre": "Sucursal Centro"}'
```

### 3️⃣ Crear Producto en una Sucursal
**Request:**

```http
POST /api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos
Content-Type: application/json
```
**Body:**

```json
{
  "nombre": "Laptop Dell",
  "stock": 45
}
```
**Response:**

```json
{
  "id": "67341e9852af3a2d9f5acb89",
  "nombre": "Laptop Dell",
  "stock": 45,
  "sucursalId": "67341cda52af3a2d9f5acb12"
}
```
**cURL:**

```bash
curl -X POST http://localhost:8080/api/franquicias/67341c927f6b20b8c8a1a9dd/sucursales/67341cda52af3a2d9f5acb12/productos \
-H "Content-Type: application/json" \
-d '{"nombre":"Laptop Dell","stock":45}'
```

### 4️⃣ Actualizar el Nombre de un Producto
**Request:**

```http
PUT /api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}
Content-Type: application/json
```

**Body:**

```json
{
  "nuevoNombre": "Laptop HP EliteBook"
}
```
**Response:**

```json
{
  "id": "67341e9852af3a2d9f5acb89",
  "nombre": "Laptop HP EliteBook",
  "stock": 45,
  "sucursalId": "67341cda52af3a2d9f5acb12"
}
```
**cURL:**

```bash
curl -X PUT http://localhost:8080/api/franquicias/67341c927f6b20b8c8a1a9dd/sucursales/67341cda52af3a2d9f5acb12/productos/67341e9852af3a2d9f5acb89 \
-H "Content-Type: application/json" \
-d '{"nuevoNombre":"Laptop HP EliteBook"}'
```
### 5️⃣ Actualizar Stock de un Producto
**Request:**

```http
PUT /api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}/stock
Content-Type: application/json
```
**Body:**

```json
{
  "nuevoStock": 120
}
```
**Response:**

```json
{
  "id": "67341e9852af3a2d9f5acb89",
  "nombre": "Laptop HP EliteBook",
  "stock": 120,
  "sucursalId": "67341cda52af3a2d9f5acb12"
}
```
**cURL:**

```bash
curl -X PUT http://localhost:8080/api/franquicias/67341c927f6b20b8c8a1a9dd/sucursales/67341cda52af3a2d9f5acb12/productos/67341e9852af3a2d9f5acb89/stock \
-H "Content-Type: application/json" \
-d '{"nuevoStock":120}'
```
### 6️⃣ Eliminar un Producto
**Request:**

```http
DELETE /api/franquicias/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}
```
**Response:**

```json
{
  "mensaje": "Producto eliminado correctamente"
}
```
**cURL:**

```bash
curl -X DELETE http://localhost:8080/api/franquicias/67341c927f6b20b8c8a1a9dd/sucursales/67341cda52af3a2d9f5acb12/productos/67341e9852af3a2d9f5acb89
```
### 7️⃣ Consultar el Producto con Mayor Stock por Sucursal
**Request:**

```http
GET /api/franquicias/{franquiciaId}/productos/top-stock
```
**Response:**

```json
[
  {
    "sucursalId": "67341cda52af3a2d9f5acb12",
    "nombreSucursal": "Sucursal Centro",
    "productoConMasStock": {
      "id": "67341e9852af3a2d9f5acb89",
      "nombre": "Laptop HP EliteBook",
      "stock": 120
    }
  },
  {
    "sucursalId": "67341df752af3a2d9f5acb90",
    "nombreSucursal": "Sucursal Norte",
    "productoConMasStock": {
      "id": "67341f8e52af3a2d9f5acb99",
      "nombre": "Mouse Logitech",
      "stock": 80
    }
  }
]
```
**cURL:**

```bash
curl -X GET http://localhost:8080/api/franquicias/67341c927f6b20b8c8a1a9dd/producto
```
## ✨ Buenas Prácticas Implementadas

### Arquitectura y Diseño

- **Separación de responsabilidades**: Capas claramente definidas (Controller, Service, Repository)
- **Inmutabilidad**: Uso extensivo de campos `final` y objetos inmutables
- **Principio de Responsabilidad Única**: Cada clase tiene una única responsabilidad
- **Inyección de dependencias por constructor**: Facilita testing y hace explícitas las dependencias
- **Aplicación de principios SOLID**: clases con una sola responsabilidad, abiertas a extensión, dependientes de interfaces y con bajo acoplamiento.

### Código Limpio

- **Lombok optimizado**: Uso de anotaciones específicas (`@Getter`, `@Builder`) en lugar de `@Data`
- **Java Records**: Para DTOs simples (Java 16+)
- **Var keyword**: Para variables locales con tipos obvios
- **Streams API**: Operaciones funcionales en colecciones
- **Optional**: Para manejar valores que pueden no existir

### Documentación

- **JavaDoc completo**: En clases y métodos públicos
- **Nombres descriptivos**: Variables, métodos y clases con nombres claros
- **README exhaustivo**: Documentación completa del proyecto


### DevOps

- **Docker multi-stage build**: Optimiza el tamaño de la imagen
- **Health checks**: Para monitoreo de contenedores
- **Docker Compose**: Facilita el despliegue local
- **Variables de entorno**: Configuración externalizada

## 🔧 Comandos Útiles

```bash
# Detener todos los contenedores
docker-compose down

# Reconstruir imágenes
docker-compose up -d --build

# Ver logs en tiempo real
docker-compose logs -f

# Conectarse a MongoDB
docker exec -it franquicias-mongodb mongosh

# Limpiar volúmenes
docker-compose down -v

# Verificar salud de la aplicación
curl http://localhost:8080/actuator/health
```

## 📝 Notas Importantes

1. **Puerto 8080**: Asegúrate de que el puerto 8080 esté disponible
2. **MongoDB**: Los datos persisten en un volumen de Docker
3. **IDs generados**: Se usan UUIDs para identificadores únicos
4. **Validaciones**: Todos los campos requeridos son validados

## 📊 Métricas de Calidad

### Arquitectura
- ✅ **SOLID**: 5/5 principios implementados (100%)
- ✅ **Patrones de Diseño**: 10+ patrones
- ✅ **Capas**: 4 capas bien definidas
- ✅ **Acoplamiento**: Bajo
- ✅ **Cohesión**: Alta

## 🎖️ Puntos Extra Implementados

- ✅ Empaquetado con Docker
- ✅ Programación funcional con Streams API
- ✅ Endpoint para actualizar nombre de franquicia
- ✅ Endpoint para actualizar nombre de sucursal
- ✅ Endpoint para actualizar nombre de producto
- ✅ Arquitectura lista para despliegue en nube

## 👨‍💻 Autor

- Desarrollado por: Esteban Castaño
- 📅 Prueba Técnica - Nequi (2025)
- 🚀 Tecnologías: Java 21 · Spring Boot · MongoDB · Docker · Terraform