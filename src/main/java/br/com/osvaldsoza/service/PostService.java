package br.com.osvaldsoza.service;

import br.com.osvaldsoza.dto.CreatePostRequest;
import br.com.osvaldsoza.model.Post;
import br.com.osvaldsoza.model.User;
import br.com.osvaldsoza.repository.PostRepository;
import br.com.osvaldsoza.repository.UseRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class PostService {

    private PostRepository postRepository;
    private UseRepository useRepository;

    @Inject
    public PostService(PostRepository postRepository, UseRepository useRepository) {
        this.postRepository = postRepository;
        this.useRepository = useRepository;
    }

    public List<Post> listPosts() {
        return postRepository.findAll().stream().toList();
    }

    public List<Post> listPostsbyUser(Long userId) {
        User user = useRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException();
        }
        return postRepository.find("user_id = ?1", user.getId()).stream().toList();
    }

    @Transactional
    public Post savePosts(Long userId, CreatePostRequest createPostRequest) {
        User user = useRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException();
        }
        var post = convertFromPost(user, createPostRequest);
        postRepository.persist(post);
        return post;
    }

    @Transactional
    public void updatePosts(Long postId, CreatePostRequest createPostRequest) {
        var post = postRepository.findById(postId);
        if (post == null) {
            throw new NotFoundException();
        }
        post.setText(createPostRequest.getText());
        postRepository.persist(post);
    }

    @Transactional
    public void deletePosts(Long postId) {
        var post = postRepository.findById(postId);
        if(post == null){
            throw new NotFoundException();
        }
        postRepository.delete(post);
    }

    private Post convertFromPost(User user, CreatePostRequest createPostRequest) {
        Post post = new Post();
        post.setUser(user);
        post.setText(createPostRequest.getText());
        return post;
    }
}
