package film.Home;


import film.api.helper.FileSystemHelper;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Controller

public class HomeControllers {
        @GetMapping("/Login")
        public String hello() {
            return "Home/Login";
        }
        @GetMapping("/Home")
        public String home() {
        return "Home/Home";
    }
        @GetMapping("/DetailVideo/{IDChapter}")
        public String DetailVideo() {
            return "Home/Detail";
        }
        @GetMapping("/Signup")
        public String Signup() {
            return "Home/Signup";
        }
        @GetMapping("/Home/UserInfor")
        public String UserInfor() {
            return "Home/UserInfor";
        }
        @GetMapping("/Home/UserChangePassword")
        public String UserChangePassword() {
        return "Home/ChangePassword";
    }
    public String layChuoiSauDauCham(String chuoi) {
        int viTriDauCham = chuoi.lastIndexOf('.');
        if (viTriDauCham >= 0 && viTriDauCham < chuoi.length() - 1) {
            return chuoi.substring(viTriDauCham + 1);
        }
        return "";
    }
    @GetMapping(
            value = "/get-file/{fileName}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public @ResponseBody byte[] getFile(@PathVariable String fileName) throws IOException {
        Path path = Paths.get(FileSystemHelper.STATIC_FILES_DIR, fileName);
        File f = new File(path.toString());
        InputStream in = new FileInputStream(f);
        return IOUtils.toByteArray(in);
    }


}
