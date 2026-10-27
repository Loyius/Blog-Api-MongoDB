# Blog API MongoDB

API REST de um blog simples, desenvolvida com **Spring Boot** e **MongoDB**. O projeto modela usuários, posts e comentários, oferecendo endpoints para cadastro de usuários e busca de posts por título, por período de datas e por texto livre (título, corpo ou comentários).

## Tecnologias

- Java 21
- Spring Boot 4.1.1
- Spring Data MongoDB
- Spring MVC (Web)
- Maven

## Modelo de domínio

- **User** — usuário do blog (`id`, `name`, `email`), com referência (`@DBRef`) para os `Post`s que escreveu.
- **Post** — post do blog (`id`, `date`, `title`, `body`, `author`), com uma lista embutida de comentários.
- **AuthorDTO** — snapshot do autor (id e nome) embutido no post/comentário, evitando a necessidade de buscar o `User` completo.
- **CommentDTO** — comentário embutido no post (`text`, `date`, `author`).

## Estrutura do projeto

```
src/main/java/com/loyius/blog_api_mongodb/
├── config/            # Instantiation: popula o banco com dados de exemplo no startup
├── domain/            # Entidades Post e User
├── dto/               # AuthorDTO, CommentDTO, UserDTO
├── repository/        # PostRepository e UserRepository (Spring Data MongoDB)
├── resources/          # Controllers REST (PostResource, UserResource)
│   ├── exception/     # Handler global de exceções e StandardError
│   └── util/          # URL (decode de parâmetros e parsing de datas)
└── service/           # Regras de negócio (PostService, UserService)
    └── exception/     # ObjectNotFoundException
```

## Pré-requisitos

- Java 21+
- Maven (ou use o wrapper `mvnw`/`mvnw.cmd` incluído no projeto)
- MongoDB em execução localmente na porta padrão (`27017`)

## Configuração

As configurações estão em `src/main/resources/application.properties`:

```properties
spring.application.name=blog_api_mongodb
spring.data.mongodb.uri=mongodb://localhost:27017/workshop_mongo
```

Ajuste a URI de conexão conforme necessário caso o MongoDB esteja em outro host/porta.

## Executando o projeto

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/macOS
./mvnw spring-boot:run
```

A aplicação sobe na porta padrão `8080`.

> Ao iniciar, a classe `Instantiation` limpa as coleções `user` e `post` e insere dados de exemplo (3 usuários, 2 posts e 3 comentários), útil para testes manuais da API.

## Endpoints

### Usuários — `/users`

| Método | Endpoint          | Descrição                              |
|--------|-------------------|-----------------------------------------|
| GET    | `/users`          | Lista todos os usuários                 |
| GET    | `/users/{id}`     | Busca um usuário por id                 |
| POST   | `/users`          | Cria um novo usuário                    |
| PUT    | `/users/{id}`     | Atualiza nome e e-mail de um usuário    |
| DELETE | `/users/{id}`     | Remove um usuário                       |
| GET    | `/users/{id}/posts` | Lista os posts de um usuário          |

Exemplo de corpo para criação/atualização (`UserDTO`):

```json
{
  "name": "Larissa Ramos",
  "email": "larissa@example.com"
}
```

### Posts — `/posts`

| Método | Endpoint                | Descrição                                                    |
|--------|--------------------------|---------------------------------------------------------------|
| GET    | `/posts/{id}`           | Busca um post por id                                           |
| GET    | `/posts/titlesearch`    | Busca posts por título (parâmetro `text`)                     |
| GET    | `/posts/fullsearch`     | Busca posts por texto (título, corpo ou comentários) e período de datas (parâmetros `text`, `minDate`, `maxDate`) |

Exemplos:

```
GET /posts/titlesearch?text=viagem
GET /posts/fullsearch?text=feliz&minDate=2018-03-20&maxDate=2018-03-25
```

### Tratamento de erros

Quando um recurso não é encontrado, a API retorna `404 Not Found` com o seguinte formato (`StandardError`):

```json
{
  "timestamp": 1700000000000,
  "status": 404,
  "error": "Não encontrado",
  "message": "Objeto não encontrado",
  "path": "/users/123"
}
```

## Testes

```bash
./mvnw test
```
