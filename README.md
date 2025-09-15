
# ⚙️ CyberBytes – Back-End

O **CyberBytes** nasceu com um objetivo claro: **tornar a segurança digital acessível e compreensível para todos**.  
Vivemos em um mundo onde ataques virtuais são cada vez mais sofisticados, e o conhecimento se tornou a melhor defesa.

O **back-end** do projeto é responsável por:
* **Autenticação e segurança** dos usuários.  
* **Processamento e persistência de dados**.  
* **Controle de acesso robusto** para proteger informações sensíveis.  

---

## 🚀 Principais Funcionalidades
* **API RESTful** desenvolvida em **Java com Spring Boot**.  
* **Autenticação com JWT** para emissão de tokens seguros.  
* **Permissões avançadas** com Spring Security.  
* **Integração com PostgreSQL** para armazenamento de dados.  
* Suporte para **envio de e-mails** (SMTP).  

---

## 🛠️ Tecnologias Utilizadas
* **Java 17+**  
* **Spring Boot**  
* **Spring Security + JWT**  
* **Spring Data JPA / Hibernate**  
* **PostgreSQL**  
* **Lombok**  
* **HikariCP** (pool de conexões)  

---

## 🖥️ Como Executar Localmente

### 📋 Pré-requisitos
Antes de iniciar, instale e configure:
* [Java 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)  
* [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download/)  
* [PostgreSQL](https://www.postgresql.org/download/) e [pgAdmin](https://www.pgadmin.org/download/)

---

### ⚡ Passo a Passo

#### 1️⃣ **Clonar o repositório**
```bash
git clone https://github.com/cyberbytesunc/cyberbytes-backend.git
cd cyberbytes-backend
````

#### 2️⃣ **Configurar o Banco de Dados no pgAdmin**

1. Abra o **pgAdmin** e crie um novo banco:

   ```
   Nome: cyberbytes
   Usuário: postgres
   Senha: 12345
   ```

   > ⚠️ **Importante**: a senha **precisa ser a mesma** configurada no arquivo `application.properties`
   > (por padrão, `spring.datasource.password=12345`).
   > Caso utilize outra senha, **atualize também o arquivo**.

#### 3️⃣ **Verificar o arquivo `application.properties`**

O projeto já vem com uma configuração padrão em
`src/main/resources/application.properties`:

```properties
spring.application.name=cyberbytes
server.port=8080

# Banco de dados
spring.datasource.url=jdbc:postgresql://localhost:5432/cyberbytes
spring.datasource.username=postgres
spring.datasource.password=12345
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
api.security.token.secret=my-secret-key

# Configuração de e-mail
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=
spring.mail.password=
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```

* **JWT\_SECRET**: já definido como `my-secret-key` por padrão.
* **E-mail**: as variáveis `spring.mail.username` e `spring.mail.password`
  devem ser preenchidas com as credenciais de um e-mail válido **caso queira testar envio de e-mails**.
* **IA/Gemini**: existe configuração para IA (`spring.ia.api.key`), mas não é obrigatória para rodar a API.

> Se mudar a senha do banco, ajuste `spring.datasource.password`.

#### 4️⃣ **Abrir e Rodar no IntelliJ IDEA**

1. No IntelliJ, vá em **File > Open** e selecione a pasta do projeto.
2. Aguarde o **Maven** baixar todas as dependências.
3. Abra a classe principal (provavelmente `CyberbytesBackendApplication.java`).
4. Clique em **Run**.

O servidor iniciará em:

```
http://localhost:8080
```

---

### 🔍 Testar a API

Use ferramentas como [Postman](https://www.postman.com/) ou [Insomnia](https://insomnia.rest/) para testar os endpoints.

* Exemplo de login (após criar um usuário pelo endpoint de registro):

  ```
  POST http://localhost:8080/auth/login
  ```

  O retorno será um **token JWT**, que deve ser enviado no cabeçalho `Authorization`:

  ```
  Authorization: Bearer <seu_token>
  ```

---

<h2 id="colab">🤝 Colaboradores</h2>
<p>Um agradecimento especial a todas as pessoas que contribuíram para este projeto.</p>

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/DanielSUTD">
        <img src="https://github.com/DanielSUTD.png" width="100px;" alt="Daniel Umberto Profile Picture"/><br>
        <sub>
          <b>Daniel Umberto</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/snl0w">
        <img src="https://github.com/snl0w.png" width="100px;" alt="Khaled Ahmed Profile Picture"/><br>
        <sub>
          <b>Khaled Ahmed</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/software-Debug404">
        <img src="https://github.com/software-Debug404.png" width="100px;" alt="Kauan Davi Oliveira De Sá Profile Picture"/><br>
        <sub>
          <b>Kauan Davi Oliveira De Sá</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/Samueldasil">
        <img src="https://github.com/Samueldasil.png" width="100px;" alt="Samuel C. Braga Profile Picture"/><br>
        <sub>
          <b>Samuel Costa Braga</b>
        </sub>
      </a>
    </td>
  </tr>
  <tr>
    <td align="center">
      <a href="https://github.com/Vini-Bispo">
        <img src="https://github.com/Vini-Bispo.png" width="100px;" alt="Vinicius Bispo Profile Picture"/><br>
        <sub>
          <b>Vinicius Bispo</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/viniciusalr">
        <img src="https://github.com/viniciusalr.png" width="100px;" alt="Vinícius Rios Profile Picture"/><br>
        <sub>
          <b>Vinícius Rios</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/Lhgs-pog">
        <img src="https://github.com/Lhgs-pog.png" width="100px;" alt="Lucas Henrique Gonçalves Souto Profile Picture"/><br>
        <sub>
          <b>Lucas Henrique Gonçalves Souto</b>
        </sub>
      </a>
    </td>
  </tr>
</table>

---

## 💡 Feedback

Contribuições, sugestões ou comentários são sempre bem-vindos para melhorar o projeto.

---

### 🔗 Repositórios Relacionados

* **Front-End**: [CyberBytes Frontend](https://github.com/cyberbytesunc/cyberbytes-frontend)
* **Back-End**: [CyberBytes Backend](https://github.com/cyberbytesunc/cyberbytes-backend)
