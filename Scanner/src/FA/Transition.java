package FA;

import java.util.List;

public class Transition {
    public String source;
    public String destination;
    public String element;

    public Transition(List<String> tokens) {
        this.source = tokens.get(0);
        this.destination = tokens.get(2);
        this.element = tokens.get(1);
    }

    @Override
    public String toString() {
        return source + "-- " + element + " -->" + destination;
    }
}
