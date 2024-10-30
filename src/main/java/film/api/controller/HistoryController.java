package film.api.controller;

import film.api.Cosine.CosineSimilarity;
import film.api.DTO.*;
import film.api.configuration.security.JWTUtil;
import film.api.exception.NotFoundException;
import film.api.models.*;
import film.api.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.math3.linear.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ApiV1")
public class HistoryController {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private ActorService actorService;
    @Secured({"ROLE_ADMIN"})
    @GetMapping("/ChapterHotFromDaytoDay")
    public ResponseEntity<?> getChapterHotFromDayToDay(
            @RequestParam(value = "fromDay", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDay,
            @RequestParam(value = "toDay", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDay) {

        if (fromDay == null) {
            fromDay = LocalDate.parse("1945-12-22", DateTimeFormatter.ISO_DATE);
        }
        if (toDay == null) {
            toDay = LocalDate.now();
            toDay.plusDays(1);
        }

        List<ChapterHotDTO> chapterHotList = new ArrayList<>();


        return ResponseEntity.ok(historyService.getChaptersHotCount(fromDay.atStartOfDay(),toDay.atStartOfDay()));
    }
    @Secured({"ROLE_ADMIN","ROlE_USER"})
    @GetMapping("/HistoryByChapterIDAndUserLogin/{chapterId}")
    public ResponseEntity<?> getHistoryByChapterIDAndUserLogin(HttpServletRequest request, @PathVariable Long chapterId) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        Long userID = historyService.getUserID(username);
        History history = historyService.getHistory(chapterId, userID);
        if(history==null){
           throw  new NotFoundException("User chưa xem Chapter này.");
        }
    return new ResponseEntity<>(new HistoryDTO(history), HttpStatus.OK);
    }
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PostMapping("/HistoryByChapterIDAndUserLogin/{chapterId}")
    public ResponseEntity<?> addHistoryByChapterIDAndUserLogin(HttpServletRequest request, @PathVariable Long chapterId){
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        Long userID = historyService.getUserID(username);

        History history =new History   (null,0,chapterService.getChapter(chapterId),userService.findById(userID),0,LocalDateTime.now());
        historyService.saveHistory(history);
        return new ResponseEntity<>(new HistoryDTO(history), HttpStatus.CREATED);
    }
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @PatchMapping("/HistoryByChapterIDAndUserLogin/{chapterId}")
    public ResponseEntity<?> updateHistoryByChapterIDAndUserLogin(HttpServletRequest request, @PathVariable Long chapterId, @RequestBody HistoryRequestDTO historyRequestDTO){
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        User user =userService.findByUsername(username);
        if(user ==null){
            throw new NotFoundException("User này không tồn tại");
        }
        Chapter chapter=chapterService.findByID(chapterId);
        if(chapter ==null){
            throw new NotFoundException("Chapter này không tồn tại");
        }
        History history=historyService.updateHistory(user,chapter,historyRequestDTO);
        return new ResponseEntity<>(new HistoryDTO(history), HttpStatus.OK);
    }
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("/HistoryUserLogin")
    public ResponseEntity<?> getListHistory(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        Long userID = historyService.getUserID(username);

        List<Chapter> chapters =historyService.findChaptersByUserId(userID);
        List<ChapterDTO> chapterDTOS=new ArrayList<>();
        for(Chapter chapter:chapters){
            chapterDTOS.add(new ChapterDTO(chapter));
        }
        return new ResponseEntity<>(chapterDTOS, HttpStatus.OK);
    }
    public double sumrow(double[] row){
        double sum = 0.0;
        for (double element : row) {
            sum += element;
        }
        return sum;
    }
    public double countZeroRow(double[] row){
        double count = 0.0;
        for (double element : row) {
            if(element==(double) 0){
                count+=1;
            }
        }
        return count;
    }
    public double[] ChangeToAray(List<Double> list){
        double[] array = new double[list.size()];

// Sao chép các giá trị từ list sang mảng
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    @GetMapping("Recommend")
    public ResponseEntity<?> getRecommend(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        Long userID = historyService.getUserID(username);
        List<History> history = historyService.getListhistory(userID);
        List<Chapter> recommendedChapters = new ArrayList<>();

        // Nếu User mới thì liệt k toàn bộ Chapter Hot Trong Tuần
        if (history.isEmpty()) {
            LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
            List<Chapter> chapterHotCounts = historyService.getChaptersHot(oneWeekAgo, LocalDateTime.now());
            return new ResponseEntity<>(chapterHotCounts,HttpStatus.OK);
        }

        //------------Content Based System-------------------

        //Lấy Toàn bộ những category mà người dùng đang đăng nhập xem
        List<Long> categoryUserView= new ArrayList<>();
        for(History historyDetail:history){
            Long filmUserView=historyDetail.getChapter().getFilm().getId();
            if(filmUserView!=null){
                List<Category> categoryList= categoryService.getCategoryByFilmID(filmUserView);
                for(Category category:categoryList){
                    categoryUserView.add(category.getId());
                }
            }
        }
        //Lấy Toàn bộ những actor mà người dùng đang đăng nhập xem
        List<Long> actorUserView= new ArrayList<>();
        for(History historyDetail:history){
            Long chapterUserView = historyDetail.getChapter().getId();
            if(chapterUserView!=null){
                List<Actor> actorList= actorService.findActorByChapterId(chapterUserView);
                for(Actor actor:actorList){
                    actorUserView.add(actor.getId());
                }
            }
        }

       List<Chapter> chapterAll = chapterService.findAllByNotInId(history.stream().map(History::getChapter).map(Chapter::getId).collect(Collectors.toList()));

        // Lấy toàn bộ Thể loại
        List<Category> categoryAll = categoryService.findAll();

        // Lấy toàn bộ diễn viên
        List<Actor> actorAll = actorService.getList();
        //khởi tạo ma trận với hàng là các chapter người dùng chưa xem còn cột là category,actor
        double[][] chapter_array = new double[chapterAll.size()][categoryAll.size()+actorAll.size()];
        RealMatrix chapter_matrix = new Array2DRowRealMatrix(chapter_array);
        // khởi tạo phần category cho chapterMatrix
        for (int i = 0; i < chapterAll.size(); i++) {
            for (int j = 0; j < categoryAll.size(); j++) {
                if (categoryService.getCategoryByFilmID( chapterAll.get(i).getFilm().getId()).contains(categoryAll.get(j))) {
                    chapter_matrix.setEntry(i,j,1);
                }
            }
        }

        // khởi tạo phần actor cho chapterMatrix
        for (int i = 0; i < chapterAll.size(); i++) {
            for (int j = 0; j < actorAll.size(); j++) {
                if (actorService.findActorByChapterId( chapterAll.get(i).getFilm().getId()).contains(actorAll.get(j))) {
                    chapter_matrix.setEntry(i,j+categoryAll.size(),1);
                }
            }
        }
        //khởi tạo ma trận với hàng là User đang đăng nhập(1 hàng) còn cột là category,actor
        List<Double> userLogin_matrix = new ArrayList<>();
        // khởi tạo phần category cho userLogin_matrix
        for(Category category:categoryAll){
            if(categoryUserView.contains(category.getId()))
                userLogin_matrix.add((double) Collections.frequency(categoryUserView, category.getId())/categoryUserView.size());
            else userLogin_matrix.add((double)0);
        }
        // khởi tạo phần actor cho userLogin_matrix
        for(Actor actor:actorAll){
            if(actorUserView.contains(actor.getId()))
                userLogin_matrix.add((double) Collections.frequency(actorUserView, actor.getId())/actorUserView.size());
            else userLogin_matrix.add((double)0);
        }

        double[] similarities = new double[chapterAll.size()];
        RealVector vectorObj = new ArrayRealVector(userLogin_matrix.stream().mapToDouble(Double::doubleValue).toArray());
        for (int i = 0; i <chapterAll.size(); i++) {
            RealVector row = new ArrayRealVector(chapter_matrix.getRow(i));
            CosineSimilarity a=  new CosineSimilarity();
            similarities[i]=a.cosineSimilarity(row,vectorObj);
        }
        // Sắp xếp mảng index theo giá trị của mảng arr
        Integer[] index = new Integer[similarities.length];
        for (int i = 0; i < similarities.length; i++) {
            index[i] = i;
        }
        Arrays.sort(index, new Comparator<Integer>() {
            @Override
            public int compare(Integer i1, Integer i2) {
                 if(similarities[i1]==similarities[i2]){
                     return 0;
                 } else if (similarities[i1]>similarities[i2]){
                     return  -1;
                 }return 1;
            }
        });
        Integer[] a = new Integer[similarities.length];
        for (int i = 0; i < similarities.length; i++) {
            a[i] = index[i];
        }
        for(Integer indexDetail:index){
            if(recommendedChapters.size()>4)break;
            if(chapterAll.get(indexDetail).getChapterStatus().equals("Đã Ra")){
                recommendedChapters.add(chapterAll.get(indexDetail));
            }
        }
        //----------------------------------Lọc cộng tác theo item-item----------

        //Lấy Toàn bộ người dùng
        List<User> users =userService.findAll();
        //Tìm kiếm vị trí của người dùng
        Integer indexUserLogin=0;
        for(int i=0;i<users.size();i++){
            if(users.get(i).getId()==userID){
                 indexUserLogin=i;
            }
        }
        //Lấy Toàn Bộ Chapter
        List<Chapter> chapters=chapterService.getList();
        //Tạo ma trận full 0 với số hàng là số chapter và số cột là số User
        double [][]ratings_array= new double[chapters.size()][users.size()];
        RealMatrix ratings_matrix = new Array2DRowRealMatrix(ratings_array);
        //thay đổi các giá trị 0 thành điểm số Rate trong history nếu người dùng đã đánh giá
        for(int i=0;i<chapters.size();i++){
            for(int j=0;j<users.size();j++){
                History rating = historyService.getHistory(chapters.get(i).getId(),users.get(j).getId());
                if (rating==null){
                    ratings_matrix.setEntry(i,j,0);
                }else {
                    ratings_matrix.setEntry(i,j,rating.getRate());
                }
            }
        }
//        chuẩn hóa ma trận để giảm các rating giống nhau thể hiện rõ hơn sự đánh giá trái triều:

        double [][]ratingsx_array= new double[chapters.size()][users.size()];
        RealMatrix ratings_matrixx = new Array2DRowRealMatrix(ratingsx_array);
        double avg =0;
        for(int i=0;i<chapters.size();i++){
            Boolean fullZero= false;
            try{
                 avg=sumrow(ratings_matrix.getRow(i))/countZeroRow(ratings_matrix.getRow(i));
            }catch (Exception e){
                fullZero=true;
            }
            if(fullZero==false ){
                for (int j=0;j<users.size();j++){
                    if(ratings_matrix.getEntry(i,j)!=0){
                        ratings_matrix.setEntry(i,j,ratings_matrix.getEntry(i,j)-avg);
                    }
                }
            }
        }
        //Hoàn thành matran userlogin với chapter
        List<Double> ratingChapterUser=new ArrayList<>();
        for(int i=0;i<chapters.size();i++){
            if(ratings_matrix.getEntry(i,indexUserLogin)!=0){
                ratingChapterUser.add((double) 0);
            }else{
                List<Double> ratingList=new ArrayList<>();
                for(int j=0;j<chapters.size();j++){
                    List<Double> newChapter1 = new ArrayList<>();
                    List<Double> newChapter2 = new ArrayList<>();

                    for (int l = 0; l < ratings_matrixx.getRow(0).length; l++) {
                        if (ratings_matrixx.getEntry(i,l) != 0 && ratings_matrixx.getEntry(j,l) != 0) {
                            newChapter1.add(ratings_matrixx.getEntry(i,l));
                            newChapter2.add(ratings_matrixx.getEntry(j,l));
                        }
                    }
//                     Tính độ tương đồng giữa 2 chapter bằng cosin
                    CosineSimilarity b=  new CosineSimilarity();
                    ratingList.add(b.cosineSimilarity(ChangeToAray(newChapter1),ChangeToAray(newChapter2)));
//               //    lấy ra 2 chapter giông chapter đang tính rating nhất và tính trugn bình có trọng số
                }
                Integer[] indexs = new Integer[ratingList.size()];
                for (Integer h = 0; h < ratingList.size(); h++) {
                    indexs[h] = h;
                }
                Arrays.sort(indexs, new Comparator<Integer>() {
                    @Override
                    public int compare(Integer i1, Integer i2) {
                        if(ratingList.get(i1)==ratingList.get(i2)){
                            return 0;
                        } else if (ratingList.get(i1)>ratingList.get(i2)){
                            return  -1;
                        }return 1;
                    }
                });
                ratingChapterUser.add((ratingList.get(indexs[0])*ratings_matrix.getEntry(indexs[0],indexUserLogin)+ratingList.get(indexs[1])*ratings_matrix.getEntry(indexs[1],indexUserLogin))/(ratingList.get(indexs[0])+ratingList.get(indexs[0])));
            }
        }
        return  new ResponseEntity<>(recommendedChapters,HttpStatus.OK);
    }

}
