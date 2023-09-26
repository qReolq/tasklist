package qreol.project.tasklist.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Response for login")
public class JwtResponse {

    @Schema(description = "User id", example = "1")
    private Long id;

    @Schema(description = "User username", example = "johndoee@gmail.com")
    private String username;

    @Schema(description = "Access token", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huZG9lZUBnbWFpbOlsiUk9MRV9BRE1JT")
    private String accessToken;

    @Schema(description = "Refresh token", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huZG9lZUBnbWFpbOlsiUk9MRV9BRE1JT")
    private String refreshToken;
}
