package jan.yuboingirirobobobiko.trace;

public record TraceConnection(TraceObject trace, Direction direction) {
    public enum Direction {
        OUTPUT,
        INPUT,
    }
}
