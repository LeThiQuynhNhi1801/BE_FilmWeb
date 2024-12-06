package film.api.controller;

import film.api.DTO.response.ActorDTO;
import film.api.exception.NotFoundException;
import film.api.models.Actor;
import film.api.service.ActorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/ApiV1", produces = "application/json")
@Slf4j
@CrossOrigin("*")
public class ActorController {

    @Autowired
    private ActorService actorService;

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/AllActor")
    public ResponseEntity<?> getAllActors() {
        List<Actor> actors = actorService.getList();
        return new ResponseEntity<>(actors, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/AllActor")
    public ResponseEntity<?> addActor(@RequestBody Actor actor) {
        Actor createdActor = actorService.addActor(actor);
        return new ResponseEntity<>(createdActor, HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/ActorByID/{id}")
    public ResponseEntity<?> getActorById(@PathVariable("id") Long id) {
        Actor actor = actorService.findById(id)
                .orElseThrow(() -> new NotFoundException("Actor not found"));
        return new ResponseEntity<>(new ActorDTO(actor), HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @PatchMapping("/ActorByID/{id}")
    public ResponseEntity<?> updateActor(@PathVariable("id") Long id, @RequestBody ActorDTO actorPost) {
        Actor updatedActor = actorService.updateActor(id, actorPost);
        return new ResponseEntity<>(updatedActor, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/ActorByID/{id}")
    public ResponseEntity<?> deleteActor(@PathVariable("id") Long ActorByID) {
        actorService.deleteById(ActorByID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/ActorByName/{ActorName}")
    public ResponseEntity<List<Actor>> searchActors(@PathVariable("ActorName") String actorName) {
        List<Actor> actors = actorService.searchActors(actorName);
        return new ResponseEntity<>(actors, HttpStatus.OK);
    }
}
