package com.belezaearte.salaobeleza.adapter.out.persistence;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ProfessionalsRepository;
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
public class ProfessionalsAdapterTest {

    @InjectMocks
    private ProfessionalsAdapter professionalsAdapter;

    @Mock
    private ProfessionalsRepository professionalsRepository;

    @Test
    @DisplayName("The professional ID must be deleted.")
    void deleteId(){
        professionalsAdapter.delete(1);
        verify(professionalsRepository, times(1)).deleteById(1);
    }
}
