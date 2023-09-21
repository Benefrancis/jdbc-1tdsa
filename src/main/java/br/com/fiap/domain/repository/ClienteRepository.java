package br.com.fiap.domain.repository;

import br.com.fiap.domain.entity.Cliente;
import br.com.fiap.infra.ConnectionFactory;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <strong>Repositório de Clientes</strong>
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
public class ClienteRepository implements Repository<Cliente, Long> {

    /**
     * Variável que <strong>armazena a única instância disponível da Classe</strong>
     */
    private static final AtomicReference<ClienteRepository> instance = new AtomicReference<>();


    /**
     * Construtor privado.
     * <strong>Impedir a instânciação por outras classes.</strong>
     */
    private ClienteRepository() {
    }

    /**
     * <strong>Método responsável pelo fornecimento da única instância da Classe ClienteRepository </strong>
     * <br /><br />
     * Nessa implementação, a instância da classe ClienteRepository é armazenada em um AtomicReference.
     * <br />
     * O método of() primeiro tenta obter a instância atual a partir do AtomicReference.
     * Se a instância ainda não foi inicializada, cria-se uma nova instância e utiliza-se compareAndSet() para garantir que apenas uma instância seja criada e armazenada no AtomicReference.
     * <br />
     * Essa implementação é thread-safe e eficiente, garantindo que apenas uma instância da ClienteRepository seja criada, mesmo em um ambiente multithread.
     * <br />
     *
     * @return ClienteRepository
     */
    public static ClienteRepository of() {
        ClienteRepository result = instance.get();
        if (Objects.isNull( result )) {
            ClienteRepository factory = new ClienteRepository();
            if (instance.compareAndSet( null, factory )) {
                result = factory;
            } else {
                result = instance.get();
            }
        }
        return result;
    }


    @Override
    public List<Cliente> findAll() {
        //Vector que será abastecido com clientes
        Vector<Cliente> clientes = new Vector<>();

        try {
            //Criando a fábrica de conexão
            ConnectionFactory factory = ConnectionFactory.of();
            //Fabricando a conexão
            Connection connection = factory.getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery( "SELECT * FROM cliente" );

            //Encontramos dados?
            if (resultSet.isBeforeFirst()) {
                //Navegue até o próximo
                while (resultSet.next()) {
                    //Pego os dados do Cliente
                    Long id = resultSet.getLong( "ID_CLIENTE" );
                    String nome = resultSet.getString( "NM_CLIENTE" );
                    //Crio uma instância
                    Cliente cliente = new Cliente( id, nome );
                    //Adiciono na coleção
                    clientes.add( cliente );

                    System.out.println( cliente );
                }
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println( "Não foi possivel consultar os dados!\n" + e.getMessage() );
        }
        return clientes;
    }

    @Override
    public Cliente findById(Long id) {

        var sql = "SELECT * FROM cliente where ID_CLIENTE=?";

        ConnectionFactory factory = ConnectionFactory.of();
        Connection connection = factory.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement( sql );
            preparedStatement.setLong( 1, id );

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    //Pego os dados do Cliente
                    Long idCliente = resultSet.getLong( "ID_CLIENTE" );
                    String nome = resultSet.getString( "NM_CLIENTE" );
                    //Crio uma instância
                    Cliente cliente = new Cliente( idCliente, nome );
                    System.out.println( cliente );
                    return cliente;
                }
            } else {
                System.out.println( "Cliente não encontrado com o id = " + id );
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println( "Não foi possível executar a consulta: \n" + e.getMessage() );
        }
        return null;
    }

    @Override
    public List<Cliente> findByName(String texto) {

        //Vector que será abastecido com clientes
        Vector<Cliente> clientes = new Vector<>();

        var sql = "SELECT * FROM cliente where UPPER(NM_CLIENTE) like ?";

        ConnectionFactory factory = ConnectionFactory.of();
        Connection connection = factory.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement( sql );

            //Transformando o texto em maiúsculo
            String t = Objects.nonNull( texto ) ? texto.toUpperCase() : "";

            preparedStatement.setString( 1, "%" + t + "%" );

            ResultSet resultSet = preparedStatement.executeQuery();

            //Encontramos dados?
            if (resultSet.isBeforeFirst()) {
                //Navegue até o próximo
                while (resultSet.next()) {
                    //Pego os dados do Cliente
                    Long id = resultSet.getLong( "ID_CLIENTE" );
                    String nome = resultSet.getString( "NM_CLIENTE" );
                    //Crio uma instância
                    Cliente cliente = new Cliente( id, nome );
                    //Adiciono na coleção
                    clientes.add( cliente );
                    System.out.println( cliente );
                }
            } else {
                System.out.println( "Cliente não encontrado com o nome = " + texto );
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println( "Não foi possível executar a consulta: \n" + e.getMessage() );
        }
        return clientes;
    }

    @Override
    public Cliente persist(Cliente cliente) {

        var sql = "begin INSERT INTO cliente (ID_CLIENTE, NM_CLIENTE) VALUES (?,?) returning ID_CLIENTE into ?; end;";

        ConnectionFactory factory = ConnectionFactory.of();
        Connection connection = factory.getConnection();

        CallableStatement callableStatement = null;


        try {

            callableStatement = connection.prepareCall( sql );

            callableStatement.setLong( 1, new Random().nextLong( 1, 9999999 ) );
            callableStatement.setString( 2, cliente.getNome() );

            callableStatement.registerOutParameter( 3, Types.BIGINT );
            callableStatement.executeUpdate();

            //Inserindo o Id do Cliente
            cliente.setId( (long) callableStatement.getInt( 3 ) );

            System.out.println( "Cliente salvo com sucesso!!" );

            callableStatement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println( "Não foi possível executar o comando!\n" + e.getMessage() );
        }
        return cliente;
    }
}
