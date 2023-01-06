import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@Data
public class Item {

    private Symbol left;
    private List<Symbol> work;
    private List<Symbol> input;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return Objects.equals(getLeft(), item.getLeft()) && Objects.equals(getWork(), item.getWork()) && Objects.equals(getInput(), item.getInput());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLeft(), getWork(), getInput());
    }

    public Item(Production production) {
        this.left = production.getLeftForCFG();
        this.work = List.of();
        this.input = production.right;
    }

    public boolean isSymbolFirstInInput(Symbol symbol) {
        return !this.input.isEmpty() && this.input.get(0).equals(symbol);
    }

    public Item moveDot() {
        var item = new Item();

        item.setLeft(left);
        var newWork = new ArrayList<>(this.work);
        newWork.add(this.input.get(0));
        item.setWork(newWork);

        var newInput = new ArrayList<>(this.input);
        newInput.remove(0);
        item.setInput(newInput);

        return item;
    }

    @Override
    public String toString() {
        return "[" + left + "->" + work.stream().map(Symbol::toString).collect(Collectors.joining("")) + "." + input.stream().map(Symbol::toString).collect(Collectors.joining("")) + "]";
    }
}
