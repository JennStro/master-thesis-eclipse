package errors;

import java.util.HashMap;
import java.util.Map;

public class ErrorInformation {

    private static final HashMap<ErrorType, String> exampleHowToNotDoIt = new HashMap<>(Map.of(
            ErrorType.BITWISE_OPERATOR, "if(a != null & a.getNumber() == 0) {}",
            ErrorType.IGNORING_RETURN_VALUE, "String s = \"ABC\"; \n" +
                    "s.toLowerCase();",
            ErrorType.NOT_USING_EQUALS, "Bag bag1 = new Bag(\"123\"); \n" +
                    "Bag bag2 = new Bag(\"123\"); \n" +
                    "if (bag1 == bag2) { \n" +
                    "   return bag1; \n" +
                    "}",
            ErrorType.SEMICOLON_AFTER_IF, "if (a == 0); {}",
            ErrorType.STATIC_AS_NORMAL, "Animal dog = new Dog(); \n" +
                    "dog.staticMethod();"
    ));

    private static final HashMap<ErrorType, String> exampleHowToDoIt = new HashMap<>(Map.of(
            ErrorType.BITWISE_OPERATOR, "if(a != null && a.getNumber() == 0) {}",
            ErrorType.IGNORING_RETURN_VALUE, "String s = \"ABC\"; \n" +
                    "String lowerCaseString = s.toLowerCase();",
            ErrorType.NOT_USING_EQUALS, "Bag bag1 = new Bag(\"123\"); \n" +
                    "Bag bag2 = new Bag(\"123\"); \n" +
                    "if (bag1.equals(bag2)) { \n" +
                    "   return bag1; \n" +
                    "}",
            ErrorType.SEMICOLON_AFTER_IF, "if (a == 0) {}",
            ErrorType.STATIC_AS_NORMAL, "Animal dog = new Dog(); \n" +
                    "Dog.staticMethod();"
    ));

    private static final HashMap<ErrorType, String> what = new HashMap<>(Map.of(
            ErrorType.BITWISE_OPERATOR, "You are using the bitwiseoperator (& or |) as a logical operator.",
            ErrorType.IGNORING_RETURN_VALUE, "You are ignoring a return value.",
            ErrorType.NOT_USING_EQUALS, "You are using \"==\" to compare object.",
            ErrorType.SEMICOLON_AFTER_IF, "You have a semicolon (;) after an if-statement.",
            ErrorType.STATIC_AS_NORMAL, "You are calling a static method on an object."
    ));

    private static final HashMap<ErrorType, String> why = new HashMap<>(Map.of(
            ErrorType.BITWISE_OPERATOR, "The bitwiseoperator is not short circuit.",
            ErrorType.IGNORING_RETURN_VALUE, "The method call will have no effect because it is not stored in a variable.",
            ErrorType.NOT_USING_EQUALS, "\"==\" uses memory location to check if two objects are equal.",
            ErrorType.SEMICOLON_AFTER_IF, "The semicolon makes the if-condition like any other statement, so the condition has no effect.",
            ErrorType.STATIC_AS_NORMAL, "A static method uses the implementation of its type."
    ));

    public static String getExampleOfHowToNotDoIt(ErrorType type) {
        return exampleHowToNotDoIt.get(type);
    }

    public static String getExampleOfHowToDoIt(ErrorType type) {
        return exampleHowToDoIt.get(type);
    }

    public static String getDescriptionOf(ErrorType type) {
        return what.get(type);
    }

    public static String getCauseOf(ErrorType type) {
        return why.get(type);
    }

}
