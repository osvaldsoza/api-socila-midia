package br.com.osvaldsoza.resource;

import br.com.osvaldsoza.dto.CreatePostRequest;
import br.com.osvaldsoza.dto.ResponseError;
import br.com.osvaldsoza.model.Post;
import br.com.osvaldsoza.service.PostService;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

import static br.com.osvaldsoza.util.StatusCode.UNPROCESSABLE_ENTITY;

@Path("/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private PostService postService;
    private Validator validator;


    @Inject
    public PostResource(PostService postService, Validator validator) {
        this.postService = postService;
        this.validator = validator;
    }

    @GET
    public Response listPosts() {
        List<Post> posts = postService.listPosts();
        return Response.ok(posts).build();
    }

    @GET
    @Path("user/{userId}")
    public Response listPosts(@PathParam("userId") Long userId) {
        List<Post> posts = postService.listPostsbyUser(userId);
        return Response.ok(posts).build();
    }

    @POST
    @Path("user/{userId}")
    public Response savePosts(@PathParam("userId") Long userId, CreatePostRequest postRequest) {
        Set<ConstraintViolation<CreatePostRequest>> violations = validator.validate(postRequest);
        if (!violations.isEmpty()) {
            return ResponseError.createFromValidation(violations)
                    .withStatusCode(UNPROCESSABLE_ENTITY.getStatusCode());
        }
        var post = postService.savePosts(userId, postRequest);
        return Response.status(Response.Status.CREATED).entity(post).build();
    }

    @PUT
    @Path("{postId}")
    public Response updatePosts(@PathParam("postId") Long postId, CreatePostRequest postRequest) {
        Set<ConstraintViolation<CreatePostRequest>> violations = validator.validate(postRequest);
        if (!violations.isEmpty()) {
            return ResponseError.createFromValidation(violations)
                    .withStatusCode(UNPROCESSABLE_ENTITY.getStatusCode());
        }
        postService.updatePosts(postId, postRequest);
        return Response.ok().build();
    }

    @DELETE
    @Path("{postId}")
    public Response deletePosts(@PathParam("postId") Long postId) {
        postService.deletePosts(postId);
        return Response.noContent().build();
    }
}
