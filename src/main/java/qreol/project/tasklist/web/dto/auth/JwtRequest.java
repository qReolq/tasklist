package qreol.project.tasklist.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request for login")
public class JwtRequest {

    @Schema(description = "Email", example = "johndoee@gmail.com")
    @NotNull(message = "Username must be not null")
    private String username;

    @Schema(description = "Password", example = "12345")
    @NotNull(message = "Password must be not null")
    private String password;

}
