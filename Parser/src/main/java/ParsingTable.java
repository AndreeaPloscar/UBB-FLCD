import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ParsingTable {
    private List<Symbol> symbols;
    private Integer numberOfStates;
    private Map<Integer, ParsingTableRow> rows = new HashMap<>();

    public ParsingTable(List<Symbol> symbols, Integer numberOfStates) {
        this.symbols = symbols;
        this.numberOfStates = numberOfStates;
        for(int s = 0 ; s < numberOfStates; s++){
            rows.put(s, new ParsingTableRow(symbols));
        }
    }

    public void setGoToSymbol(Integer index, Symbol symbol, Integer state){
        var row = rows.get(index);
        row.setGoToSymbol(symbol, state);
        rows.put(index, row);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("-- action ---------------- goto -------------").append("\n");
        for (var state: rows.keySet()) {
            stringBuilder.append(state).append(" ");
            stringBuilder.append(rows.get(state)).append("\n");
        }
        return stringBuilder.toString();
    }
}
