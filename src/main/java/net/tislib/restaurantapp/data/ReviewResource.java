package net.tislib.restaurantapp.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReviewResource extends Resource<ReviewResource> {

    @Schema(accessMode = READ_ONLY)
    private Long id;

    private UserResource user;

    @Min(1)
    @Max(5)
    @Schema(required = true, example = "3")
    private short starCount;

    @NotBlank
    @Schema(required = true, example = "sample comment")
    private String comment;

    @Schema(accessMode = READ_ONLY)
    private Instant reviewTime;

    @NotBlank
    @Schema(required = true)
    private LocalDate dateOfVisit;

    @NotBlank
    @Schema(accessMode = READ_ONLY)
    private OwnerReplyResource ownerReply;
}
