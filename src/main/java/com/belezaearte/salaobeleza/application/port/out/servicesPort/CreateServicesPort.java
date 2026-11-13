package com.belezaearte.salaobeleza.application.port.out.servicesPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Services;

import java.util.Optional;

public interface CreateServicesPort {
    Services create (Services services);
}
