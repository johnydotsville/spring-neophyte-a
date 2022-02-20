package com.example.SpringRestExample1.AppCore;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CUSTOMER_ORDER")
public class Order {
    private @Id @GeneratedValue Long id;
    private String description;
    private Status status;

    Order() { }

    public Order(String description, Status status) {
        this.description = description;
        this.status = status;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Order))
            return false;
        Order order = (Order) obj;
//        return this.id == order.id
//            && this.status == order.status;
        // Почему id тут сравнивается через Object.equals, а status через == ?
        // И зачем вообще пользоваться Objects.equals? Вообще, и для сравнения
        // примитивов в частности
        return Objects.equals(this.id, order.id)
            && Objects.equals(this.description, order.description)
            && this.status == order.status;
    }

    @Override
    public int hashCode() {
        // А ведь хэш это массив байтов вроде, почему тогда int?
        return Objects.hash(this.id, this.description, this.status);
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + this.id
            + ", description='" + this.description
            + "', status=" + this.status + "}";
    }
}
