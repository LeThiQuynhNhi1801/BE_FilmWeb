package film.api.service.impl;//package film.api.service.impl;
import com.fasterxml.jackson.databind.ObjectMapper;
import film.api.DTO.request.ContextRequestDTO;
import film.api.DTO.response.ChapterHotDTO;
import film.api.exception.NotFoundException;
import film.api.models.*;
import film.api.repository.*;
import film.api.service.RecommendService;
import film.api.util.ContextUtil;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendServiceImpl implements RecommendService {

    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private DirectorRepository directorRepository;

    @Data
    @AllArgsConstructor
    public class Score{
        private Chapter chapter;
        private double score;
    }

    public double calculateCosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        if (normA == 0 || normB == 0) {
            return 0.0; // Nếu một trong hai vector có độ dài là 0, trả về cosine similarity = 0.
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public List<Chapter> getContentRecommender(Long userID){
        List<History> history = historyRepository.historyByIdUser(userID);

        List<Long> categoryUserView= new ArrayList<>();
        for(History historyDetail:history){
            Long filmUserView=historyDetail.getChapter().getFilm().getId();
            if(filmUserView!=null){
                List<Category> categoryList= categoryRepository.getCategoryByFilmID(filmUserView);
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
                List<Actor> actorList= actorRepository.findActorByChapterId(chapterUserView);
                for(Actor actor:actorList){
                    actorUserView.add(actor.getId());
                }
            }
        }
        List<Chapter> chapterAll = chapterRepository.findAll();
        List<Category> categoryAll = categoryRepository.findAll();
        List<Director> directors = directorRepository.findAll();
        List<Actor> actorAll = actorRepository.findAll();
        //khởi tạo ma trận với hàng là các chapter người dùng chưa xem còn cột là category,actor
        int totalFeatures = actorAll.size() + categoryAll.size() + directors.size();
        int[][] matrix = new int[chapterAll.size()][totalFeatures];

        // Lặp qua từng chapter để điền giá trị
        for (int i = 0; i < chapterAll.size(); i++) {
            Chapter chapter = chapterAll.get(i);
            Film film = filmRepository.filmByIdChapter(chapter.getId());

            int columnIndex = 0;

            // Thêm giá trị cho genre
            for (Category category:categoryAll) {
                if (categoryRepository.getCategoryByFilmID(film.getId()).contains(category)) {
                    matrix[i][columnIndex] = 1;
                } else {
                    matrix[i][columnIndex] = 0;
                }
                columnIndex++;
            }

            // Thêm giá trị cho actor
            for (Actor actor : actorAll) {
                if (actorRepository.findActorByChapterId(chapter.getId()).contains(actor)) {
                    matrix[i][columnIndex] = 1;
                } else {
                    matrix[i][columnIndex] = 0;
                }
                columnIndex++;
            }

            // Thêm giá trị cho director
            for (Director director : directors) {
                if (directorRepository.findDirectorByChapterId(chapter.getId()).contains(director)) {
                    matrix[i][columnIndex] = 1;
                } else {
                    matrix[i][columnIndex] = 0;
                }
                columnIndex++;
            }
        }
        int numFeatures = matrix[0].length;
        double[] userVector = new double[numFeatures];
        List<Chapter> chapterWatched = chapterRepository.UserWatched(userID);
        List<Integer> indexChapter = new ArrayList<>();
        for (Chapter chapter:chapterAll){
            if(chapterWatched.contains(chapter)) indexChapter.add(Integer.valueOf(chapterAll.indexOf(chapter)));
        }
        for(int i=0;i<indexChapter.size();i++){
            for(int j=0;j<totalFeatures;j++){
                userVector[j]+=matrix[indexChapter.get(i)][j];
            }
        }
        for(int j=0;j<totalFeatures;j++){
            userVector[j]=userVector[j]/indexChapter.size();
        }
        List<Score> cosineSimilarities = new ArrayList<>();
        for(Chapter chapter:chapterAll){
            if(!chapterWatched.contains(chapter)){
                double[] chapterVector = new double[numFeatures];
                for(int j=0;j<totalFeatures;j++){
                    chapterVector[j]=matrix[chapterAll.indexOf(chapter)][j];
                }
                Score cosineSimilarity = new Score(chapter,calculateCosineSimilarity(userVector,chapterVector));
                cosineSimilarities.add(cosineSimilarity);
            }
        }
        Collections.sort(cosineSimilarities, new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                return Double.compare(o2.score, o1.score);  // So sánh theo cosin giảm dần
            }
        });
        for (int i=50;i<cosineSimilarities.size();i++) cosineSimilarities.remove(cosineSimilarities.get(i));
        List<Chapter> list = new ArrayList<>();
        for(Score cosineSimilarity:cosineSimilarities) list.add(cosineSimilarity.chapter);
        return list;
    }

    public List<Score> getCollaborativeRecommender(Long userID){
        List<Chapter> chapterList = getContentRecommender(userID);
//        List<Chapter> chaptersWatched = chapterRepository.UserWatched(userID);
//        List<Chapter> chapterList = chapterRepository.findAllByIdNotIn(chaptersWatched);
        List<User> allUser = userRepository.findAll();
        int[][] matrix = new int[allUser.size()][chapterList.size()];
        for(int i=0;i<allUser.size();i++){
            for(int j=0;j<chapterList.size();j++){
                try {
                    int rate = historyRepository.ratingbychapteranduser(allUser.get(i).getId(),chapterList.get(j).getId());
                    matrix[i][j] = rate;
                }catch(Exception e){
                    matrix[i][j] = -1;
                };

            }
        }
        List<Score> list = new ArrayList<>();
        for(int i=0;i<chapterList.size();i++){
            double[] vectorItem = new double[allUser.size()];
            double itemRate = 0.0;
            for(int j=0;j<allUser.size();j++){
                vectorItem[j] = matrix[j][i];
            }
            List<Score> cosineSimilarities = new ArrayList<>();
            for(int j=0;j<chapterList.size();j++){
                if(i!=j){
                    double[] vectorItem1 = new double[allUser.size()];
                    for(int k=0;k<allUser.size();k++){
                        vectorItem1[k] = matrix[k][j];
                    }
                    for(int k=0;k<allUser.size();k++){
                        if(vectorItem1[k]==-1||vectorItem[k]==-1){
                            vectorItem1[k]=0;
                            vectorItem[k]=0;
                        }
                    }
                    double cosin = calculateCosineSimilarity(vectorItem,vectorItem1)/5;
                    Score cosineSimilarity = new Score(chapterList.get(j),cosin);
                    cosineSimilarities.add(cosineSimilarity);
                }
            }
            Collections.sort(cosineSimilarities, new Comparator<Score>() {
                @Override
                public int compare(Score o1, Score o2) {
                    return Double.compare(o1.getChapter().getId(), o2.getChapter().getId());  // So sánh theo cosin giảm dần
                }
            });
            for(int j=0;j<11;j++){
                itemRate +=cosineSimilarities.get(j).score;
            }
            itemRate=itemRate/11;
            Score cosineSimilarity = new Score(chapterList.get(i),itemRate);
            list.add(cosineSimilarity);
        }

        return list;
    }
