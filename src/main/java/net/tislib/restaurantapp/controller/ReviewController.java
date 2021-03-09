package net.tislib.restaurantapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.data.OwnerReplyResource;
import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.ReviewResource;
import net.tislib.restaurantapp.service.ReviewService;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static net.tislib.restaurantapp.constant.ApiConstants.API_REVIEWS;
import static net.tislib.restaurantapp.constant.ApiConstants.PATH_ID;
import static net.tislib.restaurantapp.constant.ApiConstants.PATH_OWNER_REPLY;

@RestController
@RequestMapping(API_REVIEWS)
@RequiredArgsConstructor
@Tag(name = "reviews", description = "endpoints related to user CRUD operations")
public class ReviewController {

    private final ReviewService service;

    @PostMapping
    @Operation(operationId = "reviewCreate", summary = "create review", description = "create a new review")
    public ReviewResource create(@PathVariable Long restaurantId,
                                 @RequestBody @Validated ReviewResource resource) {
        return service.create(restaurantId, resource);
    }

    @GetMapping
    @Operation(operationId = "reviewList", summary = "list reviews", description = "return list of all / filtered reviews")
    public PageContainer<ReviewResource> list(@PathVariable Long restaurantId,

                                              @Schema(description = "filter by rating")
                                              @RequestParam BigDecimal rating,

                                              @Schema(description = "page number")
                                              @RequestParam(required = false, defaultValue = "0") int page,

                                              @Schema(description = "page size")
                                              @RequestParam(required = false, defaultValue = "25") int pageSize) {
        return service.list(restaurantId, rating, PageRequest.of(page, pageSize));
    }

    @GetMapping(PATH_ID)
    @Operation(operationId = "reviewGetById", summary = "get review by id", description = "return single review by id")
    public ReviewResource get(@PathVariable Long restaurantId,
                              @PathVariable Long id) {
        return service.getClass(restaurantId, id);
    }

    @PutMapping(PATH_ID)
    @Operation(operationId = "reviewUpdate", summary = "update review", description = "update single review")
    public ReviewResource update(@PathVariable Long restaurantId,
                                 @PathVariable Long id,
                                 @RequestBody @Validated ReviewResource resource) {
        return service.update(restaurantId, id, resource);
    }

    @DeleteMapping(PATH_ID)
    @Operation(operationId = "reviewDelete", summary = "delete review", description = "delete single review by id")
    public ReviewResource delete(@PathVariable Long restaurantId,
                                 @PathVariable Long id) {
        return service.delete(restaurantId, id);
    }

    @PutMapping(PATH_OWNER_REPLY)
    @Operation(operationId = "ownerReply", summary = "reply to comment", description = "reply to comment or updated current reply")
    public ReviewResource ownerReply(@PathVariable Long restaurantId,
                                     @PathVariable Long id,
                                     @RequestBody @Validated OwnerReplyResource ownerReplyResource) {
        return service.updateOwnerReply(restaurantId, id, ownerReplyResource);
    }

}
