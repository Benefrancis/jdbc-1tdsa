package br.com.fiap.domain.repository;

import br.com.fiap.Main;
import br.com.fiap.domain.entity.Cliente;
import br.com.fiap.infra.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ClienteRepository implements Repository<Cliente, Long> {

    private static final AtomicReference<ClienteRepository> instance = new AtomicReference<>();

    private ClienteRepository() {
    }

    public static ClienteRepository build() {
        ClienteRepository result = instance.get();
        if (Objects.isNull( result )) {
            ClienteRepository repo = new ClienteRepository();
            if (instance.compareAndSet( null, repo )) {
                result = repo;
            } else {
                result = instance.get();
            }
        }
        return result;
    }

    /**
     * <strong>Método para persistencia de Entidade</strong>
     *
     * @param cliente
     * @return
     */
    @Override
    public Cliente persist(Cliente cliente) {
        var sql = "BEGIN" +
                " INSERT INTO cliente (NM_CLIENTE) " +
                "VALUES (?) " +
                "returning ID_CLIENTE into ?; " +
                "END;" +
                "";



        var factory = ConnectionFactory.build();
        Connection connection = factory.getConnection();


        CallableStatement cs = null;
        try {
            cs = connection.prepareCall( sql );
            cs.setString( 1, cliente.getNome() );
            cs.registerOutParameter( 2, Types.BIGINT );
            cs.executeUpdate();
            cliente.setId( cs.getLong( 2 ) );
            cs.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println( "Não foi possível executar o comando!\n" + e.getMessage() );
        }
        return cliente;
    }

    /**
     * Método que retorna todas as Entidades
     *
     * @return
     */
    @Override
    public List<Cliente> findAll() {

        List<Cliente> clientes = new ArrayList<>();

        try {

            var factory = ConnectionFactory.build();
            Connection connection = factory.getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery( "SELECT * FROM cliente" );

            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong( "ID_CLIENTE" );
                    String nome = resultSet.getString( "NM_CLIENTE" );
                    //Adicionando clientes na coleção
                    clientes.add( new Cliente( id, nome ) );
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

    /**
     * Método que retorna uma Entity pelo seu identificador
     *
     * @param id
     * @return
     */
    @Override
    public Cliente findById(Long id) {
        Cliente cliente = null;
        var sql = "SELECT * FROM cliente where ID_CLIENTE=?";

        var factory = ConnectionFactory.build();
        Connection connection = factory.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement( sql );
            preparedStatement.setLong( 1, id );
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    cliente = new Cliente(
                            resultSet.getLong( "ID_CLIENTE" ),
                            resultSet.getString( "NM_CLIENTE" )
                    );
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
        return cliente;
    }

    private List<Cliente> findByName(String texto) {

        List<Cliente> clientes = new ArrayList<>();
        var sql = "SELECT * FROM cliente where UPPER(NM_CLIENTE) like ?";

        var factory = ConnectionFactory.build();
        Connection connection = factory.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement( sql );
            texto = Objects.nonNull( texto ) ? texto.toUpperCase() : "";
            preparedStatement.setString( 1, "%" + texto + "%" );
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    clientes.add(
                            new Cliente( resultSet.getLong( "ID_CLIENTE" ), resultSet.getString( "NM_CLIENTE" ) )
                    );
                }
            } else {
                System.out.println( "Cliente não encontrado com o nome = " + texto );
            }
            resultSet.close(); preparedStatement.close(); connection.close();
        } catch (SQLException e) {
            System.err.println( "Não foi possível executar a consulta: \n" + e.getMessage() );
        }
        return clientes;
    }

}
