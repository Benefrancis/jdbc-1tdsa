package br.com.fiap;

import br.com.fiap.infra.configuration.cors.Configuration;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.io.IOException;
import java.net.URI;

public class Main {

    public static final String BASE_URI = "http://localhost/";

    public static HttpServer startServer() {
        return GrizzlyHttpServerFactory.createHttpServer( URI.create( BASE_URI ), new Configuration() );
    }

    public static void main(String[] args) {

        final HttpServer server = startServer();

        System.out.println( String.format( "Cliente app started with endpoints available as %s%nHit Ctrl-C to stop it....", BASE_URI + "cliente" ) );

        try {
            System.in.read();
            server.shutdownNow();
        } catch (IOException e) {
            throw new RuntimeException( e );
        }
    }
}
