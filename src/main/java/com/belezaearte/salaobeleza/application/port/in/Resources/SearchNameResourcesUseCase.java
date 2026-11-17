package com.belezaearte.salaobeleza.application.port.in.Resources;

import com.belezaearte.salaobeleza.application.domain.dto.ResourcesDto;

import java.util.List;

public interface SearchNameResourcesUseCase {
    List<ResourcesDto> searchName (String name);

}
