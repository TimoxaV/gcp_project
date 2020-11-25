package gcp.project.cloud.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    @NotNull(message = "Message must be provided")
    private MessageDto message;
    private String subscription;
}