//    public List<Chapter> getContextualRecommender(){
//
//    }
    public String getTimeOfDay(LocalDateTime dateTime) {
        int hour = dateTime.getHour();
        if (hour >= 5 && hour < 12) {
            return "morning";
        } else if (hour >= 12 && hour < 17) {
            return "afternoon";
        } else if (hour >= 17 && hour < 21) {
            return "evening";
        } else {
            return "night";
        }
    }

    public String normalizeValue(String value) {
        return (value == null || value.isEmpty()) ? "unknown" : value.toLowerCase().trim();
    }
    public String createContextString(History history) {
        String time = getTimeOfDay(history.getHistoryView());
        String device = normalizeValue(history.getDevice());
        String weather = normalizeValue(history.getWeather());

        return String.format("[time=%s|device=%s|weather=%s]", time, device, weather);
    }

    private static final Logger logger = LoggerFactory.getLogger(RecommendServiceImpl.class);
    // Ma trận đặc trưng ẩn cho người dùng và phim
    private Map<String, double[]> userFactors;
    private Map<String, double[]> itemFactors;
    // Độ lệch (bias) cho người dùng, phim và ngữ cảnh
    private Map<String, Double> userBias;
    private Map<String, Double> itemBias;
    private Map<String, Double> contextBias;
    private double globalBias;

    // Tham số mô hình
    private int numFactors = 10;
    private double learningRate = 0.01;
    private double regularization = 0.02;
    private int maxIterations = 50;

    @PostConstruct
    public void init() {
        try {
            splitData(0.8);
            File modelFile = new File(MODEL_FILE);
            if (modelFile.exists()) {
                loadModel(); // Tải mô hình đã lưu
            } else {
                logger.info("Không tìm thấy mô hình, bắt đầu huấn luyện...");
                trainModel(); // Huấn luyện mới
                saveModel(); // Lưu mô hình
            }
            evaluateModel(10);
        } catch (Exception e) {
            logger.error("Lỗi trong quá trình khởi tạo mô hình", e);
            throw e;
        }
    }

    private static final String MODEL_FILE = "recommendation_model.json";

    public void saveModel() {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> model = new HashMap<>();
        model.put("userFactors", userFactors);
        model.put("itemFactors", itemFactors);
        model.put("userBias", userBias);
        model.put("itemBias", itemBias);
        model.put("contextBias", contextBias);
        model.put("globalBias", globalBias);

        try {
            objectMapper.writeValue(new File(MODEL_FILE), model);
            logger.info("Mô hình đã được lưu thành công tại {}", MODEL_FILE);
        } catch (IOException e) {
            logger.error("Lỗi khi lưu mô hình: {}", e.getMessage());
            throw new RuntimeException("Không thể lưu mô hình", e);
        }
    }

    @SuppressWarnings("unchecked")
    public void loadModel() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> model = objectMapper.readValue(new File(MODEL_FILE), Map.class);

            // Chuyển đổi userFactors và itemFactors từ List<Double> sang double[]
            Map<String, List<Double>> tempUserFactors = (Map<String, List<Double>>) model.get("userFactors");
            userFactors = new HashMap<>();
            for (Map.Entry<String, List<Double>> entry : tempUserFactors.entrySet()) {
                String key = entry.getKey();
                double[] array = entry.getValue().stream().mapToDouble(Double::doubleValue).toArray();
                userFactors.put(key, array);
            }

            Map<String, List<Double>> tempItemFactors = (Map<String, List<Double>>) model.get("itemFactors");
            itemFactors = new HashMap<>();
            for (Map.Entry<String, List<Double>> entry : tempItemFactors.entrySet()) {
                String key = entry.getKey();
                double[] array = entry.getValue().stream().mapToDouble(Double::doubleValue).toArray();
                itemFactors.put(key, array);
            }

            // Các trường khác
            userBias = (Map<String, Double>) model.get("userBias");
            itemBias = (Map<String, Double>) model.get("itemBias");
            contextBias = (Map<String, Double>) model.get("contextBias");
            globalBias = (Double) model.get("globalBias");

            logger.info("Mô hình đã được tải thành công từ {}", MODEL_FILE);
        } catch (IOException e) {
            logger.error("Lỗi khi tải mô hình: {}", e.getMessage());
            throw new RuntimeException("Không thể tải mô hình", e);
        }
    }

    private List<History> trainSet;
    private List<History> testSet;

    private void splitData(double trainRatio) {
        List<History> ratings = historyRepository.findAll();
        Collections.shuffle(ratings); // Shuffle để dữ liệu được phân bố ngẫu nhiên
        int trainSize = (int) (ratings.size() * trainRatio);

        trainSet = ratings.subList(0, trainSize); // 80% dữ liệu
        testSet = ratings.subList(trainSize, ratings.size()); // 20% dữ liệu
    }

    private void trainModel() {
        Set<String> userIds = trainSet.stream()
                .map(history -> history.getUser().getId().toString())
                .collect(Collectors.toSet());

        Set<String> itemIds = trainSet.stream()
                .map(history -> history.getChapter().getId().toString())
                .collect(Collectors.toSet());

        Set<String> contextKeys = getContextKeys(trainSet);


        // Khởi tạo các tham số
        initializeParameters(userIds, itemIds, contextKeys);

        // Huấn luyện mô hình bằng SGD
        for (int iter = 0; iter < maxIterations; iter++) {
            for (History rating : trainSet) {
                String userId = rating.getUser().getId().toString();
                String itemId = rating.getChapter().getId().toString();
                double actualRating = rating.getRate();
                String context = ContextUtil.createContextString(rating);

                double predictedRating = predictRating(userId, itemId, context);
                double error = actualRating - predictedRating;

                // Cập nhật bias
                userBias.put(userId, userBias.get(userId) + learningRate *
                        (error - regularization * userBias.get(userId)));
                itemBias.put(itemId, itemBias.get(itemId) + learningRate *
                        (error - regularization * itemBias.get(itemId)));
                contextBias.put(context, contextBias.get(context) + learningRate *
                        (error - regularization * contextBias.get(context)));

                // Cập nhật yếu tố ẩn
                double[] userFactor = userFactors.get(userId);
                double[] itemFactor = itemFactors.get(itemId);
                for (int k = 0; k < numFactors; k++) {
                    double tempUserFactor = userFactor[k];
                    userFactor[k] += learningRate * (error * itemFactor[k] -
                            regularization * userFactor[k]);
                    itemFactor[k] += learningRate * (error * tempUserFactor -
                            regularization * itemFactor[k]);
                }
            }
        }
    }

    private double predictRating(String userId, String itemId, String context) {
        double[] userFactor = userFactors.get(userId);
        double[] itemFactor = itemFactors.get(itemId);
        if (userFactor == null) {
            logger.error("userFactor is null for userId: {}", userId);
            throw new IllegalStateException("userFactor is null for userId: " + userId);
        }
        if (itemFactor == null) {
            logger.error("itemFactor is null for itemId: {}", itemId);
            throw new IllegalStateException("itemFactor is null for itemId: " + itemId);
        }
        double dotProduct = 0.0;
        for (int k = 0; k < numFactors; k++) {
            dotProduct += userFactor[k] * itemFactor[k];
        }
        double prediction = globalBias + userBias.get(userId) +
                itemBias.get(itemId) + contextBias.getOrDefault(context, 0.0) +
                dotProduct;
        return prediction;
    }

    private void initializeParameters(Set<String> userIds, Set<String> itemIds,
                                      Set<String> contextKeys) {
        Random random = new Random();
        userFactors = new HashMap<>();
        itemFactors = new HashMap<>();
        userBias = new HashMap<>();
        itemBias = new HashMap<>();
        contextBias = new HashMap<>();
        globalBias = 0.0;

        for (String userId : userIds) {
            double[] factors = new double[numFactors];
            for (int k = 0; k < numFactors; k++) {
                factors[k] = (random.nextDouble() - 0.5) * 0.01;
            }
            userFactors.put(userId, factors);
            userBias.put(userId, 0.0);
        }
        for (String itemId : itemIds) {
            double[] factors = new double[numFactors];
            for (int k = 0; k < numFactors; k++) {
                factors[k] = (random.nextDouble() - 0.5) * 0.01;
            }
            itemFactors.put(itemId, factors);
            itemBias.put(itemId, 0.0);
        }
        for (String contextKey : contextKeys) {
            contextBias.put(contextKey, 0.0);
        }
    }

    private Set<String> getContextKeys(List<History> ratings) {
        return ratings.stream()
                .map(ContextUtil::createContextString)
                .collect(Collectors.toSet());
    }

    // Các phương thức hỗ trợ như getAllItemIds() và getMovieById()
    private List<String> getAllItemIds() {
//        List<Chapter> list = chapterRepository.findAll();
//        List<String> longList = new ArrayList<>();
//        for (Chapter chapter:list){
//            longList.add(chapter.getId().toString());
//        }
//        return longList;
        return new ArrayList<>(itemFactors.keySet());
    }

    private Chapter getMovieById(String itemId) {
        Chapter chapter = chapterRepository.ChapterByIdChapter(Long.valueOf(itemId));
        // Lấy thông tin phim từ cơ sở dữ liệu hoặc dịch vụ
        return chapter;
    }

    @Override
    public List<Chapter> recommend(String username, ContextRequestDTO contextRequestDTO, int topN) {
        String context = ContextUtil.createContextString(contextRequestDTO);
        List<String> allItemIds = getAllItemIds();
        Long userId = userRepository.findByUsername(username).getId();
        List<History> history = historyRepository.historyByIdUser(userId);
        if (history.isEmpty()) {
            LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
            List<Object[]> results = historyRepository.getChaptersHotCount(oneWeekAgo, LocalDateTime.now());
            List<Chapter> chapterHots = new ArrayList<>();
            for (Object[] result : results) {
                ChapterHotDTO chapterHotDTO = new ChapterHotDTO();
                BigInteger bigInt = (BigInteger) result[0];
                Long chapterID = bigInt.longValue();
                Chapter chapter = chapterRepository.findById(chapterID)
                        .orElseThrow(() -> new NotFoundException("Chapter not found"));
                if (chapter != null) chapterHots.add(chapter);

            }
            return chapterHots;
        }
        List<Score> scoreList = new ArrayList<>();

        for (String itemId : allItemIds) {
            double score = predictRating(userId.toString(), itemId, context);
            Score score1 = new Score(getMovieById(itemId),score);
            scoreList.add(score1);
        }
        List<Score> collaborative = getCollaborativeRecommender(userId);

        List<Score> result = new ArrayList<>();
        for(int i=0;i<collaborative.size();i++){
            for(int j=0;j<scoreList.size();j++){
                if(collaborative.get(i).getChapter().getId()==scoreList.get(j).chapter.getId()){
                    Score score1 = new Score(collaborative.get(i).getChapter(),(collaborative.get(i).getScore()+scoreList.get(j).getChapter().getId())/2);
                    result.add(score1);
                }
            }
        }
        Collections.sort(result, new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                return Double.compare(o2.getScore(), o1.getScore());  // So sánh theo cosin giảm dần
            }
        });
        List<Chapter> recommedList = new ArrayList<>();
        for(int i=0;i<topN;i++){
            recommedList.add(result.get(i).getChapter());
        }

        return recommedList;
    }
    private double calculatePrecisionAtN(String username, int n, ContextRequestDTO contextRequestDTO) {
        List<Chapter> recommendedItems = recommend(username, contextRequestDTO, n); // Gọi hàm recommend để lấy Top-N gợi ý
        Long userId = userRepository.findByUsername(username).getId();
        List<Chapter> relevantItems = getRelevantItems(userId); // Lấy các mục liên quan từ testSet

        int hits = 0; // Số mục liên quan trong danh sách gợi ý
        for (Chapter item : recommendedItems) {
            if (relevantItems.contains(item)) {
                hits++;
            }
        }

        return (double) hits / n;
    }

    private double calculateRecallAtN(String username, int n, ContextRequestDTO contextRequestDTO) {
        List<Chapter> recommendedItems = recommend(username, contextRequestDTO, n); // Gọi hàm recommend để lấy Top-N gợi ý
        Long userId = userRepository.findByUsername(username).getId();
        List<Chapter> relevantItems = getRelevantItems(userId); // Lấy các mục liên quan từ testSet

        int hits = 0; // Số mục liên quan trong danh sách gợi ý
        for (Chapter item : recommendedItems) {
            if (relevantItems.contains(item)) {
                hits++;
            }
        }

        return relevantItems.isEmpty() ? 0.0 : (double) hits / relevantItems.size();
    }
