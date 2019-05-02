## [ Back-end ] Biblioteca virtual pública
Objetivo - Colocar em prática o uso do TDD e do desenvolvimento de API REST

### Tecnologias usadas
>- Spring
>    - Spring boot
>    - Spring data JPA
>    - Spring security
>- Bean validation
>- MongoDB
>- Junit
>- Maven
>- Heroku

## Endpoints
>- Autenticação
>   - POST Login usuário https://bvp.herokuapp.com/login  
>   - GET Logout usuário https://bvp.herokuapp.com/logout
>- Livro
>   - GET Listar todos os livros https://bvp.herokuapp.com/v1/livro/all
>   - GET Listar livros do usuário https://bvp.herokuapp.com/v1/livro/all/{ id do usuário }
>   - POST Criar livro https://bvp.herokuapp.com/v1/livro
>   - PUT Atualizar livro https://bvp.herokuapp.com/v1/livro
>   - DEL Remover livro por id https://bvp.herokuapp.com/v1/livro/{ id do livro }
>- Usuário
>   - GET Listar todos os usuários https://bvp.herokuapp.com/v1/usuario/all
>   -  GET Buscar usuário por id https://bvp.herokuapp.com/v1/usuario/{ id do usuário }
>   - POST Criar usuário https://bvp.herokuapp.com/v1/usuario
>   - PUT Atualizar usuário https://bvp.herokuapp.com/v1/usuario
>   - DEL Remover usuário por id https://bvp.herokuapp.com/v1/usuario/{ id do usuário }

## Motivação

- Objetivo
    - Conectar pessoas que tenham interesse em compartilhar materiais educacionais de forma **gratuita**.

- O Funcionamento
    - Partindo do princípio de que temos uma pessoa com um *livro* e interesse em doá-lo, esta pessoa cria um anúncio na plataforma com os detalhes do material em questão e adiciona algum meio de contato. Caso alguém tenha interesse no item anunciado, o interessado entra em contato com o doador e combina uma forma de receber o material.

- O Processo de doação e entrega/recebimento
    - É de inteira responsabilidade do doador e do beneficiário, sem qualquer tipo de interferência da plataforma.

- A Plataforma
    - O funcionamento principal não possui custo algum aos seus usuários.