package net.tislib.restaurantapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.UserResource;
import net.tislib.restaurantapp.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.tislib.restaurantapp.constant.ApiConstants.API_USERS;
import static net.tislib.restaurantapp.constant.ApiConstants.PATH_ID;

@RestController
@RequestMapping(API_USERS)
@RequiredArgsConstructor
@Tag(name = "users", description = "endpoints related to user CRUD operations")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(operationId = "userCreate", summary = "create user", description = "create a new user")
    public UserResource create(@RequestBody @Validated UserResource userResource) {
        return null;
    }

    @GetMapping
    @Operation(operationId = "userList", summary = "list users", description = "return list of all users")
    public PageContainer<UserResource> list() {
        return userService.list();
    }

    @GetMapping(PATH_ID)
    @Operation(operationId = "userGetById", summary = "get user by id", description = "return single user by its id")
    public UserResource get(@PathVariable Long id) {
        return userService.get(id);
    }

    @PutMapping(PATH_ID)
    @Operation(operationId = "userUpdate", summary = "update user", description = "find user by id and replace it")
    public UserResource update(@PathVariable Long id,
                               @RequestBody @Validated UserResource userResource) {
        return userService.update(id, userResource);
    }

}
