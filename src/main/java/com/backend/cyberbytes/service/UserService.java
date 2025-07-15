package com.backend.cyberbytes.service;

import com.backend.cyberbytes.dto.ForgotPasswordDto;
import com.backend.cyberbytes.dto.UserRequestDto;
import com.backend.cyberbytes.dto.UserResponseDto;
import com.backend.cyberbytes.exceptions.ResourceNotFoundException;
import com.backend.cyberbytes.model.User;
import com.backend.cyberbytes.model.UserRole;
import com.backend.cyberbytes.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    //Dependencias
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityCodeService securityCodeService;

    @Autowired
    private EmailService emailService;

    /*
     * Salva um novo usuário no banco de dados
     *
     * */
    public void registerUser(UserRequestDto data, int attempt){
        if (userRepository.findByEmail(data.email()) != null)
            throw new DataIntegrityViolationException("Este email de usuário já existe");

        if (!securityCodeService.verifyCode(data.email(), attempt)) {
            throw new IllegalArgumentException("Código expirado ou tentativa inválida");
        }

        User user = new User();
        user.setName(data.name());
        user.setEmail(data.email());
        user.setRole(UserRole.USER);
        user.setPassword(new BCryptPasswordEncoder().encode(data.password()));

        userRepository.save(user);
    }

    /*
   Retorna todos os usuários
   */
    public List<UserResponseDto> findAllUsuarios(){
        return userRepository.findAll().stream()
                .map(UserResponseDto::new)
                .toList();
    }

    /*
    Retorna um usuário específico pelo id dele
    * */
    public Optional<User> findUserById(String id){
        return userRepository.findById(id);
    }

    /*
    Retorna um usuário específico pelo Email dele
    * */
    public Optional<User> findUserByEmail(String email){
        return userRepository.findOptionalByEmail(email);
    }


    /*
    Atualizar o usuário
    * */
    @Transactional
    public User updateUser(String id, UserRequestDto newUser) {
        User userExists = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Optional<User> userEmail = userRepository.findOptionalByEmail(newUser.email());
        if (userEmail.isPresent() && !userEmail.get().getId().equals(userExists.getId())) {
            throw new DataIntegrityViolationException("Já existe um usuário com este email");
        }

        userExists.setName(newUser.name());
        userExists.setEmail(newUser.email());

        if (newUser.password() != null && !newUser.password().isBlank()) {
            String encryptedPassword = new BCryptPasswordEncoder().encode(newUser.password());
            userExists.setPassword(encryptedPassword);
        }

        return userRepository.save(userExists);
    }


    public void changePassword(ForgotPasswordDto forgotPasswordDto, int attempt){
        User user = userRepository.findOptionalByEmail(forgotPasswordDto.email())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (!securityCodeService.verifyCode(forgotPasswordDto.email(), attempt)) {
            throw new IllegalArgumentException("Código expirado ou tentativa inválida");
        }

        if (forgotPasswordDto.password() != null && !forgotPasswordDto.password().isBlank()) {
            String encryptedPassword = new BCryptPasswordEncoder().encode(forgotPasswordDto.password());
            user.setPassword(encryptedPassword);
            userRepository.save(user);
        }
    }

    /*
     * Deleta um usuário específico
     * */
    @Transactional
    public User deleteById(String id){

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        userRepository.deleteById(id);
        return user;
    }

    /*
     * Deleta todos os usuários no banco de dados
     * */
    @Transactional
    public void deleteAll(){
        userRepository.deleteAll();
    }
}
