package br.com.osvaldsoza.repository;

import br.com.osvaldsoza.model.Follower;
import br.com.osvaldsoza.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

    public boolean hasFolowers(User follower, User user) {
        var params = Parameters.with("follower", follower).and("user", user).map();
        var query = find("follower = :follower and user = :user", params);
        var followerResult = query.firstResultOptional();
        return followerResult.isPresent();
    }
}
