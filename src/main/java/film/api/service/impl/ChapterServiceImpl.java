package film.api.service.impl;

import film.api.DTO.request.ChapterRequestDTO;
import film.api.exception.InvalidInputException;
import film.api.exception.NotFoundException;
import film.api.helper.FileSystemHelper;
import film.api.models.*;
import film.api.repository.*;
import film.api.service.ChapterService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChapterServiceImpl implements ChapterService {
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private ActorChapterRepository actorChapterRepository;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private DirectorRepository directorRepository;
    @Autowired
    private DirectorChapterRepository directorChapterRepository;

    @Override
    public String getUniqueFileName(String fileName, String uploadDir) {
        String newFileName = fileName;
        int index = 1;
        File uploadedFile = new File(uploadDir +"/"+ newFileName);
        while (uploadedFile.exists()) {
            newFileName = fileName.replaceFirst("[.][^.]+$", "") + "(" + index + ")" + "." +
                    FilenameUtils.getExtension(fileName);
            uploadedFile = new File(uploadDir +"/"+ newFileName);
            index++;
        }
        return newFileName;
    }

    @Override
    public String saveFile(MultipartFile file, String typeFile){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileNameNew = getUniqueFileName(fileName, FileSystemHelper.STATIC_FILES_DIR);

        // Đường dẫn để lưu file
        Path path = Paths.get(FileSystemHelper.STATIC_FILES_DIR, fileNameNew);
        System.out.println("Saved file path: " + path.toString());

        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error saving file", e);
        }

        // Kiểm tra phần mở rộng của file
        String extension = FilenameUtils.getExtension(fileNameNew).toLowerCase();
        String urlBasePath;

        if ("mp4".equals(extension)) {
            urlBasePath = "/play/";
        } else {
            urlBasePath = "/api/files/";
        }

        // Tạo đường dẫn URL trả về
        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(urlBasePath)
                .path(fileNameNew)
                .toUriString();

        return fileUrl;
//        // Lưu file vào thư mục image
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        String fileNameNew =getUniqueFileName(fileName, FileSystemHelper.STATIC_FILES_DIR);
//
//        Path path = Paths.get(FileSystemHelper.STATIC_FILES_DIR, fileNameNew);
//        System.out.println("saved file path: "+ path.toString());
//        try {
//            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        // Lưu đường dẫn của file vào CSDL
//        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/play/")
//                .path(fileNameNew)
//                .toUriString();
//        return  fileUrl;
    }


    @Override
    public List<Chapter> getList(){
        return chapterRepository.findAll();
    }

    @Override
    public List <Chapter> getChapterByFilmID(Long filmID){
        return chapterRepository.getChapterByFilmID(filmID);
    }

    @Override
    public List<Chapter> chapterByChapterId(Long chapterId){
        return chapterRepository.chapterByChapterId(chapterId);
    }

    @Override
    public Chapter getChapter(Long id){
        return chapterRepository.ChapterByIdChapter(id);
    }

    @Override
    public Chapter findByID(Long chapterID){
        return chapterRepository.findById(chapterID)
                .orElseThrow(() -> new NotFoundException("Chapter not found"));
    }

    @Override
    public Chapter addChapter(Long filmID, ChapterRequestDTO chapterPost){
        Film film= filmRepository.findById(filmID)
                .orElseThrow(() -> new NotFoundException("Film not found"));
        if(chapterPost.getChapterName()==null ||chapterPost.getChapterName().replaceAll("\\s+", "").equals("")){
            throw new InvalidInputException("Vui Lòng nhập Tên Chapter");
        }
        if(chapterPost.getListActor()==null ||chapterPost.getListActor().replaceAll("\\s+", "").equals("")){
            throw new InvalidInputException("Vui Lòng nhập Tên Actor");
        }
        if(chapterPost.getListDirector()==null ||chapterPost.getListDirector().replaceAll("\\s+", "").equals("")){
            throw new InvalidInputException("Vui Lòng nhập Tên Director");
        }

        if(chapterPost.getChapterDescription()==null||chapterPost.getChapterDescription().replaceAll("\\s+", "").equals("")){
            throw new InvalidInputException("Vui Lòng nhập  Mô Tả Chapter");
        }
        if(chapterPost.getTrailerChapter()==null){
            throw new InvalidInputException("Vui Lòng nhập  Trailer Chapter");
        }
        if(chapterPost.getChapterImage()==null){
            throw new InvalidInputException("Vui Lòng nhập  Image Chapter");
        }

        String status="Đang Ra";
        String video="";
        if(chapterPost.getVideo()!=null){
             video = saveFile(chapterPost.getVideo(),"videos");
            status="Đã Ra";

        }
        String trailerChapter = saveFile(chapterPost.getTrailerChapter(),"Videos");
        String imageChapter = saveFile(chapterPost.getChapterImage(),"Images");
        Integer chapterNumber = chapterRepository.chapternumberbyIdFilmInt(filmID);
        if(chapterNumber==null) {
            chapterNumber = 1;
        }else {
            chapterNumber+=1;
        }
        Chapter chapter =new Chapter(null,"",chapterPost.getChapterName(),chapterNumber,video,film,chapterPost.getChapterDescription(),trailerChapter,imageChapter, LocalDateTime.now(),null,status);
        if(chapterPost.getVideo()!=null){
            chapter.setChapterPremieredDay(LocalDateTime.now());
        }
        String[] actorString = chapterPost.getListActor().split(",");
        Long[] actorList = new Long[actorString.length];
        for(int i = 0; i < actorString.length; i++) {
            actorList[i] = Long.parseLong(actorString[i]);
        }
        Chapter chapterNew=chapterRepository.save(chapter);
        for(Long i:actorList){
            Actor actor=actorRepository.findById(i).orElseThrow(null);
            actorChapterRepository.save(new ActorChapter(null,actor,chapterNew));
        }
        String[] directorString = chapterPost.getListDirector().split(",");
        Long[] directorList = new Long[directorString.length];
        for(int i = 0; i < directorString.length; i++) {
            directorList[i] = Long.parseLong(directorString[i]);
        }
        for(Long i:directorList){
            Director director=directorRepository.findById(i).orElseThrow(null);
            directorChapterRepository.save(new DirectorChapter(null,director,chapterNew));
        }
        return chapterNew;
    }

    @Override
    public Chapter updateChapter(Long chapterID,ChapterRequestDTO chapterPatch) {
        Chapter chapter = chapterRepository.findById(chapterID)
                .orElseThrow(() -> new NotFoundException("Chapter not found"));

        if (chapterPatch.getChapterName() != null) {
            chapter.setChapterName(chapterPatch.getChapterName());
        }
        if (chapterPatch.getChapterDescription() != null) {
            chapter.setChapterDescription(chapterPatch.getChapterDescription());
        }
        if(chapterPatch.getListActor()!=null ){
            if(chapterPatch.getListActor().replaceAll("\\s+", "").isEmpty())
                throw new IllegalArgumentException("Vui Lòng nhập Tên Chapter");
            List<ActorChapter> actorChapters=actorChapterRepository.findActorChapterByChapterId(chapterID);
            actorChapterRepository.deleteAll(actorChapters);
            String[] actorString = chapterPatch.getListActor().split(",");
            Long[] actorList = new Long[actorString.length];
            for(int i = 0; i < actorString.length; i++) {
                actorList[i] = Long.parseLong(actorString[i]);
            }

            for(Long i:actorList){
                Actor actor=actorRepository.findById(i)
                        .orElseThrow(() -> new NotFoundException("Actor not found"));;
                actorChapterRepository.save(new ActorChapter(null,actor,chapter));
            }

        }

        if(chapterPatch.getListDirector()!=null ){
            if(chapterPatch.getListDirector().replaceAll("\\s+", "").equals(""))throw new IllegalArgumentException("Vui Lòng nhập Tên Chapter");
            List<DirectorChapter> directorChapters=directorChapterRepository.findDirectorChapterByChapterId(chapterID);
            for(DirectorChapter directorChapter:directorChapters){
                directorChapterRepository.delete(directorChapter);
            }
            String[] directorString = chapterPatch.getListDirector().split(",");
            Long[] directorList = new Long[directorString.length];
            for(int i = 0; i < directorString.length; i++) {
                directorList[i] = Long.parseLong(directorString[i]);
            }

            for(Long i:directorList){
                Director director=directorRepository.findById(i).orElseThrow(null);
                directorChapterRepository.save(new DirectorChapter(null,director,chapter));
            }

        }
        if (chapterPatch.getChapterImage() != null) {
            String image = saveFile(chapterPatch.getChapterImage(),"Images");

            chapter.setChapterImage(image);
        }
        if (chapterPatch.getTrailerChapter() != null) {
            String trailer = saveFile(chapterPatch.getChapterImage(),"Videos");
            chapter.setTrailerChapter(trailer);
        }
        if (chapterPatch.getVideo() != null) {
            String video = saveFile(chapterPatch.getVideo(),"Videos");
            chapter.setChapterStatus("Đã Ra");
            chapter.setVideo(video);
        }
        return chapter;
    }

//    @Override
//    public List<Chapter> findAllByNotInId(List<Long> chapterIDList){
//        return chapterRepository.findAllByIdNotIn(chapterIDList);
//    }

    @Override
    public List<Chapter> newestChapters(){
        return chapterRepository.Newest();
    }

//    @Override
//    public List<Long> getIDChapter(A a) {
//        List<Long> list = new ArrayList<>();
//        List<String> stringList = a.getList();
//        for(int i=0;i<stringList.size();i++){
//            list.add(chapterRepository.getId(stringList.get(i)));
//        }
//        return list;
//    }
}
