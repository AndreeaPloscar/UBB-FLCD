import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class Production {

    List<NonTerminal> left;
    List<Symbol> right;


    public NonTerminal getLeftForCFG() {
        return left.get(0);
    }

    @Override
    public String toString() {
        return left.stream().map(Object::toString)
                .collect(Collectors.joining(" ")) +
                " -> " +
                right.stream().map(Object::toString)
                        .collect(Collectors.joining(" "));
    }
}
