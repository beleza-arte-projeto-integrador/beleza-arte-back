package com.belezaearte.salaobeleza.application.port.out.servicesPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Services;

import java.util.List;

public interface SearchNameServicesPort {
    List<Services> searchName (String name);
}
