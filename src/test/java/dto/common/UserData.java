package dto.common;

import dto.request.GameRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    private int id;
    private String login;
    private String pass;
    private List<GameRequest> games;
}