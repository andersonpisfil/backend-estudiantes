package com.backend.estudiantes.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "instructores")
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne //De uno a uno
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(nullable = false, length = 10, unique = true)
    private String especialidad;

    @Column(nullable = false)
    private LocalDate fechaContratacion;


}
