package com.belezaearte.salaobeleza.application.port.out.resourcesPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Resources;

import java.util.List;

public interface SearchActiveResourcesPort {
    List<Resources> searchActive (Boolean active);
}
