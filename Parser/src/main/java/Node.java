import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Node {
    private Integer index;
    private String info;
    private Integer parent;
    private Integer rightSibling;

    @Override
    public String toString() {
        return  "{ " + index + " } " + " { " + info + " } " + " { parent:" + parent+ " } " + " { sibling:" + rightSibling + " } ";
    }
}
