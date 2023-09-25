package br.com.fiap.domain.service;

import br.com.fiap.domain.entity.Cliente;
import br.com.fiap.domain.repository.ClienteRepository;

import java.util.List;
import java.util.Objects;

public class ClienteService implements Service<Cliente, Long> {

    private ClienteRepository repo = ClienteRepository.of();

    @Override
    public List<Cliente> findAll() {
        return repo.findAll();
    }

    @Override
    public Cliente findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public List<Cliente> findByName(String texto) {
        return repo.findByName(texto);
    }

    @Override
    public Cliente persist(Cliente cliente) {
        return repo.persist(cliente);
    }

    public Cliente update(Cliente cliente) {

        if (Objects.nonNull(cliente.getNome()) && !cliente.getNome().trim().equals("")) {
            cliente.setNome(cliente.getNome().trim());
            //Nome é obrigatório
            return repo.update(cliente);
        }
        return null;
    }

    /**
     * @param id
     */
    @Override
    public boolean delete(Long id) {
        Cliente c = findById(id);
        if (Objects.isNull(c)) return false;
        return repo.delete(c.getId());
    }
}
