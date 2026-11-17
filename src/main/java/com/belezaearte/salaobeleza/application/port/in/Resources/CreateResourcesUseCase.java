package com.belezaearte.salaobeleza.application.port.in.Resources;

import com.belezaearte.salaobeleza.application.domain.dto.ResourcesDto;

public interface CreateResourcesUseCase {
    ResourcesDto createResources (ResourcesDto resourcesDto);
}
