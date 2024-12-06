package film.api.util;


import film.api.DTO.request.ContextRequestDTO;
import film.api.models.History;

import java.util.Map;

public class ContextUtil {

    public static String createContextString(History rating) {
        String time = getTimeOfDay(rating.getTime());
        String device = normalizeValue(rating.getDevice());
        String weather = normalizeValue(rating.getWeather());
        return String.format("time=%s|device=%s|weather=%s",
                time, device, weather);
    }

    public static String createContextString(ContextRequestDTO contextRequestDTO) {
        String time = getTimeOfDay(contextRequestDTO.getTime());
        String device = normalizeValue(contextRequestDTO.getDevice());
        String weather = normalizeValue(contextRequestDTO.getWeather());
        return String.format("time=%s|device=%s|weather=%s",
                time, device, weather);
    }

    private static String getTimeOfDay(java.sql.Timestamp timestamp) {
        java.time.LocalDateTime dateTime = timestamp.toLocalDateTime();
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

    private static String normalizeValue(String value) {
        return (value == null || value.isEmpty()) ? "unknown"
                : value.toLowerCase().trim();
    }
}
