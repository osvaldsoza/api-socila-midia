package br.com.osvaldsoza.resource;

import br.com.osvaldsoza.dto.CreateUserRequest;
import br.com.osvaldsoza.dto.ResponseError;
import br.com.osvaldsoza.dto.UpdateUserRequest;
import br.com.osvaldsoza.service.UserService;
import br.com.osvaldsoza.util.StatusCode;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

import static br.com.osvaldsoza.util.StatusCode.UNPROCESSABLE_ENTITY;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private UserService userService;
    private Validator validator;
    private Set<ConstraintViolation<CreateUserRequest>> violations;

    @Inject
    public UserResource(UserService userService, Validator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    @GET
    public Response listUsers() {
        var users = userService.listUsers();
        return Response.ok(users).build();
    }

    @POST
    public Response createUser(CreateUserRequest userRequest) {
        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(userRequest);
        if (!violations.isEmpty()) {
            return ResponseError.createFromValidation(violations)
                    .withStatusCode(UNPROCESSABLE_ENTITY.getStatusCode());
        }
        var user = userService.createUser(userRequest);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @PUT
    @Path("{id}")
    public Response updateUser(@PathParam("id") Long id, UpdateUserRequest userRequest) {
        userRequest.setId(id);
        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(userRequest);
        if (!violations.isEmpty()) {
            return ResponseError.createFromValidation(violations)
                    .withStatusCode(UNPROCESSABLE_ENTITY.getStatusCode());
        }
        var user = userService.updateUser(userRequest);
        if (user != null) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        var userDeleted = userService.deleteUser(id);
        if (userDeleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
