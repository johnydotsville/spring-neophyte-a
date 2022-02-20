package com.example.SpringRestExample1.AppCore;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Employee {
    private @Id @GeneratedValue Long id;
    // Для следования принципу REST о поддержании старых клиентов мы сохраняем поддержку
    // старого поля name через геттеры и сеттеры, хотя фактически внутри уже оперируем
    // двумя новыми полями
    //private String name;
    private String firstName;
    private String lastName;
    private String role;

    Employee() { }

//    public Employee(String name, String role) {
    public Employee(String firstName, String lastName, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    public void setName(String name) {
        String[] parts = name.split(" ");
        this.firstName = parts[0];
        this.lastName = parts[1];
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Employee))
            return false;
        Employee emp = (Employee) obj;

        return Objects.equals(this.id, emp.id)
                && Objects.equals(this.firstName, emp.firstName)
                && Objects.equals(this.lastName, emp.lastName)
                && Objects.equals(this.role, emp.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.firstName, this.lastName, this.role);
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + this.id + ", " +
                "firstName='" + this.firstName + "', " +
                "lastName='" + this.lastName + "', " +
                "role='" + this.role + "'";
    }
}
