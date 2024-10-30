package film.Home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/Admin")
public class AdminControlles {
    @GetMapping("/Login")
    public String AdminLogin() {
        return "Admin/AdminLogin";
    }
    @GetMapping("/Home")
    public String HomeLogin() {
        return "Admin/DashBoard";
    }
    @GetMapping("/User")
    public String UserManage() {
        return "Admin/UserManage";
    }
    @GetMapping("/Category")
    public String CategoryManage() {
        return "Admin/CategoryManage";
    }
    @GetMapping("/Film")
    public String FilmManage() {
        return "Admin/FilmManage";
    }
    @GetMapping("/Actor")
    public String ActorManage() {
        return "Admin/ActorManage";
    }
    @GetMapping("/AddCategory")
    public String AddCategory() {
        return "Admin/AddCategory";
    }
    @GetMapping("/AddFilm")
    public String AddFilm() {
        return "Admin/AddFilm";
    }
    @GetMapping("/AddActor")
    public String AddActor() {
        return "Admin/AddActor";
    }
    @GetMapping("/AddChapterByFilmID/{FilmID}")
    public String AddChapterFilm() {
        return "Admin/AddChapterFilm";
    }
    @GetMapping("/EditUser/{UserID}")
    public String EditUser() {
        return "Admin/EditUser";
    }
    @GetMapping("/EditCategory/{CategoryID}")
    public String EditCategory() {
        return "Admin/EditCategory";
    }
    @GetMapping("/EditFilm/{FilmID}")
    public String EditFilm() {
        return "Admin/EditFilm";
    }
    @GetMapping("/EditActor/{ActorID}")
    public String EditActor() {
        return "Admin/EditActor";
    }
    @GetMapping("/EditChapter/{ChapterID}")
    public String EditChapter() {
        return "Admin/EditChapterFilm";
    }
}