//    private List<Chapter> getRelevantItems(Long userId) {
//        // Lấy danh sách các mục (items) mà người dùng đã đánh giá trong tập kiểm tra (testSet)
//        return testSet.stream()
//                .filter(history -> history.getUser().getId().equals(userId))
//                .map(History::getChapter)
//                .collect(Collectors.toList());
//    }
    private void evaluateModel(int n) {
        double precisionSum = 0.0;
        double recallSum = 0.0;

        // Lấy danh sách tất cả các username từ tập kiểm tra
        Set<String> testUsernames = testSet.stream().limit(30)
                .map(history -> history.getUser().getUsername())
                .collect(Collectors.toSet());

        // Tính Precision@N và Recall@N cho từng người dùng
        for (String username : testUsernames) {
            ContextRequestDTO contextRequestDTO = new ContextRequestDTO("computer", "cloudy", Timestamp.valueOf(LocalDateTime.now()));

            precisionSum += calculatePrecisionAtN(username, n, contextRequestDTO);
            recallSum += calculateRecallAtN(username, n, contextRequestDTO);
        }

        // Trung bình Precision và Recall trên tất cả người dùng
        double precisionAtN = precisionSum / testUsernames.size();
        double recallAtN = recallSum / testUsernames.size();

        System.out.println("Precision@" + n + ": " + precisionAtN);
        System.out.println("Recall@" + n + ": " + recallAtN);
    }
    private Map<Long, List<Chapter>> relevantItemsCache = new HashMap<>();
    private List<Chapter> getRelevantItems(Long userId) {
        // Nếu userId đã tồn tại trong cache, trả về danh sách được lưu
        return relevantItemsCache.computeIfAbsent(userId, id ->
                testSet.stream()
                        .filter(history -> history.getUser().getId().equals(id)) // Lọc các bản ghi của user
                        .map(History::getChapter) // Lấy chapter từ lịch sử
                        .collect(Collectors.toList()) // Chuyển thành danh sách
        );
    }


}



