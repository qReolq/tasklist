package qreol.project.tasklist.service;

import qreol.project.tasklist.web.dto.auth.JwtRequest;
import qreol.project.tasklist.web.dto.auth.JwtResponse;

public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);

}
