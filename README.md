# api-estacionamento
API com gerenciamento de Usuario, Login com JWT e gerenciamento de estacionamento

📖 README - API de Autenticação e Estacionamento
🚀 Sobre o projeto
API desenvolvida em Spring Boot + Spring Security para autenticação de usuários utilizando JWT e gerenciamento de estacionamento.
Ela permite cadastro/login de usuários, geração de token, e operações relacionadas a veículos e vagas de estacionamento.

⚙️ Tecnologias utilizadas
Spring Boot

Spring Security

JWT (Java JWT - Auth0)

JJWT

BCryptPasswordEncoder

Hibernate/JPA

Jakarta Validation

📌 Endpoints principais
🔑 Autenticação
POST /autenticacao/login

Entrada:

json
{
  "email": "usuario@email.com",
  "senha": "123456"
}
Saída (sucesso):

json
{
  "token": "jwt-gerado-aqui"
}
Saída (erro):

json
{
  "error": "Credenciais inválidas. Verifique e-mail e senha."
}
POST /usuario/cadastrar

Entrada:

json
{
  "nome": "Pedro",
  "email": "usuario@email.com",
  "senha": "123456"
}
Saída:

json
{
  "id": 1,
  "nome": "Pedro",
  "email": "usuario@email.com"
}
🚗 Estacionamento
POST /estacionamento/entrada

Registra a entrada de um veículo.

Entrada:

json
{
  "placa": "ABC1234",
  "modelo": "Fiat Uno",
  "cor": "Prata"
}
Saída:

json
{
  "mensagem": "Veículo registrado com sucesso",
  "vaga": 12
}
POST /estacionamento/saida

Registra a saída de um veículo e calcula o valor a pagar.

Entrada:

json
{
  "placa": "ABC1234"
}
Saída:

json
{
  "mensagem": "Saída registrada",
  "valor": 15.00
}
GET /estacionamento/vagas

Lista vagas disponíveis e ocupadas.

Saída:

json
[
  { "vaga": 1, "status": "ocupada" },
  { "vaga": 2, "status": "livre" }
]
🔒 Segurança
STATELESS → não há sessão, todas as requisições precisam de token.

JWT → gerado no login e enviado no header:

Código
Authorization: Bearer <token>
BCrypt → senhas são armazenadas criptografadas no banco.

🛠️ Configuração
Dependências Maven
xml
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>4.5.2</version>
</dependency>

<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
📊 Fluxo de autenticação e estacionamento
Usuário se cadastra em /usuario/cadastrar.

Faz login em /autenticacao/login e recebe JWT.

Usa o token para acessar endpoints protegidos de estacionamento.

Registra entrada/saída de veículos e consulta vagas.
