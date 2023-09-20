package br.com.fiap.domain.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


public interface Resource<T, U> {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") U id);

    @POST
    public Response persist(T t);
}
