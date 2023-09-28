package br.com.fiap.domain.resources;

import br.com.fiap.domain.entity.Cliente;
import br.com.fiap.domain.repository.ClienteRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Path("/cliente")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteResource {

    @Context
    UriInfo uriInfo;

    private ClienteRepository repo =  ClienteRepository.build();

    @GET
    public Response findAll() {
        List<Cliente> clientes = new ArrayList<>();
        clientes = repo.findAll();
        return Response.ok(clientes).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        Cliente cliente = repo.findById(id);
        if (Objects.isNull(cliente)) return Response.status(404).build();
        return Response.ok(cliente).build();
    }

    @POST
    public Response persit(Cliente c) {
        Cliente cliente = repo.persist(c);
        //Criando a URI da requisição
        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        URI uri = ub.path(String.valueOf(cliente.getId())).build();
        return Response.created(uri).entity(cliente).build();
    }

}
