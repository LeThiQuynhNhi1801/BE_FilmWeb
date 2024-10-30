package film.api.controller;

import film.api.DTO.ActorDTO;
import film.api.exception.NotFoundException;
import film.api.models.Actor;
import film.api.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/ApiV1", produces = "application/json")
public class ActorController {

    @Autowired
    private ActorService actorService;
    @Secured({"ROLE_ADMIN","ROLE_USER"})

    @GetMapping("/AllActor")
    public ResponseEntity<?> getAllActors() {


        return new ResponseEntity<>(actorService.getList(), HttpStatus.OK);
    }
    @Secured({"ROLE_ADMIN"})
    @PostMapping("/AllActor")
    public ResponseEntity<?> addActor(@RequestBody Actor actor) {


        return new ResponseEntity<>(actorService.addActor(actor), HttpStatus.CREATED);
    }
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/ActorByID/{id}")
    public ResponseEntity<?> getActorById(@PathVariable Long id) {
        Optional<Actor> actor = actorService.findById(id);

        if (!actor.isPresent()) {
            throw new NotFoundException("Không Có Actor");
        }
        return new ResponseEntity<>(new ActorDTO(actor.get()), HttpStatus.OK);
    }
    @Secured({"ROLE_ADMIN"})
    @PatchMapping("/ActorByID/{ActorByID}")
    public ResponseEntity<Object> updateActor(@PathVariable("ActorByID") Long id, @RequestBody ActorDTO actorPost) {

        return new ResponseEntity<>(actorService.updateActor(id,actorPost), HttpStatus.OK);
    }
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/ActorByID/{ActorByID}")
    public ResponseEntity<?> deleteActor(@PathVariable Long ActorByID) {
        actorService.deleteById(ActorByID);
        return new ResponseEntity<>("Xóa thành công", HttpStatus.NO_CONTENT);
    }
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/ActorByName/{ActorName}")
    public ResponseEntity<List<Actor>> searchActors(@PathVariable("ActorName") String actorName) {
        List<Actor> actors = actorService.searchActors(actorName);
        return new ResponseEntity<>(actors, HttpStatus.OK);
    }

}

