# Aplicação de votação

Trello do Projeto: https://trello.com/b/LLuq5hxp/api-assembleia


## Objetivo
O objetivo da aplicação é controlar votações em pautas.

A aplicação permite que seja criada uma pauta, abrir uma sessão em uma pauta, votar e calcular o resultado dos votos.

## Documentação
Foi utilizado o Swagger para documentar.

Também foi utilizado o Trello para documentação das tarefas.

A documentação da API está disponível em https://api-assembleia.herokuapp.com/swagger-ui.html

## Testes
Para os testes foi utilizado MockMvc com Mockito e JUnit 5.

Os testes foram separados em camadas (controllers, services e mappers) e utilizado o mockito para simulação dos dados.

## Integração
Foi utilizado o OpenFeign para provisionar a integração com a api externa, pela minha familiaridade com a implementação e também por ele disponibilizar integração com o Hystrix, que foi utilizado como Circuit Breaker.

Foi realizada a integração com uma api externa https://user-info.herokuapp.com/users.

Caso a api externa esteja indisponível ou esteja com um tempo de resposta muito elevado (maior que 2 segundos), o fallback será acionado salvando o voto no BD com um status de NOT_CONFIRMED.
Ao cálcular os resultados, a aplicação irá realizar uma varredura procurando por votos não confirmados a fim de tentar confirmá-los.

Caso o serviço continue indisponível, será informado que não é possível realizar a contagem de votos no momento.

Caso o serviço esteja disponível, será calculado os votos, excluindo aqueles dos quais os associados não estejam habilitados para votar.

## Execução
A aplicação está disponibilizada no heroku: https://api-assembleia.herokuapp.com/

Caso deseja rodar localmente, deverá clonar ou baixar o repositório e executar na pasta do repositório o comando: docker build -t <tag-name> ./
  
Após conclusão, executar o container com o comando: docker run -p 8080:8080 -t <tag-name>

