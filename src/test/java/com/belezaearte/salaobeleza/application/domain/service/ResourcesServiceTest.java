package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.Resources;
import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ResourcesRepository;
import com.belezaearte.salaobeleza.application.domain.dto.ResourcesDto;
import com.belezaearte.salaobeleza.application.port.out.resourcesPort.*;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResourcesServiceTest {

    @InjectMocks
    private ResourcesService resourcesService;

    @Mock
    private DeleteResourcesPort deleteResourcesPort;
    @Mock
    private UpdateResourcesPort updateResourcesPort;
    @Mock
    private CreateResourcesPort createResourcesPort;
    @Mock
    private SearchActiveResourcesPort searchActiveResourcesPort;
    @Mock
    private SearchNameResourcesPort searchNameResourcesPort;
    @Mock
    private SearchTypeResourcesPort searchTypeResourcesPort;
    @Mock
    private ResourcesRepository resourcesRepository;


    @Test
    @DisplayName("Should delete resources")
    void deleteResources(){
        Resources resources = Resources.builder().id(1).build();
        when(resourcesRepository.findById(1)).thenReturn(Optional.of(resources));

        resourcesService.deleteResources(1);

        verify(resourcesRepository,times(1)).findById(1);
        verify(deleteResourcesPort,times(1)).delete(1);
        verify(deleteResourcesPort,never()).delete(0);
    }

    @Test
    @DisplayName("Should not delete resources when they do not exist")
    void shouldNotDeleteResources(){
        when(resourcesRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BusinessOutboundException.class, () -> resourcesService.deleteResources(1));

        verify(resourcesRepository,times(1)).findById(1);
        verify(deleteResourcesPort,never()).delete(anyInt());
    }

    @Test
    @DisplayName("Should create a resource")
    void createResource(){
        ResourcesDto resourcesDto = ResourcesDto.builder().name("Anna").type("MACA").active(false).build();
        Resources resourceSaved = Resources.builder().id(1).name("Anna").type("MACA").active(false).build();

        when(createResourcesPort.create(any(Resources.class))).thenReturn(Optional.of(resourceSaved));

        ResourcesDto result = resourcesService.createResources(resourcesDto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Anna", result.getName());
        verify(createResourcesPort, times(1)).create(any(Resources.class));
    }

    @Test
    @DisplayName("Should update resources")
    void updateResources(){
        Resources newResourceData = Resources.builder().id(1).name("Anna Updated").type("MACA").active(false).build();
        ResourcesDto existingResource = ResourcesDto.builder().id(1).name("Ana").type("MACA").active(false).build();

        when(updateResourcesPort.update(any(Resources.class))).thenReturn(Optional.of(newResourceData));

        ResourcesDto result = resourcesService.updateResources(existingResource);

        assertNotNull(result);
        assertEquals("Anna Updated", result.getName());

        verify(updateResourcesPort, times(1)).update(any(Resources.class));

    }

    @Test
    @DisplayName("Should return null when the port returns null")
    void updateResourcesNull(){
        ResourcesDto existingResource = ResourcesDto.builder().id(1).name("Ana").type("MACA").active(false).build();

        when(updateResourcesPort.update(any())).thenReturn(null);

        assertThrows(BusinessOutboundException.class, () -> resourcesService.updateResources(existingResource));

        verify(updateResourcesPort).update(any());
    }

    @Test
    @DisplayName("Should search resources by name")
    void searchByName(){
        Resources resources = Resources.builder().name("Anna").id(1).build();

        when(searchNameResourcesPort.searchName("Anna")).thenReturn(List.of(resources));

        ResourcesDto result = resourcesService.searchName("Anna").get(0);

        assertNotNull(result);
        assertEquals("Anna", result.getName());
        assertEquals(1, result.getId());
        verify(searchNameResourcesPort, times(1)).searchName("Anna");
    }

    @Test
    @DisplayName("Should search resources by type")
    void searchByType(){
        Resources resources = Resources.builder().type("MACA").id(1).build();

        when(searchTypeResourcesPort.searchType("MACA")).thenReturn(List.of(resources));

        ResourcesDto result = resourcesService.searchType("MACA").get(0);

        assertNotNull(result);
        assertEquals("MACA", result.getType());
        assertEquals(1, result.getId());
        verify(searchTypeResourcesPort, times(1)).searchType("MACA");
    }

    @Test
    @DisplayName("Should search resources by active")
    void searchByActive(){
        Resources resources = Resources.builder().active(false).id(1).build();

        when(searchActiveResourcesPort.searchActive(false)).thenReturn(List.of(resources));

        ResourcesDto result = resourcesService.searchActive(false).get(0);

        assertNotNull(result);
        assertEquals(false, result.getActive());
        assertEquals(1, result.getId());
        verify(searchActiveResourcesPort, times(1)).searchActive(false);
    }

}
