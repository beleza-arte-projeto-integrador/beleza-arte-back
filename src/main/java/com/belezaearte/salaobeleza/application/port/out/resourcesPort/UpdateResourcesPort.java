package com.belezaearte.salaobeleza.application.port.out.resourcesPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Resources;

import java.util.Optional;

public interface UpdateResourcesPort {
    Optional<Resources> update (Resources resources);

}
