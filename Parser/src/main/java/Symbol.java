
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor

public class Symbol {

    private String label;

    public Symbol(String label) {
        this.label = label;
    }

    @Override
    public String toString(){
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Symbol)) return false;
        Symbol symbol = (Symbol) o;
        return Objects.equals(getLabel(), symbol.getLabel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLabel());
    }
}

