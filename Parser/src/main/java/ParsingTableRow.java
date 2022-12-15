import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParsingTableRow {
    private String action;
    private List<Symbol> symbols;
    private List<Pair<Symbol, Integer>> goTo = new ArrayList<>();

    public ParsingTableRow(List<Symbol> symbols) {
        this.symbols = symbols;
        for(var symbol: symbols){
            goTo.add(new Pair<>(symbol, null));
        }
    }

    public void setGoToSymbol(Symbol symbol, Integer state){
        for (var pair: goTo) {
            if(pair.getFirst().equals(symbol)){
                pair.setSecond(state);
            }
        }
    }

    @Override
    public String toString() {
        var stringBuilder = new StringBuilder();
        stringBuilder.append(action).append(" -- ");
        for (var item: goTo) {
            if(item.getSecond() == null){
                stringBuilder.append(" ");
            }else{
                stringBuilder.append(item.getFirst()).append(" - ").append(item.getSecond()).append("  ");
            }
        }
        return stringBuilder.toString();
    }
}
