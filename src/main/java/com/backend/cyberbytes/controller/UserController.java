package com.backend.cyberbytes.controller;


import com.backend.cyberbytes.dto.EmailRequestDto;
import com.backend.cyberbytes.dto.ForgotPasswordDto;
import com.backend.cyberbytes.dto.UserRequestDto;
import com.backend.cyberbytes.dto.UserResponseDto;
import com.backend.cyberbytes.exceptions.ResourceNotFoundException;
import com.backend.cyberbytes.model.User;
import com.backend.cyberbytes.service.SecurityCodeService;
import com.backend.cyberbytes.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityCodeService securityCodeService;

    /*
     * Retorna todos os usuários
     * */
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsuarios() {
        List<UserResponseDto> users = userService.findAllUsuarios();
        return ResponseEntity.ok(users);
    }

    /*
     * Retorna um usuário específico pelo ID
     * */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
        Optional<User> optionalUser = userService.findUserById(id);

        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(new UserResponseDto(optionalUser.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
    }

    /*
     * Retorna um usuário específico pelo EMAIL
     * */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable("email") String email) {
        Optional<User> optionalUser = userService.findUserByEmail(email);

        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(new UserResponseDto(optionalUser.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
    }

    /*
     * Gerar código para criar conta
     * */
    @PostMapping("/code")
    public ResponseEntity<String> genSecurityCode(@RequestBody EmailRequestDto emailRequestDto){
        String email = emailRequestDto.email();
        //System.out.println(email);
        try {
            securityCodeService.saveCode(email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("Ouve um erro ao gerar um código");
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(202)).body("Um email contendo o código de verificação foi enviado");
    }

    /*
     * Gerar código para alterar senha
     * */
    @PostMapping("/forgot-password")
    public ResponseEntity<String> genRecoveryCode(@RequestBody EmailRequestDto emailRequestDto){
        String email = emailRequestDto.email();
        System.out.println(email);
        try {
            securityCodeService.sendRecoveryCode(email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("Ouve um erro ao gerar um código");
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(202)).body("Um email contendo o código de verificação foi enviado");
    }

    /*
     * Redefinir senha do usuário
     * */
    @PostMapping("/recover-account")
    public ResponseEntity<String> resetPassword(
            @RequestBody @Valid ForgotPasswordDto forgotPasswordDto,
            @RequestParam("attempt") int attempt) {
        try {
            userService.changePassword(forgotPasswordDto, attempt);
            return ResponseEntity.ok("Senha modificada com sucesso!");
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    /*
     * Atualizar dados do usuário
     * */
    @PutMapping("{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") String id, @RequestBody UserRequestDto data) {
        try {
            User updatedUser = userService.updateUser(id, data);
            return ResponseEntity.ok(new UserResponseDto(updatedUser));
        } catch (DataIntegrityViolationException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
     * Deletar todos os usuários do banco de dados
     * */
    @DeleteMapping
    public ResponseEntity<String> deleteAllUsuarios() {
        userService.deleteAll();
        return ResponseEntity.ok("Usuários deletados com sucesso");
    }

    /*
     * Deleta um usuário específico
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuarioById(@PathVariable("id") String id) {
        try {
            User deletedUser = userService.deleteById(id);
            return ResponseEntity.ok(new UserResponseDto(deletedUser));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
