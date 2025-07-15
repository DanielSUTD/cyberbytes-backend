package com.backend.cyberbytes.controller;

import com.backend.cyberbytes.dto.PageResponseDto;
import com.backend.cyberbytes.service.IaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/ia")
public class IaController {

    @Autowired
    private IaService service;

    @PostMapping("/chat")
    public ResponseEntity<String> send_prompt(@RequestBody String prompt) throws URISyntaxException, IOException, InterruptedException {
        try{
            String response = service.sendRequest(prompt);
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(response);
        } catch (Exception e) {
            System.out.println("Erro ao tentar fazer requisição. Exception: "+ e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/page")
    public ResponseEntity<PageResponseDto> create_page(@RequestBody String prompt) throws  URISyntaxException, IOException, InterruptedException{
        try{
            PageResponseDto response = service.createContent(prompt);
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(response);
        } catch (Exception e) {
            System.out.println("Erro ao tentar fazer requisição. Exception: "+ e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
