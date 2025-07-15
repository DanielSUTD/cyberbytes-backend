package com.backend.cyberbytes.dto;

import com.backend.cyberbytes.model.Page;

public record PageResponseDto(String uuid, String slug, String title, String subtitle, String introduction, String content, String conclusion, String topic){
    public PageResponseDto(Page page){
        this(page.getId(), page.getSlug(), page.getTitle(), page.getSubtitle(), page.getIntroduction(), page.getContent(), page.getConclusion(), page.getTopic());
    }
}
