package br.com.osvaldsoza.repository;

import br.com.osvaldsoza.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UseRepository implements PanacheRepository<User> {
}
