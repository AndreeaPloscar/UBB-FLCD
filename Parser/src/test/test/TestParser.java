import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class TestParser {

    private Parser parser;
    private Grammar grammar;

    @Before
    public void setUp() {
        parser = new Parser();
        grammar = new Grammar();
        try {
            grammar.readFromFile(Objects.requireNonNull(Menu.class.getResource("g1.txt")).getFile());
        } catch (ReadGrammarException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGoTo() {
        parser = new Parser(grammar);
        assertEquals(parser.goTo(Set.of(new Item(grammar.getInitialSymbol(), List.of(), List.of(new Symbol("S")))),
                        new Symbol("S")),
                Set.of(new Item(grammar.getInitialSymbol(), List.of(new Symbol("S")), List.of())));
    }

    @Test
    public void testClosure() {
        parser = new Parser(grammar);
        assertEquals(parser.closure(Set.of(new Item(grammar.getInitialSymbol(), List.of(), List.of(new Symbol("S"))))),
                Set.of(new Item(grammar.getInitialSymbol(), List.of(), List.of(new Symbol("S"))),
                        new Item(grammar.getInitialSymbol(), List.of(), List.of(new Symbol("a"), new Symbol("A")))
                ));

    }

    @Test
    public void testCanonical() {
        parser = new Parser(grammar);

        var item0 = new Item(new Symbol("S'"), List.of(), List.of(new Symbol("S")));
        var item1 = new Item(new Symbol("S"), List.of(), List.of(new Symbol("a"), new Symbol("A")));
        var item2 = new Item(new Symbol("S'"), List.of(new Symbol("S")), List.of());
        var item3 = new Item(new Symbol("S"), List.of(new Symbol("a")), List.of(new Symbol("A")));
        var item4 = new Item(new Symbol("A"), List.of(), List.of(new Symbol("b"), new Symbol("A")));
        var item5 = new Item(new Symbol("A"), List.of(), List.of(new Symbol("c")));
        var item6 = new Item(new Symbol("S"), List.of(new Symbol("a"), new Symbol("A")), List.of());
        var item7 = new Item(new Symbol("A"), List.of(new Symbol("b")), List.of(new Symbol("A")));
        var item8 = new Item(new Symbol("A"), List.of(new Symbol("c")), List.of());
        var item9 = new Item(new Symbol("A"), List.of(new Symbol("b"), new Symbol("A")), List.of());

        var s0 = Set.of(item0, item1);
        var s1 = Set.of(item2);
        var s2 = Set.of(item3, item4, item5);
        var s3 = Set.of(item6);
        var s4 = Set.of(item4, item5, item7);
        var s5 = Set.of(item8);
        var s6 = Set.of(item9);

        parser.generateCanonicalCollection();

        assertEquals(parser.getStates(),
                List.of(s0, s1, s2, s3, s4, s5, s6));

        parser.printStatesString();

    }

    @Test
    public void testParse() {
        parser = new Parser(grammar);
        var seq = new ArrayList<>(List.of("a", "b","b", "c"));
//        var seq = new ArrayList<>(List.of("if", "1", "<", "2", "print", "(", "A", ")", "else", "print", "(", "B", ")"));
        try {
            parser.parse(seq);
        } catch (Exception e) {
            e.printStackTrace();
        }
        parser.printTree("out1.txt");
        var expected = new Stack<Integer>();
        expected.addAll(List.of(3,2,2, 1));
//        expected.addAll(List.of(80, 89, 92, 7, 11, 81, 89, 92, 7, 6, 16, 15, 14, 2, 17, 15, 14, 2, 5, 4, 3, 1));
        assertEquals(parser.outputStack, expected);
        System.out.println(parser.getTable());
    }

    @Test
    public void testParsePIF() {
        parser = new Parser(grammar);
//        var seq = new ArrayList<>(List.of("a", "b","b", "c"));
//        var seq = new ArrayList<>(List.of("if", "1", "<", "2", "print", "(", "A", ")", "else", "print", "(", "B", ")"));
        try {
            parser.parsePIF("pif.out");
        } catch (Exception e) {
            e.printStackTrace();
        }
        parser.printTree("out2.txt");
        var expected = new Stack<Integer>();
//        expected.addAll(List.of(3,2,2, 1));
        expected.addAll(List.of(7, 11, 7, 6, 15, 14, 2, 15, 14, 2, 5, 4, 3, 1));
        assertEquals(parser.outputStack, expected);
        System.out.println(parser.getTable());
    }

    @Test
    public void testParsingTable() {
        parser = new Parser(grammar);
        try {
            parser.generateParsingTable();
        } catch (ParsingTableConflictException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(parser.getTable());
    }
}
