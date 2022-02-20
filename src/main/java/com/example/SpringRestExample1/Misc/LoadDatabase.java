package com.example.SpringRestExample1.Misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.SpringRestExample1.AppCore.*;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository empRepo, OrderRepository orderRepo) {
        // Это тут DI через метод типа?
        return args -> {
//            log.info("Preloading " + repo.save(new Employee("Владимир", "Епифанцев", "братишка")));
//            log.info("Preloading" + repo.save(new Employee("Сергей", "Пахомов", "поехавший")));
            Employee emp1 = empRepo.save(new Employee("Владимир", "Епифанцев", "братишка"));
            Employee emp2 = empRepo.save(new Employee("Сергей", "Пахомов", "поехавший"));

            empRepo.findAll().forEach(emp -> log.info("Preloaded employee: " + emp));

            orderRepo.save(new Order("MacBook Pro", Status.COMPLETED));
            orderRepo.save(new Order("iPhone", Status.IN_PROGRESS));

            orderRepo.findAll().forEach(order -> log.info("Preloaded order: " + order));
        };
    }
}
