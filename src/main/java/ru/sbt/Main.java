package ru.sbt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class Main {
    public static void main(String[] args) throws SQLException {
        try (Connection connection = getConnection("jdbc:h2:~/test", "admin", "secret")) {
//        createTable(connection);

            PersonDao personDao = new PersonDaoImpl(connection);

            personDao.savePerson(new Person(10L, "Alex"));
            personDao.savePerson(new Person(12L, "Alex"));
            personDao.savePerson(new Person(15L, "Bob"));

            List<Person> persons = personDao.findByName("Bob");
            System.out.println(persons);
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        connection.createStatement().executeUpdate("CREATE TABLE Persons" +
                "(" +
                "id int," +
                "name varchar(255)" +
                ");");
    }
}
