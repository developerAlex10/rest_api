package dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import dto.common.Info;
import dto.common.UserData;
import lombok.Data;

@Data
public class UserResponse {
    @JsonProperty("register_data")
    private UserData registerData;
    private Info info;
}
