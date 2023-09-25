package br.com.fiap.infra.configuration.cors;

import org.glassfish.jersey.server.ResourceConfig;

public class MyApplication extends ResourceConfig {

    public MyApplication() {
        // Pacote onde estão suas classes de recursos
        packages("br.com.fiap.domain.resources");

        // Registrar o filtro CORS
        register(CORSFilter.class);
    }
}
