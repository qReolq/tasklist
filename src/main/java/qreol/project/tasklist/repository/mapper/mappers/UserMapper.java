package qreol.project.tasklist.repository.mapper.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import qreol.project.tasklist.domain.user.User;
import qreol.project.tasklist.web.dto.user.UserDTO;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper mapper;

    public UserDTO toDTO(User user) {
        return mapper.map(user, UserDTO.class);
    }

    public User toEntity(UserDTO userDTO) {
        return mapper.map(userDTO, User.class);
    }

}
