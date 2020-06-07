# Component Example

Como funciona e como user o framework Clojure Component do Stuart Sierra? Dá uma olhada no artigo que eu escrevi
no Medium que tem tudo lá: https://medium.com/@renatoalencar/introdu%C3%A7%C3%A3o-ao-clojure-component-6ffbf388f6ae

## Como rodar?

### Lein

Você precisa configurar um banco de dados MongoDB e configurar as credenciais em uma variavel de ambiente
`DATABASE_URL` em formato de URL.

É só rodar `lein run-component` para rodar o exemplo implmentado usando Component ou rodar `lein run-atom`
para rodar o exemplo usando um Atom para gerenciar estado.

### Docker

É possível rodar tudo com `docker-compose` também é só rodar `docker-compose up`.

## Licença

[MIT](./LICENSE)
