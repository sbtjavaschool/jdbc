package ru.sbt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonDaoImpl implements PersonDao {
    private final Connection connection;

    public PersonDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public void savePerson(Person person) {
        try {
            PreparedStatement statement = connection.prepareStatement("insert into Persons (id, name) values(?, ?)");
            statement.setLong(1, person.getId());
            statement.setString(2, person.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Person findById(long id) {
        return selectOne(execute("select * from Persons where id = ?", s -> s.setLong(1, id)));
    }

    public List<Person> findByName(String name) {
        return execute("select * from Persons where name = ?", s -> s.setString(1, name));
    }

    private Person selectOne(List<Person> persons) {
        return persons.isEmpty() ? null : persons.get(0);
    }

    private List<Person> execute(String sql, Consumer<PreparedStatement> consumer) {
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            consumer.accept(statement);
            ResultSet resultSet = statement.executeQuery();

            List<Person> persons = new ArrayList<>();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String personName = resultSet.getString("name");
                persons.add(new Person(id, personName));
            }

            return persons;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private interface Consumer<T> {
        void accept(T t) throws Exception;
    }
}
