# Plataforma HelpDesk - Desafio Técnico

Uma plataforma completa para gerenciamento de chamados de suporte técnico (HelpDesk), construída com arquitetura baseada em microsserviços, comunicação assíncrona e infraestrutura 100% conteinerizada.

---

## Descrição do Sistema

O sistema de HelpDesk tem como objetivo facilitar a abertura, acompanhamento e resolução de chamados técnicos. A plataforma permite que clientes criem tickets relatando problemas, enquanto administradores e técnicos podem assumir esses chamados, alterar status e resolvê-los. Todas as ações importantes geram notificações assíncronas para manter o fluxo de informação transparente.

## Arquitetura e Microsserviços

A solução foi projetada sob uma arquitetura distribuída, garantindo baixo acoplamento e responsabilidade única:

- **API Gateway (Porta 8080):** Ponto de entrada único para o Frontend. Faz o roteamento transparente das requisições para os microsserviços apropriados, isolando a rede interna.
- **User Service (Porta 8081):** Responsável por todo o ciclo de vida dos usuários (Clientes, Técnicos e Admins). Mantém os dados cadastrais e as regras de exclusão lógica (inativação).
- **Ticket Service (Porta 8082):** O coração do negócio. Gerencia a criação de chamados, atribuição de técnicos, mudança de status (Aberto, Em Progresso, Fechado) e regras de prioridade.
- **Notification Service (Porta 8083):** Microsserviço puramente reativo (consumidor). Escuta eventos do RabbitMQ e persiste o histórico de notificações geradas pela movimentação dos tickets.

## Tecnologias Utilizadas

**Backend:**
- Java 21
- Spring Boot 3.x (Spring Web, Spring Data JPA, Spring Validation)
- Spring Cloud Gateway
- Maven

**Infraestrutura & Mensageria:**
- PostgreSQL (Banco de dados relacional)
- RabbitMQ (Broker de mensageria)
- Docker & Docker Compose (Containerização e Orquestração)

## Pré-requisitos

Para rodar este projeto na sua máquina, você precisará ter instalado:
- **Docker** e **Docker Compose**
- *(Opcional, apenas para desenvolvimento local e testes manuais)*: Java 21+ e Maven 3.9+

## Instruções de Execução

Graças ao Docker, subir todo o ecossistema é extremamente simples. Na raiz do projeto, execute os passos abaixo:

1. **Configurar variáveis de ambiente:**
   Existe um arquivo chamado `.env.example` na raiz do projeto contendo o modelo de configuração necessário. Faça uma cópia dele e renomeie para `.env`:
   ```bash
   cp .env.example .env
   ```

2. **Subir os containers:**
   Execute o docker-compose para inicializar toda a infraestrutura:
   ```bash
   # Sobe os bancos, o RabbitMQ e os 4 microsserviços
   docker-compose up -d --build
   ```

O orquestrador cuidará da compilação (`maven build`) de cada microsserviço internamente e os iniciará na ordem correta usando dependências de rede.

**Para derrubar o ambiente:**
```bash
docker-compose down
```

**Para parar o ambiente:**
```bash
docker-compose stop
```
**Para iniciar o ambiente caso ele já exista:**
```bash
docker-compose start
```

## Principais Endpoints (Via API Gateway)

*O prefixo para acesso é sempre a porta do gateway `http://localhost:8080`*

### Usuários (`/api/users`)
- `POST /api/users`: Cria um novo usuário
- `GET /api/users`: Lista os usuários
- `GET /api/users/{id}`: Detalhes de um usuário
- `PUT /api/users/{id}`: Atualiza um usuário
- `DELETE /api/users/{id}`: Inativa (soft-delete) um usuário

### Tickets (`/api/tickets`)
- `POST /api/tickets`: Cria um chamado (Dispara notificação)
- `GET /api/tickets`: Lista os chamados (com paginação/filtros)
- `GET /api/tickets/{id}`: Detalhes de um chamado
- `PUT /api/tickets/{id}`: Atualiza status/prioridade ou atribui técnico (Dispara notificação)

### Notificações (`/api/notifications`)
- `GET /api/notifications`: Consulta o histórico de notificações geradas

## Eventos RabbitMQ

A comunicação inter-serviços utiliza o padrão **Publish-Subscribe** via filas do RabbitMQ, garantindo que o `ticket-service` não dependa do `notification-service` estar online.
Eventos emitidos:
- `TicketCreated`: Disparado quando um cliente abre um novo chamado.
- `TicketAssigned`: Disparado quando um técnico é alocado ao chamado.
- `TicketStatusChanged`: Disparado quando o status transiciona (ex: Resolvido/Fechado).

## Estratégia de Persistência

- **Database-per-service Pattern:** Cada microsserviço possui seu próprio schema lógico no PostgreSQL (`user_schema`, `ticket_schema`, `notification_schema`). Nenhum serviço acessa as tabelas do outro diretamente, reforçando o isolamento.
- **ORM:** O mapeamento é feito via Hibernate/JPA.
- **Inativação:** Exclusão de usuários utiliza _Soft Delete_ (campo `active=false`) para não quebrar a integridade histórica dos tickets atribuídos a eles.

## Principais Decisões Arquiteturais

1. **Centralização de Exceções:** Foi criado um tratamento unificado (GlobalExceptionHandler e ProblemDetails) para retornar mensagens de erro consistentes para o Frontend em todas as APIs.
2. **Docker Multi-stage Build:** Os Dockerfiles compilam o projeto do zero internamente usando imagem Maven, e depois movem apenas o arquivo `.jar` para uma imagem JRE limpa. Isso garante containeres extremamente pequenos e fáceis de rodar em qualquer lugar sem que o desenvolvedor precise ter o Java configurado na máquina.
3. **Variáveis de Ambiente (.env):** As conexões de rede não estão em *hardcode*. O projeto está preparado tanto para rodar isoladamente (localhost) durante o desenvolvimento e testes, quanto dentro da rede interna do Docker (usando os hostname virtuais do docker-compose).
4. **Testes e Mockito:** Implementação estratégica de testes focados nas regras de negócio e nos status HTTP, garantindo cobertura total sem lentidão excessiva.
