package com.prototype.Ikmbd328.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Имя пустое!")
    @Pattern(regexp = "^[A-Za-zА-Яа-я\\s]+$", message = "Имя может содержать только буквы и пробелы!")
    private String title;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)//ВК для бд
    @NotNull(message = "Тренер пуст")//пустота
    private Trainer trainer;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)//ВК для бд
    @NotNull(message = "Программа пуста")//пустота
    private Program program;


    public Client() {} //конструкторы пустой для JPA

    public Client(String title, Trainer trainer, Program program) {//конструктор со всеми параметрами
        this.title = title;
        this.trainer = trainer;
        this.program = program;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", trainer=" + trainer.getName() +
                ", program=" + program.getName() +
                '}';
    }
}