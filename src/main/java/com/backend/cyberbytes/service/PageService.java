package com.backend.cyberbytes.service;

import com.backend.cyberbytes.dto.PageRequestDto;
import com.backend.cyberbytes.dto.PageResponseDto;
import com.backend.cyberbytes.exceptions.ResourceNotFoundException;
import com.backend.cyberbytes.model.Page;
import com.backend.cyberbytes.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PageService {

    @Autowired
    private PageRepository repository;

    /*
     * Retorna todas as páginas
     * */
    public List<PageResponseDto> getAllPaginas(){
        return repository.findAll()
                .stream()
                .map(PageResponseDto::new)
                .toList();
    }

    /*
     * Retorna o slug da página
     * */
    public PageResponseDto getPageBySlug(String slug) {
        if (slug == null || slug.isBlank()) {
            throw new IllegalArgumentException("Slug não pode ser nulo ou vazio.");
        }

        Page page = repository.findBySlug(slug);

        if (page == null) {
            throw new ResourceNotFoundException("Página não encontrada para o slug: " + slug);
        }

        return new PageResponseDto(page);
    }

    /*
     * Retorna páginas quando o usuário pesquisa
     * */
    public List<PageResponseDto> findByTitleContainingIgnoreCase(String query) {
        if (query == null || query.isBlank()) {
            return Collections.emptyList();
        }

        List<Page> pages = repository.findByTitleContainingIgnoreCase(query);

        List<PageResponseDto> dtos = pages.stream()
                .map(PageResponseDto::new)
                .collect(Collectors.toList());

        return dtos;
    }

    /*
     * Retorna páginas pelo tópico delas
     * */
    public List<PageResponseDto> getRelatedPages(String topic) {
        List<Page> relatedPages = repository.findByTopic(topic);

        //Aleatoriedade nos resultados
        Collections.shuffle(relatedPages);

        // Retorna até 10 páginas
        return relatedPages.stream()
                .limit(10)
                .map(PageResponseDto::new)
                .collect(Collectors.toList());
    }

    /*
     * Salva uma página
     * */
    public Page savePagina(PageRequestDto dto) throws Exception {
        if (repository.findBySlug(dto.slug()) != null) {
            throw new Exception("Esse link já existe!");
        }

        Page page = new Page(dto);
        return repository.save(page);
    }

    /*
     * Atualiza uma página
     * */
    public void updatePagina(PageRequestDto dto) {
        Page page = repository.findBySlug(dto.slug());
        if (page == null) {
            throw new ResourceNotFoundException("Página não encontrada para o slug: " + dto.slug());
        }

        page.setTitle(dto.title());
        page.setSubtitle(dto.subtitle());
        page.setIntroduction(dto.introduction());
        page.setContent(dto.content());
        page.setConclusion(dto.conclusion());
        page.setTopic(dto.topic());

        repository.save(page);
    }

    /*
     * Deleta uma página
     * */
    public void deletePageBySlug(String slug) {
        Page page = repository.findBySlug(slug);
        if (page == null) {
            throw new ResourceNotFoundException("Página não encontrada para o slug: " + slug);
        }

        repository.delete(page);
    }
}
