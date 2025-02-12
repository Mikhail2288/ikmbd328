package com.prototype.Ikmbd328.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Entity //аннотация для сущности JPA для бд
public class Trainer {

    @Id //ПК
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) //защита на уровне бд
    @NotBlank(message = "Имя пустое!") //защита на уровне приложения
    @Pattern(regexp = "^[A-Za-zА-Яа-я\\s]+$", message = "Имя может содержать только буквы и пробелы!")//проверка валидации данных
    private String name;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true) //поле trainer в классе Client - ВК, каскадно всё, клиент не может быть без тренера
    private List<Client> clients = new ArrayList<>();

    public Trainer() {} //Конструктор для JPA

    public Trainer(String name) { //Конструкторы с name
        this.name = name;
    }

    //Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}