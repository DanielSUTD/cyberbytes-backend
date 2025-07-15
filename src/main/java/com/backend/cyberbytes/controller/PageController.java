package com.backend.cyberbytes.controller;

import com.backend.cyberbytes.dto.PageRequestDto;
import com.backend.cyberbytes.dto.PageResponseDto;
import com.backend.cyberbytes.model.Page;
import com.backend.cyberbytes.exceptions.ResourceNotFoundException;
import com.backend.cyberbytes.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("page")
public class PageController {

    @Autowired
    private PageService pageService;

    /*
     * Endpoint: Retorna todas as páginas
     * */
    @GetMapping
    public ResponseEntity<List<PageResponseDto>> getAllPaginas() {
        List<PageResponseDto> pages = pageService.getAllPaginas();

        if (pages == null || pages.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }

        return ResponseEntity.ok(pages); // 200
    }

    /*
     * Endpoint: Retorna uma página pelo slug
     * */
    @GetMapping("/slug")
    public ResponseEntity<PageResponseDto> getPaginaBySlug(@RequestParam("slug") String slug) {
        if (slug == null || slug.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        PageResponseDto dto = pageService.getPageBySlug(slug.trim());

        if (dto == null) {
            return ResponseEntity.notFound().build(); // 404
        }

        return ResponseEntity.ok(dto); // 200
    }


    /*
     * Endpoint: Retorna paginas pelo tópico
     * */
    @GetMapping("/related")
    public ResponseEntity<List<PageResponseDto>> getRelatedPages(@RequestParam String topic) {
        List<PageResponseDto> pages = pageService.getRelatedPages(topic);

        if (pages.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }

        return ResponseEntity.ok(pages); // 200
    }

    /*
     * Endpoint: Retorna paginas pelo mecanismo de pesquisa
     * */
    @GetMapping("/search")
    public ResponseEntity<List<PageResponseDto>> findByTitleContainingIgnoreCase(@RequestParam("query") String query) {
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        List<PageResponseDto> dtos = pageService.findByTitleContainingIgnoreCase(query);
        return ResponseEntity.ok(dtos); // 200 OK
    }

    /*
     * Endpoint: Salva uma página
     * */
    @PostMapping
    public ResponseEntity<?> savePagina(@RequestBody PageRequestDto dto) {
        try {
            Page page = pageService.savePagina(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(page); // 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // 409 Conflict
        }
    }

    /*
     * Endpoint: Atualiza uma página
     * */
    @PutMapping
    public ResponseEntity<?> updatePagina(@RequestBody PageRequestDto dto) {
        try {
            pageService.updatePagina(dto);
            return ResponseEntity.ok("Página atualizada com sucesso."); // 200 OK
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar página."); // 500
        }
    }

    /*
     * Endpoint: Deleta uma página
     * */
    @DeleteMapping
    public ResponseEntity<?> deletePagina(@RequestBody PageRequestDto dto) {
        try {
            pageService.deletePageBySlug(dto.slug());
            return ResponseEntity.ok("Página deletada com sucesso."); // 200 OK
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar página."); // 500
        }
    }
}
