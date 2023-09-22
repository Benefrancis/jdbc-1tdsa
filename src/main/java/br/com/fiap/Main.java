package br.com.fiap;

import br.com.fiap.domain.entity.Cliente;
import br.com.fiap.domain.repository.ClienteRepository;
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
    public static Connection getConnection() {
        Properties prop = new Properties();
        FileInputStream file = null;
        String url = null;
        String pass = null;
        String user = null;
        String driver = null;
        String debugar = null;
        try {

            file = new FileInputStream( "src/main/resources/application.properties" );
            prop.load( file );

            url = prop.getProperty( "datasource.url" );
            user = prop.getProperty( "datasource.username" );
            pass = prop.getProperty( "datasource.password" );
            driver = prop.getProperty( "datasource.driver-class-name" );
            debugar = prop.getProperty( "datasource.debugar" );
            file.close();

            return DriverManager.getConnection( url, user, pass );

        } catch (FileNotFoundException e) {
            System.err.println( "Não encontramos o arquivo de configuração: " + e.getMessage() );
        } catch (IOException e) {
            System.err.println( "Não foi possível ler o arquivo de configuração: " + e.getMessage() );
        } catch (SQLException e) {
            System.err.println( "Não foi possível realizar a conexão com o banco de dados: " + e.getMessage() );
        }
        return null;
    }

    private static final String BASE_URI = "http://localhost/";

    public static HttpServer startServer() {
        // Configure container response filters (CORSFilter)
        // Configure ConnectionFactory
        // Configure os pacotes em que temos Recursos da API REST
        final ResourceConfig rc = new ResourceConfig()
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

