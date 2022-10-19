import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
	 var st = new ST(1);
        System.out.println(st.addElement("ab"));
        System.out.println(st.addElement("ba"));
        System.out.println(st.addElement("ab"));
        System.out.println(st.addElement("'ba'"));
        System.out.println(st.addElement("ce"));
        System.out.println(st.addElement(5));
        System.out.println(st.addElement("5"));
        System.out.println(st.addElement(-5));
        System.out.println(st.addElement(-5));
        System.out.println(st.addElement(5));
        System.out.println(Arrays.stream(st.getElements()).map(o -> o.toString()).collect(Collectors.toList()));
    }
}
