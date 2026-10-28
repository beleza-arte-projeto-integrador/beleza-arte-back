package com.belezaearte.salaobeleza.application.port.out.professionalsPort;

<<<<<<< HEAD
import com.belezaearte.salaobeleza.adapter.out.persistence.Clients;

import java.time.LocalDateTime;
import java.util.List;

public interface SearchSpecialtyProfessionalsPort {
    List<Clients> professionalExperience (LocalDateTime created);

=======
import com.belezaearte.salaobeleza.adapter.out.persistence.Professionals;

import java.util.List;

public interface SearchSpecialtyProfessionalsPort {
    List<Professionals> searchSpecialty (String specialty);
>>>>>>> 202f520cce9da5cad7acc0f80aac518f0ca348dc
}
