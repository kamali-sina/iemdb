package com.example.iemdb;

import exception.CommandException;
import main.Actor;
import manager.ActorManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import output.Output;

import java.util.Collection;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/actors")
public class ActorController {

    @RequestMapping("/")
    public Output getActors() throws CommandException {
        return new Output(HttpStatus.OK.value(), ActorManager.actors.values());
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