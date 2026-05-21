package com.quadrafast.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "quadras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quadra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(nullable = false)
    private Boolean ativa;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    private void prePersist() {
        this.criadoEm = LocalDateTime.now();
        if (this.ativa == null) this.ativa = true;
    }
}
