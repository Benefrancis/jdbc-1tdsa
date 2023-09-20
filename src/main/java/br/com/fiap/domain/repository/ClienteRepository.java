package br.com.fiap.domain.repository;

import br.com.fiap.domain.entity.Cliente;
import br.com.fiap.infra.ConnectionFactory;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class ClienteRepository implements Repository<Cliente, Long> {
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
            var t = Objects.nonNull( texto ) ? texto.toUpperCase() : "";

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

        var sql = "INSERT INTO cliente (ID_CLIENTE, NM_CLIENTE) VALUES (?,?)";

        ConnectionFactory factory = ConnectionFactory.of();
        Connection connection = factory.getConnection();

        try {

            PreparedStatement preparedStatement = connection.prepareStatement( sql );

            preparedStatement.setLong( 1, cliente.getId() );
            preparedStatement.setString( 2, cliente.getNome() );

            preparedStatement.execute();
            System.out.println( "Cliente salvo com sucesso!!" );

            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            System.err.println( "Não foi possível executar o comando!\n" + e.getMessage() );
        }
        return cliente;
    }
}
