package com.example.SpringRestExample1.Controllerz;

import java.util.List;
import java.util.stream.Collectors;

import com.example.SpringRestExample1.AppCore.Employee;
import com.example.SpringRestExample1.AppCore.EmployeeRepository;
import com.example.SpringRestExample1.CustomExceptions.EmployeeNotFoundException;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {
    private final EmployeeRepository repo;
    private final EmployeeModelAssembler assembler;

    EmployeeController(EmployeeRepository repo, EmployeeModelAssembler assembler) {
        this.repo = repo;
        this.assembler = assembler;
    }

    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> allEmployees() {
        List<EntityModel<Employee>> emps = repo
            .findAll()
            .stream()
//            .map(e -> EntityModel.of(
//                e,
//                linkTo(methodOn(EmployeeController.class).findEmployee(e.getId())).withSelfRel(),
//                linkTo(methodOn(EmployeeController.class).allEmployees()).withSelfRel())
//                // В туторе было как в строчке ниже, но результаты не совпадали. Мб ошибка в туторе
//                //linkTo(methodOn(EmployeeController.class).allEmployees()).withRel("employees"))
//            )
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(emps, linkTo(methodOn(EmployeeController.class).allEmployees()).withSelfRel());
    }

    @PostMapping("/employees")
    //Employee newEmployee(@RequestBody Employee employee) {
    ResponseEntity<?> newEmployee(@RequestBody Employee employee) {
        EntityModel<Employee> entityModel = assembler.toModel(repo.save(employee));

        //return repo.save(employee);
        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    @GetMapping("/employees/{id}")
    EntityModel<Employee> findEmployee(@PathVariable Long id) {
        Employee employee = repo.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return assembler.toModel((employee));
//        return EntityModel.of(employee,
//                linkTo(methodOn(EmployeeController.class).findEmployee(id)).withSelfRel(),
//                linkTo(methodOn(EmployeeController.class).allEmployees()).withRel("employees"));
    }

    @PutMapping("/employees/{id}")
//    Employee updateEmployee(@RequestBody Employee employee, @PathVariable Long id) {
    ResponseEntity<?> updateEmployee(@RequestBody Employee employee, @PathVariable Long id) {
//        return repo.findById(id)
//                .map(e -> {
//                    e.setName(employee.getName());
//                    e.setRole(employee.getRole());
//                    return repo.save(e);
//                })
//                .orElseGet(() -> {
//                    employee.setId(id);
//                    return repo.save(employee);
//                });
        Employee updEmp = repo.findById(id)
            .map(emp -> {
                emp.setName(employee.getName());
                emp.setRole(employee.getRole());
                return repo.save(emp);
            })
            .orElseGet(() -> {
                employee.setId(id);
                return repo.save(employee);
            });

        EntityModel<Employee> entityModel = assembler.toModel(updEmp);

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    @DeleteMapping("/employees/{id}")
//    void deleteEmployee(@PathVariable Long id) {
    ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        repo.deleteById((id));

        return ResponseEntity.noContent().build();
    }
}
