package film.api.controller;



import film.api.DTO.request.DirectorDTO;
import film.api.exception.NotFoundException;
import film.api.models.Director;
import film.api.service.DirectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@CrossOrigin("*")
@RequestMapping(value = "/ApiV1", produces = "application/json")
public class DirectorController {

    @Autowired
    private DirectorService directorService;
    @Secured({"ROLE_ADMIN","ROLE_USER"})

    @GetMapping("/AllDirector")
    public ResponseEntity<?> getAllDirectors() {


        return new ResponseEntity<>(directorService.getList(), HttpStatus.OK);
    }
    @Secured({"ROLE_ADMIN"})
    @PostMapping("/AllDirector")
    public ResponseEntity<?> addDirector(@RequestBody Director director) {


        return new ResponseEntity<>(directorService.addDirector(director), HttpStatus.CREATED);
    }
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/DirectorByID/{id}")
    public ResponseEntity<?> getDirectorById(@PathVariable Long id) {
        Optional<Director> director = directorService.findById(id);

        if (!director.isPresent()) {
            throw new NotFoundException("Không Có Director");
        }
        return new ResponseEntity<>(new DirectorDTO(director.get()), HttpStatus.OK);
    }
    @Secured({"ROLE_ADMIN"})
    @PatchMapping("/DirectorByID/{DirectorByID}")
    public ResponseEntity<Object> updateDirector(@PathVariable("DirectorByID") Long id, @RequestBody DirectorDTO directorPost) {

        return new ResponseEntity<>(directorService.updateDirector(id,directorPost), HttpStatus.OK);
    }
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/DirectorByID/{DirectorByID}")
    public ResponseEntity<?> deleteDirector(@PathVariable Long DirectorByID) {
        directorService.deleteById(DirectorByID);
        return new ResponseEntity<>("Xóa thành công", HttpStatus.NO_CONTENT);
    }
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/DirectorByName/{DirectorName}")
    public ResponseEntity<List<Director>> searchDirectors(@PathVariable("DirectorName") String directorName) {
        List<Director> directors = directorService.searchDirectors(directorName);
        return new ResponseEntity<>(directors, HttpStatus.OK);
    }

}

