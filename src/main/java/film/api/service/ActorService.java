package film.api.service;

import film.api.DTO.response.ActorDTO;
import film.api.models.Actor;

import java.util.List;
import java.util.Optional;

public interface ActorService {
    List<Actor> getList();

    Actor addActor(Actor actor);

    Optional<Actor> findById(Long id);

    Actor updateActor(Long id, ActorDTO actorPatchDTO);

    void deleteById(Long id);

    List<Actor> searchActors(String actorName);

    List<Actor> findActorByChapterId(Long chapterId);
}
