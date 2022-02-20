package com.example.SpringRestExample1.Controllerz;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.example.SpringRestExample1.AppCore.Employee;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {
    @Override
    public EntityModel<Employee> toModel(Employee emp) {
        return EntityModel.of(
            emp,
            linkTo(methodOn(EmployeeController.class).findEmployee(emp.getId())).withSelfRel(),
            linkTo(methodOn(EmployeeController.class).allEmployees()).withRel("employees")
        );
    }
}
