package com.belezaearte.salaobeleza.adapter.out.persistence;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ResourcesRepository;
import com.belezaearte.salaobeleza.application.port.out.resourcesPort.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ResourcesAdapter implements CreateResourcesPort, DeleteResourcesPort, SearchActiveResourcesPort, SearchNameResourcesPort, SearchTypeResourcesPort, UpdateResourcesPort {

    private final ResourcesRepository resourcesRepository;

    @Override
    public Optional<Resources> create(Resources resources) {
        return Optional.of(resourcesRepository.saveAndFlush(resources));
    }

    @Override
    public void delete(Integer id) {
        resourcesRepository.deleteById(id);
    }

    @Override
    public List<Resources> searchActive(Boolean active) {
        Resources resources = Resources.builder().active(active).build();
        Example<Resources> example = Example.of(resources);
        return resourcesRepository.findAll(example);
    }

    @Override
    public List<Resources> searchName(String name) {
        Resources resources = Resources.builder().name(name).build();
        Example<Resources> example = Example.of(resources);
        return resourcesRepository.findAll(example);
    }

    @Override
    public List<Resources> searchType(String type) {
        Resources resources = Resources.builder().type(type).build();
        Example<Resources> example = Example.of(resources);
        return resourcesRepository.findAll(example);
    }

    @Override
    public Optional<Resources> update(Resources resources) {
        Optional<Resources> resourcesOptional = resourcesRepository.findById(resources.getId());
        if (resourcesOptional.isPresent()){
            return Optional.of(resourcesRepository.saveAndFlush(resources));
        }
        else {
            return null;
        }
    }
}
