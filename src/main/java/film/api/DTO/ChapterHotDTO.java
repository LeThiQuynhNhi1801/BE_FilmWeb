package film.api.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class ChapterHotDTO {
    private BigInteger id;
    private BigInteger CountView;
    private BigDecimal RateAvg;
    private String ChapterImage;
    private String ChapterName;

    // getters and setters
}

