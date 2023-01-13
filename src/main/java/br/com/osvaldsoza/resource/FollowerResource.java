package br.com.osvaldsoza.resource;

import br.com.osvaldsoza.dto.FollowerRequest;
import br.com.osvaldsoza.service.FollowerService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {


    private FollowerService followerService;

    @Inject
    public FollowerResource(FollowerService followerService) {
        this.followerService = followerService;
    }

    @PUT
    @Path("user/{userId}")
    @Transactional
    public Response updateFollowerUser(@PathParam("userId") Long userId, FollowerRequest followerRequest) {
        followerService.updateFollowerUser(userId, followerRequest);
        return Response.noContent().build();
    }
}
