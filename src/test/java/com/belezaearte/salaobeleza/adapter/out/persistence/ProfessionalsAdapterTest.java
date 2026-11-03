package com.belezaearte.salaobeleza.adapter.out.persistence;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ProfessionalsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProfessionalsAdapterTest {

    @InjectMocks
    private ProfessionalsAdapter professionalsAdapter;

    @Mock
    private ProfessionalsRepository professionalsRepository;

    @Test
    @DisplayName("")
    void deleteId(){}
}
