package net.tislib.restaurantapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.RestaurantResource;
import net.tislib.restaurantapp.service.RestaurantService;
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

import static net.tislib.restaurantapp.constant.ApiConstants.API_RESTAURANTS;
import static net.tislib.restaurantapp.constant.ApiConstants.PATH_ID;

@RestController
@RequestMapping(API_RESTAURANTS)
@RequiredArgsConstructor
@Tag(name = "restaurants", description = "endpoints related to restaurant CRUD operations")
public class RestaurantController {

    private final RestaurantService service;

    @PostMapping
    @Operation(operationId = "restaurantCreate", summary = "create restaurant", description = "create a new restaurant")
    public RestaurantResource create(@RequestBody @Validated RestaurantResource resource) {
        return service.create(resource);
    }

    @GetMapping
    @Operation(operationId = "restaurantList", summary = "list restaurants", description = "return list of all / filtered restaurants")
    public PageContainer<RestaurantResource> list(@Schema(description = "filter by rating option, ratings filter are rounded to integer")
                                                  @RequestParam(required = false) BigDecimal rating,

                                                  @Schema(description = "filter by restaurant owner")
                                                  @RequestParam(required = false) Long ownerId,

                                                  @Schema(description = "page number")
                                                  @RequestParam(required = false, defaultValue = "0") int page,

                                                  @Schema(description = "page size")
                                                  @RequestParam(required = false, defaultValue = "25") int pageSize) {
        return service.list(rating, ownerId, PageRequest.of(page, pageSize));
    }

    @GetMapping(PATH_ID)
    @Operation(operationId = "restaurantGetById", summary = "get restaurant by id", description = "return single restaurant by id")
    public RestaurantResource get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping(PATH_ID)
    @Operation(operationId = "restaurantUpdate", summary = "update restaurant", description = "update single restaurant")
    public RestaurantResource update(@PathVariable Long id,
                                     @RequestBody @Validated RestaurantResource resource) {
        return service.update(id, resource);
    }

    @DeleteMapping(PATH_ID)
    @Operation(operationId = "restaurantDelete", summary = "delete restaurant", description = "delete singler restaurant by id")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
