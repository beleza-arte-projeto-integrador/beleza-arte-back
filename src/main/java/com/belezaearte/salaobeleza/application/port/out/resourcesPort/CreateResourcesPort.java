package com.belezaearte.salaobeleza.application.port.out.resourcesPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Resources;

import java.util.Optional;

public interface CreateResourcesPort {
    Optional<Resources> create (Resources resources);

}
