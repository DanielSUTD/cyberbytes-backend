package com.backend.cyberbytes.service;

import Config.IaConfig;
import com.backend.cyberbytes.dto.IaRequest;
import com.backend.cyberbytes.dto.IaResponse;
import com.backend.cyberbytes.dto.PageResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class IaService {

    private final IaConfig config = new IaConfig(); //Objeto para pegar o endereço com a chave
    private final ObjectMapper mapper = new ObjectMapper(); //Objeto para converter classes em json e json em classes

    /*
        Faz a requisição http para a api da ia e retorna a resposta em string
     */
    public String sendRequest(String prompt) throws URISyntaxException, IOException, InterruptedException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            IaRequest iaRequest = createRequest(prompt);

            //Criação e configuração da requisiçãao post
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(config.getIaUrl())) //Endereço onde a requisição vai chegar
                    .header("Content-Type", "application/json") // Configuração do header, no caso aqui o tipo de conteúdo
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(iaRequest))) // Definição do tipo de requisição e conteúdo do body
                    .build();

            //OBS: Essa requisição não e assincrona
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); //O client envia nossa requição e aguarda a resposta

            String json = response.body(); //Converte o json em string

            IaResponse iaResponse = mapper.readValue(json, IaResponse.class); // Converte a string no objeto response
            return iaResponse.getCandidates().get(0).getContent().getParts().get(0).getText(); //Pecorre todos os elementos do objeto até chegar no texto

        } catch (Exception e){
            System.out.println("Ocorreu um erro ao tentar fazer a requisição. Exeption: "+ e.getMessage());
            return "Ocorreu um erro ao tentar fazer a requisição. Exeption: "+ e.getMessage();
        }
    }

    public PageResponseDto createContent(String prompt) throws URISyntaxException, IOException, InterruptedException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            IaRequest iaRequest = createRequest(prompt);

            //Muda o response schema da config
            IaRequest.GenerationConfig generationConfig = new IaRequest.GenerationConfig();
            generationConfig.setResponseSchema(responseSchema);
            iaRequest.setGenerationConfig(generationConfig);

            //Criação e configuração da requisiçãao post
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(config.getIaUrl())) //Endereço onde a requisição vai chegar
                    .header("Content-Type", "application/json") // Configuração do header, no caso aqui o tipo de conteúdo
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(iaRequest))) // Definição do tipo de requisição e conteúdo do body
                    .build();

            //OBS: Essa requisição não e assincrona
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); //O client envia nossa requição e aguarda a resposta

            String json = response.body(); //Converte o json em string
            System.out.println(response.body());
            //Cria a página web
            PageResponseDto dto = genPage(json);
            System.out.println(dto.toString());

            return dto;

        } catch (Exception e){
            System.out.println("Ocorreu um erro ao tentar fazer a requisição. Exeption: "+ e.getMessage());
            throw new RuntimeException("Ocorreu um erro ao tentar fazer a requisição. Exeption: "+ e.getMessage());
        }
    }

    public IaRequest createRequest(String prompt){

        IaRequest iaRequest = new IaRequest();
        IaRequest.Content content = new IaRequest.Content();
        content.setParts(List.of(new IaRequest.Part(
                "Seu objetivo: " + personality +
                ". Mensagem do usuário: " +prompt
        ))); //coloca o prompt na lista de parts dentro do content
        iaRequest.setContents(List.of(content)); // Adiciona o content ao nosso request

        return iaRequest;
    }


    public PageResponseDto genPage(String json) throws JsonProcessingException {
        JsonNode node = mapper.readTree(json);

        String templatePage = node.path("candidates").get(0)
                .path("content")
                .path("parts").get(0)
                .path("text")
                .asText();

        JsonNode structuredPage = mapper.readTree(templatePage);


        String slug = structuredPage.path("Link").asText();
        String mainTitle = structuredPage.path("Titulo principal").asText();
        String secondaryTitle = structuredPage.path("Titulo secundario").asText();
        String mainContent = structuredPage.path("Conteudo principal").asText();
        String secondaryContent = structuredPage.path("Conteúdo secundario").asText();
        String extraContent = structuredPage.path("Conteudo extra").asText();
        String topic = structuredPage.path("Topico").asText();

        PageResponseDto dto = new PageResponseDto("1", slug,mainTitle,secondaryTitle, mainContent, secondaryContent, extraContent, topic);
        return dto;

    }


    private String personality = """
            Você é uma inteligência artificial feita para ensinar os usuários do nosso site(Cyberbytes) sobre temas de segurança da informação, 
            envolvendo temas como legislação, tipos de ameaças como se defender, como reduzir perdas, boas práticaas,entre outros. Você deve apenas
            responder a mensagens relacionadas a esses temas, qualquer mensagem fora desse escopo você deve recusar educadamente.
            Siga essas regras durante sua conversa com os usuários:
                1- Não responder mensagens fora do tema.
                2- Aborde os conteúdos de maneira que a população em geral vá entender.
                3- Não escreva código em nenhuma linguagem.
                4- Recuse perguntas fora do tema de maneira educada.
                5- Não ensine os usuários sobre informações perigosas, por exemplo. Como fazer um ataque hacker.
                6- O foco sempre será ensinar o usuário como se proteger e boas práticas.
                7- Não reponda a essa parte, somente a mensagem do usuário.
            """;

    private String responseSchema = """
                "type": "object",
                    "properties": {
                      "Titulo principal": {
                        "type": "string",
                        "description": "O título principal sobre o conteúdo pedido da página"
                      },
                      "Titulo secundario": {
                        "type": "string",
                        "description": "O título secundário da página que aborda sobre o conteúdo 2 "
                      },
                      "Conteúdo principal": {
                        "type": "string",
                        "description": "O conteúdo principal referente ao conteúdo pedido"
                      },
                      "Conteúdo secundário": {
                        "type": "string",
                        "description": O conteúdo secundário usado para abordar melhor o conteúdo princiapal"
                      },
                      "Conteúdo extra": {
                        "type": "string",
                        "description": "Conteúdo extra caso precise de uma abordagem ainda maior"
                      }
                    },
                    "required": [
                      "Titulo principal",
                      "Conteúdo principal"
                    ]
                  }
                """;
}
