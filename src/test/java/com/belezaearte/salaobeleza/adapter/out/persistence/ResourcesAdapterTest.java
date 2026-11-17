package com.belezaearte.salaobeleza.adapter.out.persistence;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ResourcesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResourcesAdapterTest {

    @InjectMocks
    private ResourcesAdapter resourcesAdapter;

    @Mock
    private ResourcesRepository resourcesRepository;

    @Test
    @DisplayName("The resource ID must be deleted")
    void deleteId(){
        resourcesAdapter.delete(1);
        verify(resourcesRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("The resource must be updated if it exists.")
    void updateResources(){

        Resources newResourceData = Resources.builder().id(1).name("Anna Updated").type("MACA").active(false).build();
        Resources existingResource = Resources.builder().id(1).name("Ana").type("MACA").active(false).build();

        when(resourcesRepository.findById(Mockito.any())).thenReturn(Optional.of(existingResource));
        when(resourcesRepository.saveAndFlush(newResourceData)).thenReturn(newResourceData);

        Optional<Resources> result = resourcesAdapter.update(newResourceData);

        assertTrue(result.isPresent());
        assertEquals("Anna Updated", result.get().getName());

        verify(resourcesRepository,times(1)).findById(1);
        verify(resourcesRepository,times(1)).saveAndFlush(newResourceData);
    }

    @Test
    @DisplayName("Must not update if the Resource does not exist")
    void  notUpdateResource(){

        Resources newResourceData = Resources.builder().id(1).name("Anna Updated").type("MACA").active(false).build();

        when(resourcesRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        Optional<Resources> result = resourcesAdapter.update(newResourceData);

        assertNull(result);

        verify(resourcesRepository,times(1)).findById(1);
    }

    @Test
    @DisplayName("Must create Resource")
    void createResource(){
        Resources resourceSaved = Resources.builder().id(1).name("Anna Updated").type("MACA").active(false).build();
        when(resourcesRepository.saveAndFlush(resourceSaved)).thenReturn(resourceSaved);

        Optional<Resources> result = resourcesAdapter.create(resourceSaved);

        assertNotNull(result);
        verify(resourcesRepository,times(1)).saveAndFlush(resourceSaved);
    }

    @Test
    @DisplayName("Must search by name")
    void searchName(){
        Resources resources = new Resources();
        resources.setName("Anna");

        when(resourcesRepository.findAll(any(Example.class))).thenReturn(List.of(resources));
        List<Resources> result = resourcesAdapter.searchName("Anna");

        assertFalse(result.isEmpty(), "The list should not be empty");
        assertEquals(1,result.size());
        assertEquals("Anna", result.get(0).getName());

        ArgumentCaptor<Example<Resources>> captor = ArgumentCaptor.forClass(Example.class);
        verify(resourcesRepository).findAll(captor.capture());
    }

    @Test
    @DisplayName("Must search by type")
    void searchType(){
        Resources resources = new Resources();
        resources.setType("MACA");

        when(resourcesRepository.findAll(any(Example.class))).thenReturn(List.of(resources));
        List<Resources> result = resourcesAdapter.searchType("MACA");

        assertFalse(result.isEmpty(), "The list should not be empty");
        assertEquals(1,result.size());
        assertEquals("MACA", result.get(0).getType());

        ArgumentCaptor<Example<Resources>> captor = ArgumentCaptor.forClass(Example.class);
        verify(resourcesRepository).findAll(captor.capture());

    }

    @Test
    @DisplayName("Must search by active")
    void searchActive(){
        Resources resources = new Resources();
        resources.setActive(false);

        when(resourcesRepository.findAll(any(Example.class))).thenReturn(List.of(resources));
        List<Resources> result = resourcesAdapter.searchActive(false);

        assertFalse(result.isEmpty(), "The list should not be empty");
        assertEquals(1,result.size());
        assertEquals(false, result.get(0).getActive());

        ArgumentCaptor<Example<Resources>> captor = ArgumentCaptor.forClass(Example.class);
        verify(resourcesRepository).findAll(captor.capture());

    }

}
