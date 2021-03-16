package net.tislib.restaurantapp.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@Data
@EqualsAndHashCode(callSuper = false)
@FieldNameConstants
public class OwnerReplyResource extends Resource<OwnerReplyResource> {

    @NotBlank
    @Schema(description = "reply comment from restaurant owner")
    @Length(min = 10, max = 255)
    private String comment;
    @Schema(accessMode = READ_ONLY)
    private Instant replyTime;

}
