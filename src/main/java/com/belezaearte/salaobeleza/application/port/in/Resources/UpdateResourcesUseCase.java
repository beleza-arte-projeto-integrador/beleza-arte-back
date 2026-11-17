package com.belezaearte.salaobeleza.application.port.in.Resources;

import com.belezaearte.salaobeleza.application.domain.dto.ResourcesDto;

public interface UpdateResourcesUseCase {
    ResourcesDto updateResources (ResourcesDto resourcesDto);

}
