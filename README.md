# API de Agência de Viagens

API RESTful para gerenciamento de destinos de viagem com Spring Boot, PostgreSQL e Spring Security.

## Tecnologias

- Spring Boot 3.1.5
- Spring Data JPA
- Spring Security
- PostgreSQL
- Java 17

## Instalação e Configuração

### 1. Instalar PostgreSQL

```bash
brew install postgresql@15
brew services start postgresql@15
```

### 2. Criar Banco de Dados

```bash
createdb viagensdb
```

### 3. Configurar Credenciais

Edite `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/viagensdb
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

### 4. Executar Aplicação

```bash
mvn spring-boot:run
```

Aplicação disponível em: `http://localhost:8080`

## Segurança

### Perfis

- **USER**: Visualizar e avaliar destinos
- **ADMIN**: Criar, editar e excluir destinos

### Permissões

**Públicos:**
- `GET /api/destinos`
- `GET /api/destinos/{id}`
- `GET /api/destinos/pesquisar`
- `GET /api/destinos/avaliacao`
- `POST /api/auth/registro`
- `POST /api/auth/login`

**Autenticados:**
- `POST /api/destinos/{id}/avaliar`

**ADMIN:**
- `POST /api/destinos`
- `PUT /api/destinos/{id}`
- `DELETE /api/destinos/{id}`

## Uso

### Registrar Usuário

Senha: mínimo 8 caracteres (maiúscula, minúscula, número, caractere especial).

```bash
curl -X POST http://localhost:8080/api/auth/registro \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario1",
    "email": "usuario1@email.com",
    "password": "Senha123!"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario1",
    "password": "Senha123!"
  }'
```

Resposta contém token JWT em `data.token`.

### Usar Token

```bash
TOKEN="seu-token-aqui"

curl -X POST http://localhost:8080/api/destinos/1/avaliar \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"nota": 8.5}'
```

### Criar Usuário ADMIN

```sql
INSERT INTO usuarios_roles (usuario_id, role_id)
SELECT u.id, r.id
FROM usuarios u, roles r
WHERE u.username = 'admin' AND r.nome = 'ADMIN';
```

## Endpoints

**Autenticação:**
- `POST /api/auth/registro` - Registrar
- `POST /api/auth/login` - Login
- `GET /api/auth/usuario-atual` - Usuário atual

**Destinos:**
- `GET /api/destinos` - Listar
- `GET /api/destinos?disponiveis=true` - Disponíveis
- `GET /api/destinos/{id}` - Buscar
- `GET /api/destinos/pesquisar?termo=...` - Pesquisar
- `GET /api/destinos/avaliacao?minima=...` - Por avaliação
- `POST /api/destinos` - Criar (ADMIN)
- `PUT /api/destinos/{id}` - Atualizar (ADMIN)
- `DELETE /api/destinos/{id}` - Excluir (ADMIN)
- `POST /api/destinos/{id}/avaliar` - Avaliar (autenticado)

## Estrutura

```
src/main/java/com/agencia/viagens/
├── config/
│   ├── SecurityConfig.java
│   └── DataInitializer.java
├── controller/
│   ├── AuthController.java
│   └── DestinoController.java
├── dto/
├── exception/
├── model/
├── repository/
├── security/
├── service/
└── validation/
```

## Características

- Autenticação JWT
- Autorização por perfil (USER/ADMIN)
- Validação de senha forte
- Integração PostgreSQL
- Spring Data JPA
- Tratamento global de exceções
