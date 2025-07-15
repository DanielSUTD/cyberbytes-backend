package com.backend.cyberbytes.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Table(name = "security_code")
@Entity(name = "SecurityCode")
@EqualsAndHashCode(of = "id")

@Component
public class SecurityCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private BigInteger id;
    @Column(name = "code", nullable = false)
    private int code;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    public SecurityCode(String email, int code, LocalDateTime now){
        this.email=email;
        this.code=code;
        this.createdAt=now;
    }
}
