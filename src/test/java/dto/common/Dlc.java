package dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dlc {
    private String description;
    private String dlcName;
    @JsonProperty("isDlcFree")
    private boolean dlcFree;
    private double price;
    private int rating;
    private SimilarDlc similarDlc;
}
