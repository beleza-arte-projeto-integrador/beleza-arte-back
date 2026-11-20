package com.belezaearte.salaobeleza.adapter.out.persistence;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ProfessionalAvailabilityRepository;
import com.belezaearte.salaobeleza.application.port.out.professionalAvailabilityPort.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ProfessionalAvailabilityAdapter implements CreateProfessionalAvailabilityPort, UpdateProfessionalAvailabilityPort, DeleteProfessionalAvailabilityPort, SearchIdProfessionalAvailabilityPort, SearchProfessionalAvailabilityPort, SearchAllProfessionalAvailabilityPort {

    private final ProfessionalAvailabilityRepository professionalAvailabilityRepository;

    @Override
    public Optional<ProfessionalAvailability> create(ProfessionalAvailability professionalAvailability) {
        return Optional.of(professionalAvailabilityRepository.saveAndFlush(professionalAvailability));
    }

    @Override
    public Optional<ProfessionalAvailability> update(ProfessionalAvailability professionalAvailability) {
        if (professionalAvailability.getId() == null || !professionalAvailabilityRepository.existsById(professionalAvailability.getId())) {
            return Optional.empty();
        }
        return Optional.of(professionalAvailabilityRepository.saveAndFlush(professionalAvailability));
    }

    @Override
    public void delete(Integer id) {
        professionalAvailabilityRepository.deleteById(id);
    }

    @Override
    public Optional<ProfessionalAvailability> searchId(Integer id) {
        return professionalAvailabilityRepository.findById(id);
    }

    @Override
    public List<ProfessionalAvailability> searchProfessional(Integer professionalId) {
        return professionalAvailabilityRepository.findByProfessionalIdOrderByWeekdayAscStartTimeAsc(professionalId);
    }

    @Override
    public List<ProfessionalAvailability> searchProfessionalWeekday(Integer professionalId, Short weekday) {
        return professionalAvailabilityRepository.findByProfessionalIdAndWeekdayOrderByStartTimeAsc(professionalId, weekday);
    }

    @Override
    public List<ProfessionalAvailability> searchAll() {
        return professionalAvailabilityRepository.findAll();
    }
}
