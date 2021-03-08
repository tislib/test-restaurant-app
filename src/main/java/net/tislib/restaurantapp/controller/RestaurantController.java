package net.tislib.restaurantapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.RestaurantResource;
import org.springframework.validation.annotation.Validated;
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

    @PostMapping
    @Operation(operationId = "restaurantCreate", summary = "create restaurant", description = "create a new restaurant")
    public RestaurantResource create(@RequestBody @Validated RestaurantResource userResource) {
        return null;
    }

    @GetMapping
    @Operation(operationId = "restaurantList", summary = "list restaurants", description = "return list of all / filtered restaurants")
    public PageContainer<RestaurantResource> list(@Schema(description = "filter by rating option, ratings filter are rounded to integer") @RequestParam BigDecimal rating) {
        return null;
    }

    @GetMapping(PATH_ID)
    @Operation(operationId = "restaurantGetById", summary = "get restaurant by id", description = "return single restaurant by id")
    public RestaurantResource get(@PathVariable Long id) {
        return null;
    }

    @PutMapping(PATH_ID)
    @Operation(operationId = "restaurantUpdate", summary = "update restaurant", description = "update single restaurant")
    public RestaurantResource update(@PathVariable Long id,
                                     @RequestBody @Validated RestaurantResource userResource) {
        return null;
    }

}
