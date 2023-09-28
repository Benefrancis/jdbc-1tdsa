package br.com.fiap;

import br.com.fiap.domain.entity.Cliente;
import br.com.fiap.domain.repository.ClienteRepository;
import br.com.fiap.infra.ConnectionFactory;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class Main {

    private static final String BASE_URI = "http://localhost/";

    public static HttpServer startServer() {
        // Configure container response filters (CORSFilter)
        // Configure ConnectionFactory
        // Configure os pacotes em que temos Recursos da API REST
        final ResourceConfig rc = new ResourceConfig()
                .register( ConnectionFactory.build() )
                .packages( "br.com.fiap.domain.resources" );
        return GrizzlyHttpServerFactory.createHttpServer( URI.create( BASE_URI ), rc );
    }

    public static void main(String[] args) {
        var server = startServer();
        System.out.println( String.format(
                "Cliente app started with endpoints available " +
                        "as %s%nHit Ctrl-C to stop it....", BASE_URI + "cliente" ) );
        try {
            System.in.read();
            server.stop();
        } catch (IOException e) {
            throw new RuntimeException( e );
        }
    }
}

