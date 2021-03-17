package net.tislib.restaurantapp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.tislib.restaurantapp.controller.UserController;
import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.UserResource;
import net.tislib.restaurantapp.data.mapper.UserMapper;
import net.tislib.restaurantapp.exception.InvalidFieldException;
import net.tislib.restaurantapp.model.User;
import net.tislib.restaurantapp.model.repository.UserRepository;
import net.tislib.restaurantapp.service.AuthenticationService;
import net.tislib.restaurantapp.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;

import java.util.Objects;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AuthenticationService authenticationService;

    @Override
    public UserResource create(UserResource resource) {
        log.trace("create user request: {}", resource);

        /*
            validate user request data, most of validations is done by annotation based validation rules
            validation method is used for handling complex validation which
         */
        validateUser(resource, null);

        User entity = mapper.from(resource);

        entity.setPassword(passwordEncoder.encode(resource.getPassword()));
        entity.setId(null);

        repository.save(entity);

        log.info("user created with details: {}", entity);

        return get(entity.getId());
    }

    @Override
    public PageContainer<UserResource> list(Pageable pageable) {
        log.trace("list user request: {}", pageable);

        return mapper.mapPage(repository.findAll(pageable))
                .map(item -> item.add(linkTo(methodOn(UserController.class)
                        .get(item.getId()))
                        .withSelfRel()));
    }

    @Override
    public UserResource get(Long id) {
        log.trace("get user by id: {}", id);

        User entity = getEntity(id);

        log.info("user found with details: {}", entity);

        return mapper.to(entity)
                .add(linkTo(methodOn(UserController.class)
                        .get(entity.getId()))
                        .withSelfRel());
    }

    @Override
    public UserResource update(Long id, UserResource resource) {
        log.trace("update user by id: {}, resource: {}", id, resource);

        User entity = getEntity(id);

        validateUser(resource, entity);

        resource.setId(id);

        mapper.mapFrom(entity, resource);

        // change password if password changed
        if (!StringUtils.isEmpty(resource.getPassword())) {
            entity.setPassword(passwordEncoder.encode(resource.getPassword()));
        }

        repository.save(entity);

        log.info("user updated with details: {}", entity);

        return get(resource.getId());
    }

    @Override
    public void delete(Long id) {
        log.trace("delete user by id: {}", id);

        User entity = getEntity(id);

        if (Objects.equals(entity.getId(), authenticationService.getCurrentUser().getId())) {
            throw new IllegalArgumentException("you cannot remove own user");
        }

        repository.delete(entity);

        log.info("user deleted with details: {}", entity);
    }

    private void validateUser(UserResource resource, User existingEntity) {
        Optional<User> userOptional = repository.findByEmail(resource.getEmail());

        if (userOptional.isEmpty()) {
            return;
        }

        // check if email is already exists and used by different user
        if (!Objects.equals(userOptional.get().getId(), existingEntity.getId())) {
            throw new InvalidFieldException("email", "this email is already used by another user");
        }
    }

    private User getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user not found with id: " + id));
    }
}
