package net.tislib.restaurantapp.service.impl;

import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.UserResource;
import net.tislib.restaurantapp.data.mapper.UserMapper;
import net.tislib.restaurantapp.model.User;
import net.tislib.restaurantapp.model.repository.UserRepository;
import net.tislib.restaurantapp.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserResource create(UserResource resource) {
        User entity = mapper.from(resource);

        entity.setPassword(passwordEncoder.encode(resource.getPassword()));
        entity.setId(null);
        repository.save(entity);

        return get(entity.getId());
    }

    @Override
    public PageContainer<UserResource> list(Pageable pageable) {
        return mapper.mapPage(repository.findAll(pageable));
    }

    @Override
    public UserResource get(Long id) {
        User entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user not found with id: " + id));

        return mapper.to(entity);
    }

    @Override
    public UserResource update(Long id, UserResource resource) {
        User existingEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user not found with id: " + id));

        resource.setId(id);
        String previousPassword = existingEntity.getPassword();
        mapper.mapFrom(existingEntity, resource);

        // change password if password changed
        if (!StringUtils.isEmpty(resource.getPassword())) {
            existingEntity.setPassword(passwordEncoder.encode(resource.getPassword()));
        } else {
            // reset password back if no change needed
            existingEntity.setPassword(previousPassword);
        }

        repository.save(existingEntity);

        return get(resource.getId());
    }

    @Override
    public void delete(Long id) {
        User existingEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user not found with id: " + id));

        repository.delete(existingEntity);
    }
}
