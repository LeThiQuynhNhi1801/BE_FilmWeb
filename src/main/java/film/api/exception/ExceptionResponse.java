package film.api.exception;

import lombok.Data;

@Data
public class ExceptionResponse {
    private String messages;

    private Integer status;



    public ExceptionResponse(String messages, Integer status) {
        this.messages=messages;

        this.status = status;

    }
}
