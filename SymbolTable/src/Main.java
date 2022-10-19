import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
	 var st = new ST(1);
        System.out.println(st.addIdentifier("ab"));
        System.out.println(st.addIdentifier("ba"));
        System.out.println(st.addIdentifier("ab"));
        System.out.println(st.addIdentifier("ba"));
        System.out.println(st.addIdentifier("ce"));
        System.out.println(st.addIdentifier(5));
        System.out.println(st.addIdentifier("5"));
        System.out.println(st.addIdentifier(-5));
        System.out.println(st.addIdentifier(-5));
        System.out.println(st.addIdentifier(5));
        System.out.println(Arrays.stream(st.getElements()).map(o -> o.toString()).collect(Collectors.toList()));
    }
}
