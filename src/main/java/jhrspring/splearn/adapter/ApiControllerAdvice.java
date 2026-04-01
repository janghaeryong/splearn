package jhrspring.splearn.adapter;

import jhrspring.splearn.domain.member.DuplicateEmailException;
import jhrspring.splearn.domain.member.DuplicateProfileException;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ProblemDetail exceptionHandler(Exception e) {
        return getProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler({DuplicateEmailException.class, DuplicateProfileException.class})
    public ProblemDetail emailExceptionHandler(DuplicateEmailException e) {
        //RFC9457
        return getProblemDetail(HttpStatus.CONFLICT, e);
    }

    private static @NonNull ProblemDetail getProblemDetail(HttpStatus status, Exception e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, e.getMessage());
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setProperty("exception", e.getClass().getSimpleName());
        return detail;
    }

}
