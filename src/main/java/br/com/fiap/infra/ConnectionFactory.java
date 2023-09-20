package br.com.fiap.infra;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <strong>Fábrica de conexões com o banco de dados</strong>
 * <br />
 * Implementação usando java.util.concurrent.atomic.AtomicReference pode ser considerada mais moderna e preferível por alguns motivos:
 * <br />
 * <strong>AtomicReference para thread safety:</strong> A versão usando AtomicReference é mais explícita sobre a garantia de thread safety. A classe AtomicReference fornece operações atômicas, o que ajuda a evitar a necessidade de sincronização manual usando blocos synchronized.
 * <br />
 * <strong>Performance:</strong> A implementação com AtomicReference é geralmente mais eficiente em termos de desempenho do que a sincronização manual usando blocos synchronized, especialmente em cenários de concorrência moderada a alta. A inicialização dupla reduz a sobrecarga de sincronização.
 * <br />
 * <strong>Clareza do código:</strong> A implementação usando AtomicReference é mais concisa e mais clara, destacando a intenção de usar um objeto atomicamente atualizado para garantir a inicialização segura.
 * <br /><br />
 * Portanto, em um contexto moderno e considerando as práticas atuais de programação em Java, a implementação usando AtomicReference é muitas vezes preferível para garantir a eficiência e a clareza do código em ambientes multithread.
 * <br /><br />
 */
public class ConnectionFactory {

    /**
     * Variável que <strong>armazena a única instância disponível da Classe</strong>
     */

    private static final AtomicReference<ConnectionFactory> instance = new AtomicReference<>();

    /**
     * Construtor privado.
     * <strong>Impedir a instânciação por outras classes.</strong>
     */
    private ConnectionFactory() {
    }


    /**
     * <strong>Método responsável pelo fornecimento da única instância da Classe ConnectionFactory </strong>
     * <br /><br />
     * Nessa implementação, a instância da classe ConnectionFactory é armazenada em um AtomicReference.
     * <br />
     * O método of() primeiro tenta obter a instância atual a partir do AtomicReference.
     * Se a instância ainda não foi inicializada, cria-se uma nova instância e utiliza-se compareAndSet() para garantir que apenas uma instância seja criada e armazenada no AtomicReference.
     * <br />
     * Essa implementação é thread-safe e eficiente, garantindo que apenas uma instância da ConnectionFactory seja criada, mesmo em um ambiente multithread.
     * <br />
     *
     * @return
     */
    public static ConnectionFactory of() {
        ConnectionFactory result = instance.get();
        if (Objects.isNull( result )) {
            ConnectionFactory factory = new ConnectionFactory();
            if (instance.compareAndSet( null, factory )) {
                result = factory;
            } else {
                result = instance.get();
            }
        }
        return result;
    }

    /**
     * A classe ConnectionFactory existe para fabricar Connection
     *
     * @return
     */
    public  Connection getConnection() {


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

            if (Objects.isNull( driver ) || driver.equals( "" )) {
                System.out.println( "\nInforme os dados de conexão no arquivo application.properties [ datasource.driver-class-name ]" );
                throw new RuntimeException( "Informe os dados de conexão no arquivo application.properties [ datasource.driver-class-name ]" );
            }

            if (Objects.isNull( url ) || url.equals( "" )) {
                System.out.println( "\nInforme os dados de conexão no arquivo application.properties [ datasource.url ]" );
                throw new RuntimeException( "Informe os dados de conexão no arquivo application.properties [ datasource.url ]" );
            }

            if (Objects.isNull( user ) || user.equals( "" )) {
                System.out.println( "\nInforme os dados de conexão no arquivo application.properties [ datasource.username ]" );
                throw new RuntimeException( "Informe os dados de conexão no arquivo application.properties [ datasource.username ]" );
            }


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


}
