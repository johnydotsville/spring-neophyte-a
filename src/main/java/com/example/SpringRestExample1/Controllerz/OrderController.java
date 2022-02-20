package com.example.SpringRestExample1.Controllerz;

import java.util.List;
import java.util.stream.Collectors;

import com.example.SpringRestExample1.AppCore.Order;
import com.example.SpringRestExample1.AppCore.OrderRepository;
import com.example.SpringRestExample1.AppCore.Status;
import com.example.SpringRestExample1.CustomExceptions.OrderNotFoundException;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class OrderController {
    private final OrderRepository repo;
    private final OrderModelAssembler assembler;

    public OrderController(OrderRepository repo, OrderModelAssembler assembler) {
        this.repo = repo;
        this.assembler = assembler;
    }

    @GetMapping("/orders")
    CollectionModel<EntityModel<Order>> allOrders() {
        List<EntityModel<Order>> orders = repo.findAll().stream()
            // Почему тут :: а не . что это значит?
            .map(assembler::toModel)
            .collect(Collectors.toList());
        // А почему тут например не возвращается ResponseEntity как в методе добавления,
        // а просто данные?
        return CollectionModel.of(
            orders,
            linkTo(methodOn(OrderController.class).allOrders()).withSelfRel());
    }

    @GetMapping("/orders/{id}")
    EntityModel<Order> findOrder(@PathVariable Long id) {
        Order order = repo.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));

        return assembler.toModel(order);
    }

    @PostMapping("/orders")
    ResponseEntity<EntityModel<Order>> newOrder(@RequestBody Order order) {
        order.setStatus(Status.IN_PROGRESS);
        Order newOrder = repo.save(order);

        return ResponseEntity
            .created(linkTo(methodOn(OrderController.class).findOrder(order.getId())).toUri())
            .body(assembler.toModel(newOrder));
    }

    @DeleteMapping("/orders/{id}/cancel")
    ResponseEntity<?> cancel(@PathVariable Long id) {
        Order order = repo.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.CANCELLED);
            return ResponseEntity.ok(assembler.toModel(repo.save(order)));
        }

        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
            .body(Problem.create()
                .withTitle("Method not allowed")
                .withDetail("You can't cancel as order that is in the " + order.getStatus() + " status")
            );
    }

    @PutMapping("/orders/{id}/complete")
    ResponseEntity<?> complete(@PathVariable Long id) {
        Order order = repo.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.COMPLETED);
            return ResponseEntity.ok(assembler.toModel(repo.save(order)));
        }

        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
            .body(Problem.create()
                .withTitle("Method not allowed")
                .withDetail("You can't complete as order that is in the " + order.getStatus() + " status")
            );
    }
}
