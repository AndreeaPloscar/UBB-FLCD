
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
}
