package ru.sbt;

import java.util.List;

public interface PersonDao {
    void savePerson(Person person);

    Person findById(long id);

    List<Person> findByName(String name);
}
