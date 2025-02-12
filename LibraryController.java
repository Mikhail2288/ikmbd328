package com.prototype.Ikmbd328.controller;

import com.prototype.Ikmbd328.model.Trainer;
import com.prototype.Ikmbd328.model.Client;
import com.prototype.Ikmbd328.model.Program;
import com.prototype.Ikmbd328.repository.TrainerRepository;
import com.prototype.Ikmbd328.repository.ClientRepository;
import com.prototype.Ikmbd328.repository.ProgramRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;//контейнер, который хранит данные, предназначенные для отображения в представлении.
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller //обработка НТТР запросов и возарат HTML
@RequestMapping("/") //базовый путь для всех методов
public class LibraryController {
//внедрение репозиториев для взаимодействия с бд
    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private ClientRepository clientRepository;

    // Главная страница
    @GetMapping
    public String home() {
        return "index";
    }

    // тренеры
    @GetMapping("/trainers") //Просмотр списка тренеров
    public String listTrainers(Model model) {
        model.addAttribute("trainers", trainerRepository.findAll());//Передает список тренеров из бд в модель trainers
        return "trainers/list";
    }

    @GetMapping("/trainers/new") //Отображение формы для добавления тренера
    public String showTrainerForm(Model model) {
        model.addAttribute("trainer", new Trainer());//Создает новый объект Trainer и передает его в модель
        return "trainers/form";
    }

    @PostMapping("/trainers") //сохранение тренера
    public String saveTrainer(@Valid @ModelAttribute Trainer trainer, BindingResult result) {//Принимает данные из формы (@ModelAttribute Trainer trainer) и проверяет их на валидность (@Valid).
        if (result.hasErrors()) {//Если есть ошибки валидации в result
            return "trainers/form";
        }
        //Обновление существующего тренера
        if (trainer.getId() != null) {
            Trainer existingTrainer = trainerRepository.findById(trainer.getId()) //поиск тренера по ID
                    .orElseThrow(() -> new IllegalArgumentException("Неверный идентификатор тренера: " + trainer.getId())); //тренер не найден = ошибка
            existingTrainer.setName(trainer.getName()); //новое значение для поля name
            trainerRepository.save(existingTrainer); //сохранение обновленного тренера в бд
        } else { // Создание нового тренера
            trainerRepository.save(trainer);
        }
        return "redirect:/trainers";// Перенаправляем на страницу списка тренеров
    }

    @GetMapping("/trainers/edit/{id}") //Редактирование тренера
    public String editTrainer(@PathVariable Long id, Model model) {
        model.addAttribute("trainer", trainerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный идентификатор тренера")));
        return "trainers/form";
    }

    @GetMapping("/trainers/delete/{id}") //Удаление тренера
    public String deleteTrainer(@PathVariable Long id) {
        trainerRepository.deleteById(id);
        return "redirect:/trainers";
    }

    // программы
    @GetMapping("/programs")//Просмотр списка программ
    public String listPrograms(Model model) {
        model.addAttribute("programs", programRepository.findAll());
        return "programs/list";
    }

    @GetMapping("/programs/new") //Отображение формы для добавления программы
    public String showProgramForm(Model model) {
        model.addAttribute("program", new Program());
        return "programs/form";
    }

    @PostMapping("/programs")//сохранение программы
    public String saveProgram(@Valid @ModelAttribute Program program, BindingResult result) {
        if (result.hasErrors()) {
            return "programs/form";
        }
        if (program.getId() != null) {
            Program existingProgram = programRepository.findById(program.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Неверный идентификатор программы: " + program.getId()));
            existingProgram.setName(program.getName());
            programRepository.save(existingProgram);
        } else {
            programRepository.save(program);
        }

        return "redirect:/programs";
    }

    @GetMapping("/programs/edit/{id}")//Редактирование программы
    public String editProgram(@PathVariable Long id, Model model) {
        model.addAttribute("program", programRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный идентификатор программы")));
        return "programs/form";
    }


    @GetMapping("/programs/delete/{id}")//Удаление программы
    public String deleteProgram(@PathVariable Long id) {
        programRepository.deleteById(id);
        return "redirect:/programs";
    }

    // Клиенты
    @GetMapping("/clients") //Просмотр списка клиентов
    public String listClients(Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        return "clients/list";
    }

    @GetMapping("/clients/new") //Отображение формы для добавления клиента
    public String showClientForm(Model model) {
        model.addAttribute("client", new Client());
        model.addAttribute("trainers", trainerRepository.findAll());
        model.addAttribute("programs", programRepository.findAll());
        return "clients/form";
    }

    @PostMapping("/clients") //Сохранение клиента
    public String saveClient(@Valid @ModelAttribute Client client, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("trainers", trainerRepository.findAll()); //добавляем в объект model список всех доступных тренеров
            model.addAttribute("programs", programRepository.findAll()); //добавляем в объект model список всех доступных программ
            return "clients/form";
        }
       //Обновляем связанные сущности
        Trainer trainer = trainerRepository.findById(client.getTrainer().getId()).orElseThrow(() -> new IllegalArgumentException("Неверный идентификатор тренера"));
        Program program = programRepository.findById(client.getProgram().getId()).orElseThrow(() -> new IllegalArgumentException("Неверный идентификатор программы"));

        client.setTrainer(trainer); //связь между клиентом и полученным объектом trainer
        client.setProgram(program); //связь между клиентом и полученным объектом program

        clientRepository.save(client); //сохранение в бд
        return "redirect:/clients"; //перенаправление на URL /clients
    }

    @GetMapping("/clients/edit/{id}") //Редактирование клиента
    public String editClient(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Неверный идентификатор клиента")));
        model.addAttribute("trainers", trainerRepository.findAll());
        model.addAttribute("programs", programRepository.findAll());
        return "clients/form";
    }

    @GetMapping("/clients/delete/{id}") //Удаление клиента
    public String deleteClient(@PathVariable Long id) {
        clientRepository.deleteById(id);
        return "redirect:/clients";
    }
}