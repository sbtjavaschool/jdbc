package ru.sbt;

public class City {
    private final long id;
    private final String name;
    private final int population;

    public City(long id, String name, int population) {
        this.id = id;
        this.name = name;
        this.population = population;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }
}
