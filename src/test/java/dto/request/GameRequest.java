package dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import dto.common.Dlc;
import dto.common.Requirements;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameRequest {
    private String company;
    private String description;
    private List<Dlc> dlcs;
    private int gameId;
    private String genre;
    @JsonProperty("isFree")
    private boolean free;
    private double price;
    private String publish_date;
    private int rating;
    private boolean requiredAge;
    private Requirements requirements;
    private List<String> tags;
    private String title;
}
