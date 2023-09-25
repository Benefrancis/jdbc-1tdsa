package br.com.fiap.domain.resources;

import br.com.fiap.domain.entity.Cliente;
import br.com.fiap.domain.service.ClienteService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Path("cliente")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteResource implements Resource<Cliente, Long> {

    @Context
    UriInfo uriInfo;

    private ClienteService service = new ClienteService();

    @GET
    @Override
    public Response findAll() {
        List<Cliente> all = service.findAll();

        return Response
                .status(Response.Status.OK)
                .entity(all)
                .build();
    }


    @GET
    @Path("/{id}")
    @Override
    public Response findById(@PathParam("id") Long id) {
        Cliente c = service.findById(id);

        if (Objects.isNull(c)) return Response.status(404).build();

        return Response
                .status(Response.Status.OK)
                .entity(c)
                .build();
    }

    @POST
    @Override
    public Response persist(Cliente cliente) {
        Cliente persist = service.persist(cliente);

        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        URI uri = ub.path(String.valueOf(persist.getId())).build();

        return Response
                .created(uri)
                .entity(persist)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Override
    public Response update(@PathParam("id") Long id, Cliente cliente) {
        cliente.setId(id);
        Cliente c = service.update(cliente);
        if (Objects.isNull(c)) return Response.status(404).build();
        return Response
                .ok()
                .entity(c)
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Override
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = service.delete(id);
        if (deleted) return Response.ok().build();
        return Response.status(404).build();
    }

}
