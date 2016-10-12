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
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement("insert into Persons (id, name, city_id) values(?, ?, ?)");

            statement.setLong(1, person.getId());
            statement.setString(2, person.getName());
            statement.setLong(3, person.getCity().getId());
            statement.executeUpdate();

            PreparedStatement cityStatement = connection.prepareStatement("insert into City (id, name, population) values(?,?,?)");
            cityStatement.setLong(1, person.getCity().getId());
            cityStatement.setString(2, person.getCity().getName());
            cityStatement.setInt(3, person.getCity().getPopulation());
            cityStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                throw new RuntimeException(e);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Person findById(long id) {
        return selectOne(execute("select p.id as id, p.name as name, c.id as cityId, c.name as cityName, c.population as population from Persons p, City c" +
                " where p.city_id = c.id and p.id = ?", s -> s.setLong(1, id)));
    }

    public List<Person> findByName(String name) {
        return execute("select p.id as id, p.name as name, c.id as cityId, c.name as cityName, c.population as population from Persons p, City c" +
                " where p.city_id = c.id and p.name = ?", s -> s.setString(1, name));
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
                long cityId = resultSet.getLong("cityId");
                String cityName = resultSet.getString("cityName");
                int population = resultSet.getInt("population");
                persons.add(new Person(id, personName, new City(cityId, cityName, population)));
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
