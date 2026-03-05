package dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Requirements {
    private int hardDrive;
    private String osName;
    private int ramGb;
    private String videoCard;
}
