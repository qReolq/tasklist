package qreol.project.tasklist.web.validation.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import qreol.project.tasklist.domain.exception.ResourceNotValidException;
import qreol.project.tasklist.domain.user.User;
import qreol.project.tasklist.repository.UserRepository;
import qreol.project.tasklist.web.dto.user.UserDTO;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {

    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO user = (UserDTO) target;

        validatePassword(user, errors);
        validateUsername(user, errors);

        checkError(errors);
    }

    public void validateUpdate(UserDTO user, Errors errors) {
        validateUsername(user, errors);
        checkError(errors);
    }

    private void validateUsername(UserDTO user, Errors errors) {
        Optional<User> foundUserByUsername = userRepository.findByUsername(user.getUsername());

        if (foundUserByUsername.isPresent()) {
            errors.rejectValue("username", "", "This username is already taken");
        }
    }

    private void validatePassword(UserDTO user, Errors errors) {
        if (user.getPasswordConfirmation() == null || user.getPassword() == null
                || !user.getPassword().equals(user.getPasswordConfirmation()))
            errors.rejectValue("passwordConfirmation", "", "Password confirmation should be correct");
    }

    private void checkError(Errors errors) {
        if (errors.hasErrors()) throw new ResourceNotValidException(errors.getFieldErrors());
    }

}
