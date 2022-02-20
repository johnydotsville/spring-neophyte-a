package com.example.SpringRestExample1.Controllerz;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.example.SpringRestExample1.AppCore.Status;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.SpringRestExample1.AppCore.Order;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {
    @Override
    public EntityModel<Order> toModel(Order order) {
        EntityModel<Order> model = EntityModel.of(
            order,
            // Не понимаю, почему withSelfRel. Возможно потому что не знаю что именно оно делает
            linkTo(methodOn(OrderController.class).findOrder(order.getId())).withSelfRel(),
            linkTo(methodOn(OrderController.class).allOrders()).withRel("orders")
        );

        // От оно чё, Михалыч! Соль REST в том, чтобы не просто данные вернуть, а еще и команды,
        // которые можно выполнить с этой сущностью. На этом примере стало понятнее
        if (order.getStatus() == Status.IN_PROGRESS) {
            model.add(linkTo(methodOn(OrderController.class).cancel(order.getId())).withRel("cancel"));
            model.add(linkTo(methodOn(OrderController.class).complete(order.getId())).withRel("complete"));
        }

        return model;
    }
}
