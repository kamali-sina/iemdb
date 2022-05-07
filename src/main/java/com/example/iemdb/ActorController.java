package com.example.iemdb;

import exception.CommandException;
import main.Actor;
import manager.ActorManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import output.Output;
import repository.ConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/actors")
public class ActorController {

    @GetMapping("/")
    public Output getActors() throws CommandException {
        return new Output(HttpStatus.OK.value(), ActorManager.getActors());
    }

    @GetMapping("/{id}")
    public Output getActor(@PathVariable Integer id) throws CommandException {
        try {
            Actor actor = ActorManager.getActor(id);
            return new Output(HttpStatus.OK.value(), actor);
        } catch (Exception e) {
            return new Output(HttpStatus.NOT_FOUND.value(), "actor does not exist");
        }
    }
}