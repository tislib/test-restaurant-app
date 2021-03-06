package net.tislib.restaurantapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.UserResource;
import net.tislib.restaurantapp.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static net.tislib.restaurantapp.constant.ApiConstants.API_USERS;
import static net.tislib.restaurantapp.constant.ApiConstants.PATH_ID;

@RestController
@RequestMapping(API_USERS)
@RequiredArgsConstructor
@Tag(name = "users", description = "endpoints related to user CRUD operations")
public class UserController {

    private final UserService service;

    @PostMapping
    @Operation(operationId = "userCreate", summary = "create user", description = "create a new user")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResource create(@RequestBody @Validated UserResource resource) {
        return service.create(resource);
    }

    @GetMapping
    @Operation(operationId = "userList", summary = "list users", description = "return list of all users")
    public PageContainer<UserResource> list(@Schema(description = "page number")
                                            @RequestParam(required = false, defaultValue = "0") int page,

                                            @Schema(description = "page size")
                                            @RequestParam(required = false, defaultValue = "25") int pageSize) {
        return service.list(PageRequest.of(page, pageSize, Sort.by(Sort.Order.asc("id"))));
    }

    @GetMapping(PATH_ID)
    @Operation(operationId = "userGetById", summary = "get user by id", description = "return single user by its id")
    public UserResource get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping(PATH_ID)
    @Operation(operationId = "userUpdate", summary = "update user", description = "find user by id and replace it")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public UserResource update(@PathVariable Long id,
                               @RequestBody @Validated UserResource userResource) {
        return service.update(id, userResource);
    }

    @DeleteMapping(PATH_ID)
    @Operation(operationId = "userDelete", summary = "delete user", description = "delete single user by id")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
