package com.example.iemdb;

import exception.CommandException;
import main.Actor;
import manager.ActorManager;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/actors")
public class ActorController {

    @RequestMapping("/")
    public Collection<Actor> getActors() {
        return ActorManager.actors.values();
    }

    @GetMapping("/{id}")
    public Actor getActor(@PathVariable Integer id) throws CommandException {
        return ActorManager.getActor(id);
    }
}