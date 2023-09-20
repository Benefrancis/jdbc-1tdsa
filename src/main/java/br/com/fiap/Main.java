package br.com.fiap;

import br.com.fiap.infra.ConnectionFactory;
import br.com.fiap.infra.configuration.cors.CORSFilter;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class Main {

    public static final String BASE_URI = "http://localhost/";

    public static HttpServer startServer() {

        final ResourceConfig rc = new ResourceConfig()
                // Configure container response filters (CORSFilter)
                .register( CORSFilter.class )
                // Configure ConnectionFactory
                .register( ConnectionFactory.class )
                // Configure os pacotes em que temos Recursos da API REST
                .packages( "br.com.fiap.domain.resources" );
        return GrizzlyHttpServerFactory.createHttpServer( URI.create( BASE_URI ), rc );
    }

    public static void main(String[] args) {

        final HttpServer server = startServer();

        System.out.println( String.format( "Cliente app started with endpoints available as %s%nHit Ctrl-C to stop it....", BASE_URI + "cliente" ) );

        try {
            System.in.read();
            server.stop();
        } catch (IOException e) {
            throw new RuntimeException( e );
        }
    }

}

