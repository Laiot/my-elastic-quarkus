package org.acme.elasticsearch;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.jboss.resteasy.reactive.RestQuery;

@Path("/users")
public class UserResource {
    @Inject
    UserService userService;

    @POST
    public Response index(User user) throws IOException{
        if (user.id == null){
            user.id = UUID.randomUUID().toString();
        }

        userService.index(user);
        return Response.created(URI.create("/users/" + user.id)).build();
    }

    @GET
    @Path("/{id}")
    public User get(String id) throws IOException {
        return userService.get(id);
    }

    @GET
    @Path("/search")
    public List<User> search(@RestQuery String name, @RestQuery int age) throws IOException {
        if (name != null) {
            return userService.searchByName(name);
        } else if (age != -1) {
            return userService.searchByAge(age);
        } else {
            throw new BadRequestException("Should provide name or age query parameter");
        }
    }
}
