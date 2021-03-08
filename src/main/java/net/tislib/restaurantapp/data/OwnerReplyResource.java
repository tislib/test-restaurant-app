package net.tislib.restaurantapp.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = false)
public class OwnerReplyResource extends Resource<OwnerReplyResource> {

    @NotBlank
    @Schema(description = "reply comment from restaurant owner")
    private String comment;

    private Instant replyTime;

}
