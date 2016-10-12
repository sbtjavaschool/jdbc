package ru.sbt;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class Main {
    public static void main(String[] args) throws SQLException {
        try (Connection connection = getConnection("jdbc:h2:~/test", "admin", "secret")) {
//            createTable(connection);

            PersonDao personDao = new PersonDaoImpl(connection);

            City city = new City(890, "Moscow", 10_000_000);
//            personDao.savePerson(new Person(10L, "Alex", city));
//            personDao.savePerson(new Person(12L, "Alex", city));
//            personDao.savePerson(new Person(15L, "Bob", city));

            List<Person> persons = personDao.findByName("Alex");

            System.out.println(personDao.findById(10L));
            System.out.println(persons);
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        connection.createStatement().executeUpdate("CREATE TABLE Persons" +
                "(" +
                "id int," +
                "name varchar(255)," +
                "city_id int," +
                "primary key(id)" +
                ");");

        connection.createStatement().executeUpdate("CREATE TABLE City" +
                "(" +
                "id int," +
                "name varchar(255)," +
                "population int" +
                ");");
    }
}
