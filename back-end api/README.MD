## [ Back-end ] Biblioteca virtual pública
Objetivo - Colocar em prática o uso do TDD e do desenvolvimento de uma API REST.

### Tecnologias usadas
>- Spring
>    - Spring boot
>    - Spring data JPA
>    - Spring security
>- JWT
>- Bean validation
>- MongoDB
>- Junit
>- Maven
>- Heroku

## Autenticação

MÉTODO | ENDPOINT  | DESCRIÇÃO
---    | ---       | ---
POST   | /login    | Login usuário

## Usuário

MÉTODO | ENDPOINT                	| DESCRIÇÃO
---    | ---                     	| ---
GET    | /v1/usuario/all         	| Listar todos os usuários
GET	   | /v1/usuario/all/{ id }   	| Buscar usuário por id
GET	   | /v1/usuario/contatos/{ id }| Buscar contatos do usuário por id
POST   | /v1/usuario             	| Criar usuário
PUT    | /v1/usuario             	| Atualizar usuário
DELETE | /v1/usuario/{ id }      	| Remover usuário

## Livro

MÉTODO | ENDPOINT              | DESCRIÇÃO
---    | ---                   | ---
GET    | /v1/livro/all         | Listar todos os livros
GET	   | /v1/livro/all/{ id }  | Listar todos os livros do usuário
POST   | /v1/livro             | Criar livro
PUT    | /v1/livro             | Atualizar livro
DELETE | /v1/livro/{ id }      | Remover livro


## Criar usuário
```json
Envio: 
{
    "nome": "anderson",
    "senha": "password",
    "email": "anderson@email.com",
    "contatos": [
        {
            "tipo": 1,
            "endereco": "@twitter-do-usuário"
        }
    ]
}

Resposta:
{
    "id": "d50eaeb8-5312-4561-b58c-24fd4a268a27",
    "nome": "anderson",
    "senha": "$2a$10$NWm3OFWXhPsPIgXY9MYzMequ9JoTN5a/KIaVoJ/TiLF00Niom5JRO",
    "email": "anderson@email.com",
    "contatos": [
        {
            "id": "4ab95a47-85a1-44f0-be06-ae20c2c7e3ea",
            "tipo": 1,
            "endereco": "@twitter-do-usuário",
            "tipoLiteral": "TWITTER"
        }
    ],
    "permissoes": [
        {
            "id": "003c3623-78d8-4b36-93a0-02416234ce2d",
            "nomePermissao": "ROLE_USER"
        }
    ]
}
```
## Criar livro
```json
Envio:
{
    "isbn": "123-45-6789-012-3",
    "titulo": "Livro exemplo",
    "descricao": "Descrição do livro",
    "urlCapa": "https://images.com/photo-1490633874781",
    "categorias": [
        {
            "nome": "Nome da primeira categoria"
        },
        {
            "nome": "Nome da segunda categoria"
        }
    ],
    "idDonoLivro": "d50eaeb8-5312-4561-b58c-24fd4a268a27"
}

Resposta:
{
    "id": "78fc1df0-0966-4c0e-b44b-235025f908c6",
    "isbn": "123-45-6789-012-3",
    "titulo": "Livro exemplo",
    "descricao": "Descrição do livro",
    "urlCapa": "https://images.com/photo-1490633874781",
    "categorias": [
        {
            "nome": "Nome da primeira categoria"
        },
        {
            "nome": "Nome da segunda categoria"
        }
    ],
    "idDonoLivro": "d50eaeb8-5312-4561-b58c-24fd4a268a27"
}
```
