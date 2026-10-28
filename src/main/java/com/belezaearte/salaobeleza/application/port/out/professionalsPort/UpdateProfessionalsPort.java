package com.belezaearte.salaobeleza.application.port.out.professionalsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Professionals;

import java.util.Optional;

public interface UpdateProfessionalsPort {
    Optional<Professionals> update (Professionals professionals);
}
