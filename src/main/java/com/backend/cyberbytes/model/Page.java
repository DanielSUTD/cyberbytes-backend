package com.backend.cyberbytes.model;

import com.backend.cyberbytes.dto.PageRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Table(name = "page")
@Entity(name = "Page")

@EqualsAndHashCode(of = "id")
@Component
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "slug", nullable = false, unique = true)
    private String slug;
    @Column(name = "title", nullable = false, unique = true)
    private String title;
    @Column(name = "subtitle")
    private String subtitle;
    @Column(name = "introduction", nullable = false, columnDefinition = "TEXT")
    private String introduction;
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    @Column(name = "conclusion", columnDefinition = "TEXT")
    private String conclusion;
    @Column(name = "topic")
    private String topic;

    public Page(PageRequestDto dto){
        this.slug = dto.slug();
        this.title=dto.title();
        this.subtitle=dto.subtitle();
        this.introduction=dto.introduction();
        this.content=dto.content();
        this.conclusion=dto.conclusion();
        this.topic = dto.topic();
    }
}
