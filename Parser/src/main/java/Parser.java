import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Parser {

    private Grammar grammar;

    private void enhanceGrammar() {
        grammar.enhance();
    }

    public Parser(Grammar grammar) {
        this.grammar = grammar;
    }

    private List<Set<Item>> states = new ArrayList<>();

    public Set<Item> closure(Set<Item> items) {
        var closure = new HashSet<>(items);
        var changed = true;
        while (changed) {
            changed = false;
            var oldClosure = new HashSet<>(closure);
            for (var item : oldClosure) {
                if (!item.getInput().isEmpty()) {
                    var symbol = item.getInput().get(0);
                    if (grammar.isNonTerminal(symbol.getLabel())) {
                        var oldSize = closure.size();
                        closure.addAll(grammar.getProductionsForNonTerminal(symbol.getLabel())
                                .stream()
                                .map(Item::new)
                                .collect(Collectors.toSet()));
                        changed = changed | oldSize != closure.size();
                    }
                }
            }
        }
        return closure;
    }

    public Set<Item> goTo(Set<Item> state, Symbol symbol) {
        Set<Item> itemsForClosure = new HashSet<>();

        for (var item : state) {
            if (item.isSymbolFirstInInput(symbol)) {
                itemsForClosure.add(item.moveDot());
            }
        }

        return closure(itemsForClosure);
    }

    public void generateCanonicalCollection() {
        this.enhanceGrammar();

        this.states.add(this.closure(Set.of(
                new Item(grammar.getInitialSymbol(),
                        List.of(),
                        grammar.getProductionsForNonTerminal(
                                grammar.getInitialSymbol().getLabel()).get(0).getRight()))));

        var changed = true;
        while (changed) {
            changed = false;
            var newStates = new ArrayList<>(this.states);
            for (var state : this.states) {
                for (Symbol symbol : this.grammar.getAllSymbols()) {
                    var goToResult = this.goTo(state, symbol);
                    if (goToResult.isEmpty() || states.contains(goToResult))
                        continue;

                    changed = true;
                    newStates.add(goToResult);
                }
            }
            this.states = newStates;
        }
    }

    public void printStatesString() {

        for (int i = 0; i < this.states.size(); i++) {
            System.out.print("s" + i + "= {");
            System.out.print(states.get(i).stream().map(Item::toString).collect(Collectors.joining(" ")) + "}\n");

        }
    }

}
