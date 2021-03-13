package net.tislib.restaurantapp.service.impl;

import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.controller.UserController;
import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.UserResource;
import net.tislib.restaurantapp.data.mapper.UserMapper;
import net.tislib.restaurantapp.exception.InvalidFieldException;
import net.tislib.restaurantapp.model.User;
import net.tislib.restaurantapp.model.UserRole;
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
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AuthenticationService authenticationService;

    @Override
    public UserResource create(UserResource resource) {
        validateUser(resource, null);

        User entity = mapper.from(resource);

        entity.setPassword(passwordEncoder.encode(resource.getPassword()));
        entity.setId(null);
        entity.setRole(UserRole.REGULAR);

        repository.save(entity);

        return get(entity.getId());
    }

    @Override
    public PageContainer<UserResource> list(Pageable pageable) {
        return mapper.mapPage(repository.findAll(pageable))
                .map(item -> item.add(linkTo(methodOn(UserController.class)
                        .get(item.getId()))
                        .withSelfRel()));
    }

    @Override
    public UserResource get(Long id) {
        User entity = getEntity(id);

        return mapper.to(entity)
                .add(linkTo(methodOn(UserController.class)
                        .get(entity.getId()))
                        .withSelfRel());
    }

    private User getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user not found with id: " + id));
    }

    @Override
    public UserResource update(Long id, UserResource resource) {
        User existingEntity = getEntity(id);

        validateUser(resource, existingEntity);

        resource.setId(id);

        mapper.mapFrom(existingEntity, resource);

        // change password if password changed
        if (!StringUtils.isEmpty(resource.getPassword())) {
            existingEntity.setPassword(passwordEncoder.encode(resource.getPassword()));
        } else {
            // reset password back if no change needed
            existingEntity.setPassword(existingEntity.getPassword());
        }

        repository.save(existingEntity);

        return get(resource.getId());
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

    @Override
    public void delete(Long id) {
        User existingEntity = getEntity(id);

        if (Objects.equals(existingEntity.getId(), authenticationService.getCurrentUser().getId())) {
            throw new IllegalArgumentException("you cannot remove own user");
        }

        repository.delete(existingEntity);
    }
}
