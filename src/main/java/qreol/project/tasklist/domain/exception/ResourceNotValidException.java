package qreol.project.tasklist.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@AllArgsConstructor
@Getter
public class ResourceNotValidException extends RuntimeException{

    private List<FieldError> errors;

}
