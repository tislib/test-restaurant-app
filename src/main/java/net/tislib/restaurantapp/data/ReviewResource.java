package net.tislib.restaurantapp.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReviewResource extends Resource<ReviewResource> {

    @Min(1)
    @Max(5)
    private short starCount;

    @NotBlank
    private Instant dateOfVisit;

    @NotBlank
    private String comment;

    @NotBlank
    private OwnerReplyResource ownerReply;

    private Instant createTime;

    @Override
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    public Links getLinks() {
        return super.getLinks();
    }
}
