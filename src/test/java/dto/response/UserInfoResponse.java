package dto.response;

import dto.common.GameInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private int id;
    private String login;
    private String pass;
    private List<GameInfo> games;
}
