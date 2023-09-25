package br.com.fiap.domain.service;

import java.util.List;

/**
 * Padronização de métodos de Service
 *
 * @param <T>
 * @param <U>
 * @author Benefrancis
 */
public interface Service<T, U> {

    public List<T> findAll();

    public T findById(U id);

    public List<T> findByName(String texto);

    public T persist(T t);

    public T update(T t);

    public boolean delete(U id);

}