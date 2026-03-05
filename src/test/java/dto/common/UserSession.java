package dto.common;

import api.ApiClient;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSession {
    private ApiClient apiClient;
    private String token;
    private String login;
    private String password;
}
