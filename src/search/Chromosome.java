package search;

public class Chromosome {

    private String [][][] genes; // hoursPerDay - daysPerWeek - subClassesPerSchedule

    private int fitness;

    public Chromosome() {
        this.genes = new String[7][5][9];
    }

    public double fitness() { return 0.0; }

    public Chromosome mutate() { return new Chromosome(); }

    private void createRandomChromosome () {

    }


    public String[][][] getGenes() {
        return genes;
    }

    public void setGenes(String[][][] genes) {
        this.genes = genes;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
}
