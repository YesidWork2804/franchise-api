# 🚀 Franchise API

API REST reactiva para gestionar franquicias, sucursales y productos.

## 📦 Stack

- **Java 17**
- **Spring Boot 3.5.13**
- **Spring WebFlux**
- **Spring Data R2DBC**
- **PostgreSQL / Supabase**
- **Docker & Docker Compose**
- **Terraform**
- **Render**

## ✨ Funcionalidades

- Crear franquicia
- Crear sucursal asociada a una franquicia
- Crear producto en una sucursal
- Eliminar producto de una sucursal
- Actualizar stock de producto
- Obtener el producto con mayor stock por sucursal de una franquicia
- Actualizar nombre de franquicia
- Actualizar nombre de sucursal
- Actualizar nombre de producto

## 🗂️ Variables de entorno

```env
DB_URL=r2dbc:postgresql://aws-1-us-east-1.pooler.supabase.com:5432/postgres?sslMode=require
DB_USER=postgres.vehadvbrxuiysspufvju
DB_PASSWORD=tu_password
PORT=8080
```

## ▶️ Ejecutar local

### Con Maven

```bash
./mvnw spring-boot:run
```

### Con Docker Compose

```bash
docker compose up --build
```

La API queda disponible en:

```text
http://localhost:8081
```

## ☁️ Despliegue

Desplegado en Render:

```text
https://franchise-api-oj4r.onrender.com
```

Health check:

```text
https://franchise-api-oj4r.onrender.com/actuator/health
```

## 🔌 Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/franchises` | Crear franquicia |
| PATCH | `/api/franchises/{id}` | Actualizar nombre de franquicia |
| POST | `/api/franchises/{id}/branches` | Crear sucursal |
| PATCH | `/api/franchises/{id}/branches/{branchId}` | Actualizar nombre de sucursal |
| POST | `/api/franchises/{id}/branches/{branchId}/products` | Crear producto |
| DELETE | `/api/franchises/{id}/branches/{branchId}/products/{productId}` | Eliminar producto |
| PATCH | `/api/franchises/{id}/branches/{branchId}/products/{productId}/stock` | Actualizar stock |
| PATCH | `/api/franchises/{id}/branches/{branchId}/products/{productId}` | Actualizar nombre de producto |
| GET | `/api/franchises/{id}/top-products` | Obtener producto con mayor stock por sucursal |

## 🧪 Ejemplos rápidos

### Crear franquicia

```bash
curl -X POST http://localhost:8081/api/franchises \
  -H "Content-Type: application/json" \
  -d '{"name":"Franquicia Demo"}'
```

### Actualizar stock

```bash
curl -X PATCH http://localhost:8081/api/franchises/1/branches/1/products/1/stock \
  -H "Content-Type: application/json" \
  -d '{"stock":25}'
```

## 🛠️ Infraestructura como código

La carpeta `infra/` contiene la configuración Terraform para gestionar el proyecto de Supabase con el provider oficial.

### Archivos

- `infra/providers.tf`
- `infra/variables.tf`
- `infra/main.tf`
- `infra/terraform.tfvars.example`

### Uso básico

```bash
cd infra
terraform init
terraform plan
terraform apply
```

### Qué gestiona

- Import del proyecto Supabase existente
- Configuración base del proyecto
- Ajustes de API de Supabase

## 🗄️ Persistencia

La persistencia usa **Supabase PostgreSQL** con conexión reactiva vía **R2DBC**.

## 🐳 Docker

El proyecto incluye:

- `Dockerfile`
- `docker-compose.yml`

Para despliegue en Render se usa imagen Docker con `jar` precompilado.

## ✅ Plus completados

- Docker
- Programación reactiva
- Despliegue en la nube
- Infraestructura como código
