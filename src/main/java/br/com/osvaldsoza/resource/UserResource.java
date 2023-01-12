package br.com.osvaldsoza.resource;

import br.com.osvaldsoza.dto.CreateUserRequest;
import br.com.osvaldsoza.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserService userService;

    @GET
    public Response listUsers() {
        var users = userService.listUsers();
        return Response.ok(users).build();
    }

    @POST
    public Response createUser(CreateUserRequest userRequest) {
        var user = userService.createUser(userRequest);
        return Response.ok(user).build();
    }

    @PUT
    @Path("{id}")
    public Response updateUser(@PathParam("id") Long id, CreateUserRequest userRequest) {
        var user = userService.updateUser(id, userRequest);
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
