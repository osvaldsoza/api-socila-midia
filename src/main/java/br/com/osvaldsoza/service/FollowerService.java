package br.com.osvaldsoza.service;

import br.com.osvaldsoza.dto.FollowerRequest;
import br.com.osvaldsoza.model.Follower;
import br.com.osvaldsoza.repository.FollowerRepository;
import br.com.osvaldsoza.repository.UseRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;

@ApplicationScoped
public class FollowerService {

    private FollowerRepository followerRepository;
    private UseRepository useRepository;

    @Inject
    public FollowerService(FollowerRepository followerRepository, UseRepository useRepository) {
        this.followerRepository = followerRepository;
        this.useRepository = useRepository;
    }

    @Transactional
    public void updateFollowerUser(Long userId, FollowerRequest followerRequest) {
        var user = useRepository.findById(userId);

        if (user == null) {
            throw new NotFoundException();
        }

        var follower = useRepository.findById(followerRequest.getFollowerId());

        var hasFollowers = followerRepository.hasFolowers(follower, user);
        if (!hasFollowers) {
            var entity = new Follower();
            entity.setUser(user);
            entity.setFollower(follower);
            followerRepository.persist(entity);
        }
    }
}
