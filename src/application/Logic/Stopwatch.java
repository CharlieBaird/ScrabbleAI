package application.Logic;

public class Stopwatch {

    private long start;

    public Stopwatch()
    {
        start = System.nanoTime();
    }

    public double stop()
    {
        return (System.nanoTime()-start)/1_000_000_000d;
    }

}
