package com.backend.cyberbytes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IaRequest {
    private List<Content> contents;
    private GenerationConfig generationConfig;

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public void setGenerationConfig(GenerationConfig generationConfig) {
        this.generationConfig = generationConfig;
    }

    @Data
    public static class Content{
        private List<Part> parts;

        public void setParts(List<Part> parts){
            this.parts = parts;
        }
    }

    @Data
    public static class Part{
        private String text;

        public Part(String text){
            this.text = text;
        }
    }

    @Data
    public static class GenerationConfig{
        private Double temperature = 0.8;
        private Integer maxOutPutTokens = 1000;
        private Integer candidateCount = 1;
        private String responseMimeType = "application/json";


        private String responseSchema = """
                "type": "object",
                    "properties": {
                      "resposta": {
                        "type": "string",
                        "description": "A sua resposta para a pergunta ao usuário, seguindo as regras passadas"
                      }
                    },
                    "required": [
                      "resposta"
                    ]
                  }
                """;


        public void setMaxOutPutTokens(Integer maxOutPutTokens) {
            this.maxOutPutTokens = maxOutPutTokens;
        }

        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }

        public void setResponseSchema(String responseSchema) {
            this.responseSchema = responseSchema;
        }

    }
}
