package film.api.service.impl;

import film.api.DTO.response.ActorDTO;
import film.api.exception.NotFoundException;
import film.api.models.Actor;
import film.api.repository.ActorRepository;
import film.api.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActorServiceImpl implements ActorService {
    @Autowired
    private ActorRepository actorRepository;
    @Override
    public List<Actor> getList(){

        return actorRepository.findAll();
    }

    @Override
    public Actor addActor(Actor actor) {
        return actorRepository.save(actor);
    }

    @Override
    public Optional<Actor> findById(Long id) {
        return actorRepository.findById(id);
    }


    @Override
    public Actor updateActor(Long id, ActorDTO actorPatchDTO) {

        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Actor not found"));
        if(actorPatchDTO.getSex() != null) {
            actor.setSex(actorPatchDTO.getSex());
        }
        if(actorPatchDTO.getNativeLand() != null) {
            actor.setNativeLand(actorPatchDTO.getNativeLand());
        }
        if(actorPatchDTO.getAge() != null) {
            actor.setAge(actorPatchDTO.getAge());
        }
        if(actorPatchDTO.getActorName() != null) {
            actor.setActorName(actorPatchDTO.getActorName());
        }

        return actorRepository.save(actor);
    }


    @Override
    public void deleteById(Long id) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Actor not found"));
        actorRepository.deleteById(id);
    }

    @Override
    public List<Actor> searchActors(String actorName) {
        return actorRepository.searchActors(actorName);
    }

    @Override
    public List<Actor> findActorByChapterId(Long chapterId){
        return actorRepository.findActorByChapterId(chapterId);
    }
}
