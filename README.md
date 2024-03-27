# Aprenda+.tech

Este repositório contém uma REST API para uma plataforma de cursos online.

## Tecnologias

- Java 21
- Maven 3
- Spring Boot 3
- Spring Security 6


- MySQL 8
- Flyway
- Docker & Docker Compose


- JUnit 5
- RestAssured
- Testcontainers

## Como executar

Para executar a aplicação, basta clonar o repositório e executar a aplicação usando o IntelliJ IDEA ou usando o seguinte comando:

```shell
./mvnw spring-boot:run
```
Na raiz do projeto há um arquivo `docker-compose.yml`, que contém as configurações para subir um container MySQL, necessário para o funcionamento correto da aplicação, além de um container da própria API. Para subir os containers, basta executar o seguinte comando:

```shell
docker-compose up -d
```

## Sobre o design e arquitetura

A API foi desenvolvida utilizando uma ideia do padrão [Ports and Adapters](https://alistair.cockburn.us/hexagonal-architecture/), que separa as regras de negócio da lógica de infraestrutura e garante um alto nível de testabilidade, permitindo que os comportamentos dos componentes de domínio sejam testados de maneira isolada.

Algumas concessões foram feitas para simplificar o desenvolvimento, como utilizar os mesmos modelos tanto como entidades de domínio quanto para mapeamento de banco de dados, visto que não se trata de uma API complexa, com muitas regras de negócio. Em todo o caso, é possível refatorar a aplicação em direção do padrão [Domain Model](https://martinfowler.com/eaaCatalog/domainModel.html), por exemplo, a fim de separar melhor as camadas de domínio e infraestrutura.

## Possíveis melhorias e implementações futuras

- Melhorar o feedback da execução dos testes quando utilizando o CLI do Maven
- Adicionar logs na aplicação
- Permitir a geração de relatórios em CSV e XLSX
- Exigir uma senha forte para criação de usuários
- Criar profiles de testes no Maven
- Melhorar a legibilidade dos testes por meio de classes de fixtures (DSL), utilizando libs como a Faker, por exemplo
- Criar entidades de domínio para separar melhor as camadas de domínio e infraestrutura
- Refatorar os tipos de campos que representam enumerações (statuses, roles...) para enums