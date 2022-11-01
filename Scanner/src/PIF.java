import java.util.*;

class PifElement {
    public final String token;
    public Map.Entry<Integer,Integer> position;
    public PifElement(String token, Map.Entry<Integer, Integer> position) {
        this.token = token;
        this.position = position;
    }
}

public class PIF {
    private List<PifElement> elements = new ArrayList<>();

    public void addToken(String token, Map.Entry<Integer, Integer> pos){
        this.elements.add(new PifElement(token, pos));
    }

    public void reorganize(Map<Map.Entry<Integer, Integer>, Map.Entry<Integer, Integer>> newPositions){
        for (var element : elements) {
            if (Objects.equals(element.token, "CONST") | Objects.equals(element.token, "ID")) {
                element.position = newPositions.get(element.position);
                if(element.position == null){
                    System.out.println("null");
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (var element: elements) {
            if(element.position == null){
                System.out.println("null");
            }
            result.append(element.token).append(" [").append(element.position.getKey()).append(", ").append(element.position.getValue()).append("]\n");
        }
        return result.toString();
    }
}
