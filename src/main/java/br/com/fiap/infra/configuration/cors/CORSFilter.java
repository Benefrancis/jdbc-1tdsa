package br.com.fiap.infra.configuration.cors;


import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;

import java.io.IOException;

public class CORSFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();

        // Allow requests from any origin
        headers.add( "Access-Control-Allow-Origin", "*" );

        // Allow specific HTTP methods
        headers.add( "Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD" );

        // Allow specific headers
        headers.add( "Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With" );

        // Allow credentials (cookies, authorization headers, etc.)
        headers.add( "Access-Control-Allow-Credentials", "true" );
    }
}
