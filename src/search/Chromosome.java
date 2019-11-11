package search;

public class Chromosome {

    private String [][][] genes;

    public Chromosome() { }

    public double fitness() { return 0.0; }

    public Chromosome mutate() { return new Chromosome(); }
}
