package seedu.address.logic.parser;

/**
 * Represents a parser which can parse a string and produce a result of type T.
 *
 * This is a functional interface whose functional method is {@link #parse}.
 */
public interface Parser<T> {


    /**
     * Parses an input string and returns the parsed result as an object with type T.
     * @throws ParseException if the input string could not be parsed. The ranges of the ParseException will
     * contain the substring ranges of the input string which caused the parsing to fail.
     */
    T parse(String str) throws ParseException;

}
