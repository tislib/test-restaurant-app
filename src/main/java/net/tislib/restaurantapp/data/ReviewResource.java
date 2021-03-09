package net.tislib.restaurantapp.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReviewResource extends Resource<ReviewResource> {

    @Schema(accessMode = READ_ONLY)
    private Long id;

    @Schema(accessMode = READ_ONLY)
    private UserResource user;

    @Min(1)
    @Max(5)
    @Schema(required = true, example = "3")
    private short starCount;

    @NotBlank
    @Schema(required = true, example = "sample comment")
    @Length(min = 10, max = 255)
    private String comment;

    @Schema(accessMode = READ_ONLY)
    private Instant reviewTime;

    @NotNull
    @Schema(required = true)
    private LocalDate dateOfVisit;

    @Schema(accessMode = READ_ONLY)
    private OwnerReplyResource ownerReply;

}
