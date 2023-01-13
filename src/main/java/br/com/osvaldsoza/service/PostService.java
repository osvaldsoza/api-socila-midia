package br.com.osvaldsoza.service;

import br.com.osvaldsoza.dto.CreatePostRequest;
import br.com.osvaldsoza.dto.PostResponse;
import br.com.osvaldsoza.model.Post;
import br.com.osvaldsoza.model.User;
import br.com.osvaldsoza.repository.PostRepository;
import br.com.osvaldsoza.repository.UseRepository;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PostService {

    private PostRepository postRepository;
    private UseRepository useRepository;

    @Inject
    public PostService(PostRepository postRepository, UseRepository useRepository) {
        this.postRepository = postRepository;
        this.useRepository = useRepository;
    }

    public List<PostResponse> listPosts() {
        return postRepository.findAll(Sort.by("dateTime", Sort.Direction.Descending))
                .stream()
                .map(PostResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PostResponse> listPostsbyUser(Long userId) {
        User user = useRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException();
        }
        var postResponseList = postRepository.find("user",
                        Sort.by("dateTime", Sort.Direction.Descending), user)
                .list()
                .stream()
                .map(post -> PostResponse.fromEntity(post))
                .collect(Collectors.toList());
        return postResponseList;
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
