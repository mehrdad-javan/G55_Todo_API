package se.lexicon.todo_api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import se.lexicon.notify.model.Email;
//import se.lexicon.notify.service.MessageService;
import se.lexicon.todo_api.dto.PersonDto;
import se.lexicon.todo_api.entity.Person;
import se.lexicon.todo_api.repository.PersonRepository;
import se.lexicon.todo_api.service.PersonService;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {
    PersonRepository repository;
    //MessageService<Email> emailService;

    @Autowired
    public PersonServiceImpl(PersonRepository repository /*, MessageService<Email> emailService*/) {
        this.repository = repository;
        //this.emailService = emailService;
    }

    @Override
    public List<PersonDto> findAll() {
        return repository.findAll().stream()
                .map(person -> new PersonDto(person.getId(), person.getName(), person.getEmail()))
                .toList();
    }

    @Override
    public PersonDto findById(Long id) {
        Person personEntity = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id: (" + id + ")  not found"));

        return new PersonDto(
                personEntity.getId(),
                personEntity.getName(),
                personEntity.getEmail()
        );
    }

    @Override
    public PersonDto create(PersonDto personDto) {
        // step1: convert or map PersonDto to Person entity
        Person personEntity = new Person();
        personEntity.setName(personDto.name());
        personEntity.setEmail(personDto.email());

        // step2: save the entity using repository
        Person savedEntity = repository.save(personEntity);

        /*emailService.sendMessage(
                new Email(
                        savedEntity.getEmail(),
                        "Welcome to Todo App",
                        "Hello " + savedEntity.getName() + "!\n\nYour account has been successfully created with ID: " + savedEntity.getId() + ".\nWelcome to our Todo Application!")
        );*/

        // step3: convert or map the saved entity back to PersonDto
        return new PersonDto(
                savedEntity.getId(),
                savedEntity.getName(),
                savedEntity.getEmail()
        );

    }

    @Override
    public void delete(Long id) { // 2

        Person foundPerson = repository.findById(id) // step 1: check if the person exists
                .orElseThrow(() -> new IllegalArgumentException("Id not found: " + id));  // step 2 : if exists, delete it

        // step 3: if not exists, throw an exception
        repository.delete(foundPerson);
    }
}
