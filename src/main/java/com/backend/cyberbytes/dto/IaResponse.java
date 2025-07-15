package com.backend.cyberbytes.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Representa a resposta completa da API de IA, mapeada a partir do JSON.
 * A anotação @Data do Lombok gera automaticamente getters, setters, toString, etc.
 * @JsonIgnoreProperties(ignoreUnknown = true) garante que o app não quebre se a API adicionar novos campos.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IaResponse {

    @JsonProperty("candidates")
    private List<Candidate> candidates;

    @JsonProperty("usageMetadata")
    private UsageMetadata usageMetadata;

    @JsonProperty("modelVersion")
    private String modelVersion;


    /**
     * Representa um candidato de resposta gerado pelo modelo.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Candidate {
        @JsonProperty("content")
        private Content content;

        @JsonProperty("finishReason")
        private String finishReason;

        @JsonProperty("avgLogprobs")
        private double avgLogprobs;
        // Campos como 'index' e 'safetyRatings' foram removidos pois não estão no JSON fornecido.
    }

    /**
     * Contém o conteúdo da resposta, dividido em partes e com um 'role'.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        @JsonProperty("parts")
        private List<Part> parts;

        @JsonProperty("role")
        private String role;
    }

    /**
     * Uma parte individual da resposta, geralmente contendo o texto.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Part {
        @JsonProperty("text")
        private String text;
    }

    /**
     * Metadados sobre o uso de tokens na requisição.
     * Esta classe foi adicionada para mapear o objeto 'usageMetadata' do JSON.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UsageMetadata {
        @JsonProperty("promptTokenCount")
        private int promptTokenCount;

        @JsonProperty("candidatesTokenCount")
        private int candidatesTokenCount;

        @JsonProperty("totalTokenCount")
        private int totalTokenCount;

        @JsonProperty("promptTokensDetails")
        private List<TokenDetails> promptTokensDetails;

        @JsonProperty("candidatesTokensDetails")
        private List<TokenDetails> candidatesTokensDetails;
    }

    /**
     * Detalhes sobre os tokens, como modalidade e contagem.
     * Esta classe foi adicionada para mapear os objetos dentro dos arrays '...TokensDetails'.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TokenDetails {
        @JsonProperty("modality")
        private String modality;

        @JsonProperty("tokenCount")
        private int tokenCount;
    }

    // As classes 'PromptFeedback' e 'SafetyRatings' foram removidas pois não correspondem ao JSON fornecido.
}
