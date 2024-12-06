package film.api.controller;

import film.api.models.Chapter;
import film.api.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("")
public class VideoController {

    private static final String VIDEO_DIRECTORY = "Media"; // Thư mục chứa file video
        // Tên file video
    @Autowired
    private ChapterService chapterService;
    @GetMapping("/play/{chapterVideo}")
    public ResponseEntity<Resource> playVideo(@PathVariable("chapterVideo") String chapterVideo) {
        try {
            String VIDEO_NAME = chapterVideo;
            // Lấy đường dẫn tới file video
            Path videoPath = Paths.get(VIDEO_DIRECTORY).resolve(VIDEO_NAME).toAbsolutePath();
            Resource videoResource = new UrlResource(videoPath.toUri());

            if (!videoResource.exists() || !videoResource.isReadable()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }

            // Trả về file video dưới dạng stream
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM) // Hoặc MediaType.VIDEO_MP4
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + VIDEO_NAME + "\"")
                    .body(videoResource);

        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
