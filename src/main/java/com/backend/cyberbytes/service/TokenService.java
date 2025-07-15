package com.backend.cyberbytes.service;


import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import com.backend.cyberbytes.model.User;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    //Pega o secret no application propeties
    @Value("${api.security.token.secret}")
    private String secret;

    /*
     * Gera o token
     * */
    public String generateToken(User user){
        try{
            //cria com a chave secreta
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())//Define o email do usuário
                    .withExpiresAt(genExpirationDate())//Define a data de expiração
                    .sign(algorithm);//Assina com a secret
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error ao gerar token", exception);
        }
    }

    /*
     * Valida se o token segue as nossas regras
     * */
    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            return "";
        }
    }

    /*
     * Função para criar a validade do token
     * */
    private Instant genExpirationDate(){
        //Duração de duas horas
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
