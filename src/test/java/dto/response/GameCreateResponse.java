package dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import dto.request.GameRequest;
import dto.common.Info;
import lombok.Data;

@Data
public class GameCreateResponse {
    @JsonProperty("register_data")
    private GameRequest register_data;
    private Info info;
}