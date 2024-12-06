package film.api;

import java.util.*;
import java.util.stream.Collectors;

public class RecommendationEvaluator {

    // Tính Precision@N
    public static double calculatePrecisionAtN(Set<String> relevantItems, Set<String> predictedItems, int n) {
        int intersectionCount = (int) relevantItems.stream()
                .filter(predictedItems::contains)
                .limit(n)
                .count();

        return (double) intersectionCount / n;
    }

    // Tính Recall@N
    public static double calculateRecallAtN(Set<String> relevantItems, Set<String> predictedItems, int n) {
        int intersectionCount = (int) relevantItems.stream()
                .filter(predictedItems::contains)
                .limit(n)
                .count();

        return (double) intersectionCount / relevantItems.size();
    }

    // Tính F-measure@N
    public static double calculateFMeasureAtN(double precision, double recall) {
        if (precision + recall == 0) return 0;
        return 2 * (precision * recall) / (precision + recall);
    }
}
