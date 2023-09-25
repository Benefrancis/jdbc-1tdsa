package br.com.fiap.domain.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


public interface Resource<T, U> {


    /**
     * Find all itens
     *
     * @return
     */

    public Response findAll();

    /**
     * find itens by id
     *
     * @param id
     * @return
     */

    public Response findById(@PathParam("id") U id);

    /**
     * Persist
     *
     * @param t
     * @return
     */

    public Response persist(T t);

    /**
     * Update object
     *
     * @param id
     * @param t
     * @return
     */
    public Response update(U id, T t);

    /**
     * Delete resource
     * @param id
     * @return
     */
    public Response delete(U id);
}
