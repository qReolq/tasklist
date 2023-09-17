package qreol.project.tasklist.web.dto.auth;

import lombok.Data;

@Data
public class JwtResponse {
    private Long id;
    private String username;
    private String accessToken;
    protected String refreshToken;
}
