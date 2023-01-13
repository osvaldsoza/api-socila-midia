package br.com.osvaldsoza.service;

import br.com.osvaldsoza.dto.CreateUserRequest;
import br.com.osvaldsoza.model.User;
import br.com.osvaldsoza.repository.UseRepository;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class UserService {

    private UseRepository useRepository;

    @Inject
    public UserService(UseRepository useRepository) {
        this.useRepository = useRepository;
    }

    public List<User> listUsers() {
        return useRepository.listAll(Sort.by("name"));
    }

    @Transactional
    public User createUser(CreateUserRequest userRequest) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setAge(userRequest.getAge());
        useRepository.persist(user);
        return user;
    }

    @Transactional
    public void updateUser(Long id, CreateUserRequest userRequest) {
        User user = useRepository.findById(id);
        if (user == null) {
            throw new NotFoundException();
        }
        user.setName(userRequest.getName());
        user.setAge(userRequest.getAge());
        useRepository.persist(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = useRepository.findById(id);
        if (user == null) {
            throw new NotFoundException();
        }
        useRepository.delete(user);
    }
}
