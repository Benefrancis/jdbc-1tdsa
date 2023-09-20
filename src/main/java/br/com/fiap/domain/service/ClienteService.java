package br.com.fiap.domain.service;

import br.com.fiap.domain.entity.Cliente;
import br.com.fiap.domain.repository.ClienteRepository;

import java.util.List;

public class ClienteService implements Service<Cliente, Long> {

    private ClienteRepository repo = ClienteRepository.of();


    @Override
    public List<Cliente> findAll() {
        return repo.findAll();
    }

    @Override
    public Cliente findById(Long id) {
        return repo.findById( id );
    }

    @Override
    public List<Cliente> findByName(String texto) {
        return repo.findByName( texto );
    }

    @Override
    public Cliente persist(Cliente cliente) {
        return repo.persist( cliente );
    }
}
