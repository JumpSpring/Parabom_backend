package taveSpring.parabom.Controller.Response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import taveSpring.parabom.Controller.Response.BasicResponse;
import taveSpring.parabom.Controller.Response.ErrorResponse;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<? extends BasicResponse> IllegalArgumentHandler(IllegalArgumentException e){
        log.warn("-----IllegalArgumentException-----");
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<? extends BasicResponse> RunTimeHandler(RuntimeException e){
        log.warn("-----RunTimeException-----");
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
    }

    // 유효성 검사에 대한 Error Handler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<? extends BasicResponse> methodValidException(MethodArgumentNotValidException e){
        log.warn("-----MethodArgumentNotValidException-----");
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getBindingResult().getFieldError().getDefaultMessage()));
    }

}
