package br.com.fiap;

import br.com.fiap.domain.repository.ClienteRepository;

public class Main {


    public static void main(String[] args) {
        ClienteRepository repository = ClienteRepository.of();
        repository.findAll().forEach( System.out::println );
    }


}

