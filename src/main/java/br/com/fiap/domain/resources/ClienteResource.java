package br.com.fiap.domain.resources;

import br.com.fiap.domain.entity.Cliente;
import br.com.fiap.domain.service.ClienteService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Path("/cliente")
public class ClienteResource implements Resource<Cliente, Long> {

    @Context
    UriInfo uriInfo;

    private ClienteService service = new ClienteService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response findAll() {
        List<Cliente> all = service.findAll();

        return Response
                .status( Response.Status.OK )
                //.entity( all.stream().map( ClienteDTO::of ).toList() )
                .entity( all )
                .build();
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response findById(@PathParam("id") Long id) {
        Cliente c = service.findById( id );

        if (Objects.isNull( c )) return Response.status( 404 ).build();

        return Response
                .status( Response.Status.OK )
                .entity( c )
                .build();
    }

    @POST
    @Override
    public Response persist(Cliente cliente) {
        Cliente persist = service.persist( cliente );

        UriBuilder ub = uriInfo.getAbsolutePathBuilder();
        URI uri = ub.path( String.valueOf( persist.getId() ) ).build();

        return Response
                .created( uri )
                .entity( persist )
                .build();
    }
}
