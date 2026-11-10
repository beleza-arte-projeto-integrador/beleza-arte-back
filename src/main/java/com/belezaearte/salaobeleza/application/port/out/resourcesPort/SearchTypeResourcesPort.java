package com.belezaearte.salaobeleza.application.port.out.resourcesPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Resources;

import java.util.List;

public interface SearchTypeResourcesPort {
    List<Resources> searchType (String type);
}
