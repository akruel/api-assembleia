# Aplicação de votação

Trello do Projeto: https://trello.com/b/LLuq5hxp/api-assembleia


## Objetivo
O objetivo da aplicação é controlar votações em pautas.

A aplicação permite que seja criada uma pauta, abrir uma sessão em uma pauta, votar e calcular o resultado dos votos da pauta.

## Documentação
Foi utilizado o Swagger para documentar.

Também foi utilizado o Trello para documentação das tarefas.

A documentação da API está disponível em https://api-assembleia.herokuapp.com/swagger-ui.html

## Testes
Para os testes foi utilizado MockMvc com Mockito e JUnit 5.

Os testes foram separados em camadas (controllers, services e mappers) e utilizado o mockito para simulação dos dados.

## Serviço de Banco de Dados
Foi utilizado um PostgreSQL as a Service do Heroku pela praticidade. 

Escolhido um BD SQL ao invés de um NoSQL por considerar importante um relacionamento mais forte entre os dados.

Foram criadas duas entidades: Agenda e Vote.

Vote possui uma PK com Agenda e CPF do Associado, mantendo a limitação de um voto por pauta também no lado do banco de dados.

## Integração
Foi utilizado o OpenFeign para provisionar a integração com a api externa, pela minha familiaridade com a implementação e também por ele disponibilizar integração com o Hystrix, que foi utilizado como Circuit Breaker.

Foi realizada a integração com uma api externa https://user-info.herokuapp.com/users.

Caso a api externa esteja indisponível ou esteja com um tempo de resposta muito elevado (maior que 2 segundos), o fallback será acionado salvando o voto no BD com um status de NOT_CONFIRMED.
Ao cálcular os resultados, a aplicação irá realizar uma varredura procurando por votos não confirmados a fim de tentar confirmá-los.

Caso o serviço continue indisponível, será informado que não é possível realizar a contagem de votos no momento.

Caso o serviço esteja disponível, será calculado os votos, excluindo aqueles dos quais os associados não estejam habilitados para votar.

## Performance
Pensando em perfomance, poderiam ser criadas replicas da API conforme a demanda das votações, aumentando a disponibilidade do serviço.

## Versionamento
Foi utilizado o versionamento na URI. Cada versão possui um pacote contendo seus contratos (request/response) e seus controller's.
Ao disponibilizar uma nova versão poderia ser criada um novo pacote apenas alterando o URI de v1 para v2, por exemplo.

## Mensageria e filas
Não foi utilizada mensageria/filas pelo meu pouco conhecimento na implementação, então optei utilizar o tempo da execução do teste com outras tarefas ao invés de gastá-lo na tentativa de implementar esse bônus

## Execução
A aplicação está disponibilizada no heroku: https://api-assembleia.herokuapp.com/

Caso deseje rodar localmente, deverá clonar ou baixar o repositório e executar na pasta do repositório o comando: 
```sh
docker build -t <tag-name> ./
```
  
Após conclusão, executar o container com o comando: 
```sh
docker run -p 8080:8080 -t <tag-name>
```

## Rotas da Aplicação
Há no trello um card com os anexos da collection e as environments do Postman: https://trello.com/c/cC4S92Vq/17-collection-do-postman

Há também a documentação no swagger, mas segue também as rotas aqui:

Para cadastrar uma pauta, buscar pautas ou buscar uma pauta específica: v1/agendas/{id}

Para abrir uma sessão: v1/agendas/openSession

Para cadastrar um voto e buscar votos: v1/votes

Para buscar o resultado da votação de uma pauta: v1/votes/sessionResult/{pautaID}

## Commit's
Para os commit's foram criadas branch's para cada tarefa levando o nome de card/caminho-do-card-no-trello e efetuados pull request's para cada tarefa concluída. Após o pull request, foi criada uma tag e deletada a branch.
