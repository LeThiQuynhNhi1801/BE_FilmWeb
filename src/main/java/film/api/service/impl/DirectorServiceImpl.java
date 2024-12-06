package film.api.service.impl;

import film.api.DTO.request.DirectorDTO;
import film.api.models.Director;
import film.api.repository.DirectorRepository;
import film.api.service.DirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DirectorServiceImpl implements DirectorService {
    @Autowired
    private DirectorRepository directorRepository;
    @Override
    public List<Director> getList(){

        return directorRepository.findAll();
    }

    @Override
    public Director addDirector(Director director) {
        return directorRepository.save(director);
    }

    @Override
    public Optional<Director> findById(Long id) {
        return directorRepository.findById(id);
    }


    @Override
    public Director updateDirector(Long id, DirectorDTO directorPatchDTO) {

        Director director = directorRepository.findById(id).orElse(null);

        if(directorPatchDTO.getSex() != null) {
            director.setSex(directorPatchDTO.getSex());
        }
        if(directorPatchDTO.getNativeLand() != null) {
            director.setNativeLand(directorPatchDTO.getNativeLand());
        }
        if(directorPatchDTO.getAge() != null) {
            director.setAge(directorPatchDTO.getAge());
        }
        if(directorPatchDTO.getDirectorName() != null) {
            director.setDirectorName(directorPatchDTO.getDirectorName());
        }

        return directorRepository.save(director);
    }


    @Override
    public void deleteById(Long id) {
        directorRepository.deleteById(id);
    }

    @Override
    public List<Director> searchDirectors(String directorName) {
        return directorRepository.searchDirectors(directorName);
    }

    @Override
    public List<Director> findDirectorByChapterId(Long chapterId){
        return directorRepository.findDirectorByChapterId(chapterId);
    }
}
