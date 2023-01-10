import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class Pair<T,U> {
    private T first;
    private U second;

}
