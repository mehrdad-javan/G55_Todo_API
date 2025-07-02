
package se.lexicon.todo_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.todo_api.dto.PersonDto;
import se.lexicon.todo_api.entity.Person;
import se.lexicon.todo_api.repository.PersonRepository;
import se.lexicon.todo_api.service.impl.PersonServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
@Rollback(true)  // This is optional as true is the default value

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonServiceImpl personService;

    private Person person;
    private PersonDto personDto;
    private final Long TEST_ID = 1L;
    private final String TEST_NAME = "Mehrdad Javan";
    private final String TEST_EMAIL = "mehrdad.javan@lexicon.se";

    @BeforeEach
    void setUp() {
        person = new Person(TEST_ID, TEST_NAME, TEST_EMAIL, null);
        personDto = new PersonDto(TEST_ID, TEST_NAME, TEST_EMAIL);
    }

    @Test
    void testCreate() {
        // Arrange
        Person personToSave = new Person(TEST_NAME, TEST_EMAIL);
        when(personRepository.save(any(Person.class))).thenReturn(person);

        // Act
        PersonDto created = personService.create(personDto);

        // Assert
        assertNotNull(created);
        assertEquals(TEST_NAME, created.name());
        assertEquals(TEST_EMAIL, created.email());
        verify(personRepository).save(any(Person.class));
    }

    @Test
    void testFindAll() {
        // Arrange
        Person person2 = new Person(2L, "Mehrdad Javan", "mehrdad.javan@lexicon.se", null);
        when(personRepository.findAll()).thenReturn(Arrays.asList(person, person2));

        // Act
        List<PersonDto> result = personService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(TEST_NAME, result.get(0).name());
        assertEquals("Mehrdad Javan", result.get(1).name());
        verify(personRepository).findAll();
    }

    @Test
    void testFindById() {
        // Arrange
        when(personRepository.findById(TEST_ID)).thenReturn(Optional.of(person));

        // Act
        PersonDto found = personService.findById(TEST_ID);

        // Assert
        assertNotNull(found);
        assertEquals(TEST_ID, found.id());
        assertEquals(TEST_NAME, found.name());
        verify(personRepository).findById(TEST_ID);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        when(personRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> personService.findById(TEST_ID));
        verify(personRepository).findById(TEST_ID);
    }

}