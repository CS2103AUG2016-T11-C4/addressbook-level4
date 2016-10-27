package seedu.address.logic.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.SubstringRange;

/**
 * A utility class that uses a {@link CommandLineScanner} to parse a string, breaking it down into its
 * individual {@link CommandLineParser.Argument} and {@link CommandLineParser.Flag}
 */
public class CommandLineParser {

    private static String MSG_ARG_NOT_PROVIDED = "Required argument not provided: %s";
    private static String MSG_FLAG_NOT_PROVIDED = "Required flag not provided: %s%s";
    private static String MSG_EXPECTED_KEYWORD = "Expected keyword: %s";
    private static String MSG_TOO_MANY_ARGS = "Too many arguments";
    private static String MSG_FLAG_MULTIPLE = "%s specified multiple times";

    private final List<ArgumentParser> arguments = new ArrayList<>();
    private final Map<String, FlagParser> flags = new HashMap<>();

    public CommandLineParser addArgument(ArgumentParser argument) {
        arguments.add(argument);
        return this;
    }

    public CommandLineParser addArgument(String keyword) {
        Parser<Object> parser = str -> {
            if (str.equals(keyword)) {
                return new Object(); // dummy object
            } else {
                SubstringRange range = new SubstringRange(0, str.length());
                throw new ParseException(String.format(MSG_EXPECTED_KEYWORD, keyword), range);
            }
        };
        arguments.add(new Argument<Object>(keyword, parser));
        return this;
    }

    public CommandLineParser putFlag(FlagParser flag) {
        flags.put(flag.getPrefix(), flag);
        return this;
    }

    private Collection<String> getPrefixesForFlags() {
        return flags.values().stream().map(flag -> flag.getPrefix()).collect(Collectors.toList());
    }

    public void parse(String str) throws ParseException {
        CommandLineScanner scanner = new CommandLineScanner(str, getPrefixesForFlags());

        // Reset all argument parsers
        for (ArgumentParser argument : arguments) {
            argument.reset();
        }

        // Reset all flag parsers
        for (FlagParser flag : flags.values()) {
            flag.reset();
        }

        // Parse arguments
        for (ArgumentParser argument : arguments) {
            argument.parse(scanner);
        }

        if (!flags.isEmpty()) {
            // Parse flags
            Optional<CommandLineScanner.Flag> scannerFlag;
            while ((scannerFlag = scanner.nextFlag()).isPresent()) {
                FlagParser flag = flags.get(scannerFlag.get().prefix);
                assert flag != null; // CommandLineScanner should only return flags it recognizes
                flag.parseFlag(scannerFlag.get());
            }

            // Any non-optional flags that are not specified?
            for (FlagParser flag : flags.values()) {
                if (!flag.isOptional() && !flag.isPresent()) {
                    throw new ParseException(String.format(MSG_FLAG_NOT_PROVIDED, flag.getPrefix(), flag.getName()));
                }
            }
        } else {
            // Do we have any extra arguments that should not be there?
            scanner.skipWhitespace();
            if (!scanner.getRemainingInput().isEmpty()) {
                SubstringRange range = new SubstringRange(scanner.getInputPosition(), scanner.getInput().length());
                throw new ParseException(MSG_TOO_MANY_ARGS, range);
            }
        }
    }

    public interface ArgumentParser {
        /** Name of the argument. */
        String getName();

        /** Resets the parser */
        default void reset() {}

        /** Scans and parses in input from the provided {@link CommandLineScanner}. */
        void parse(CommandLineScanner scanner) throws ParseException;
    }

    public static class Argument<T> implements ArgumentParser {
        private final String name;
        private Parser<? extends T> parser;
        private boolean present;
        private T value;
        private SubstringRange range;

        public Argument(String name, Parser<? extends T> parser) {
            assert !CollectionUtil.isAnyNull(name, parser);
            this.name = name;
            this.parser = parser;
        }

        @Override
        public String getName() {
            return name;
        }

        public void setParser(Parser<? extends T> parser) {
            assert parser != null;
            this.parser = parser;
        }

        public boolean isPresent() {
            return present;
        }

        public void set(T value, SubstringRange range) {
            assert !CollectionUtil.isAnyNull(value, range);
            this.value = value;
            this.range = range;
            present = true;
        }

        public void reset() {
            present = false;
            value = null;
            range = null;
        }

        public T getValue() {
            assert present;
            return value;
        }

        public SubstringRange getRange() {
            assert present;
            return range;
        }

        /**
         * Parses the provided {@link CommandLineScanner.Argument}.
         */
        public void parse(CommandLineScanner.Argument arg) throws ParseException {
            final T value;
            try {
                value = parser.parse(arg.value);
            } catch (ParseException e) {
                final ParseExceptionBuilder builder = new ParseExceptionBuilder(e);
                builder.prependMessage(getName() + ": ");
                if (arg.quoted) {
                    builder.clearRanges()
                            .addRange(arg.range);
                } else {
                    builder.indentRanges(arg.range.getStart());
                }
                throw builder.build();
            }
            set(value, arg.range);
        }

        @Override
        public void parse(CommandLineScanner scanner) throws ParseException {
            Optional<CommandLineScanner.Argument> arg = scanner.peekNextArgument();
            if (!arg.isPresent()) {
                SubstringRange range = new SubstringRange(scanner.getInputPosition(), scanner.getInput().length());
                throw new ParseException(String.format(MSG_ARG_NOT_PROVIDED, name), range);
            }
            parse(arg.get());
            scanner.nextArgument();
        }
    }

    public static class RestArgument<T> extends Argument<T> {
        public RestArgument(String name, Parser<? extends T> parser) {
            super(name, parser);
        }

        @Override
        public void parse(CommandLineScanner scanner) throws ParseException {
            parse(scanner.nextRestArgument());
        }
    }

    public interface FlagParser {
        /** Returns the flag prefix */
        String getPrefix();

        /** Returns the flag name */
        String getName();

        /** Resets the flag parsing state */
        default void reset() {}

        /** Returns true if the flag is optional */
        boolean isOptional();

        /** Returns true if the flag has a value */
        boolean isPresent();

        /** Parses the {@link CommandLineScanner.Flag} */
        void parseFlag(CommandLineScanner.Flag flag) throws ParseException;
    }

    private abstract static class BaseFlag<T> implements FlagParser {

        private final String prefix;
        private final String name;
        private Parser<? extends T> parser;
        protected T value;
        protected boolean present;
        protected SubstringRange range;

        protected BaseFlag(String prefix, String name, Parser<? extends T> parser) {
            assert !CollectionUtil.isAnyNull(prefix, name, parser);
            this.prefix = prefix;
            this.name = name;
            this.parser = parser;
        }

        @Override
        public String getPrefix() {
            return prefix;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isPresent() {
            return present;
        }

        public void setParser(Parser<? extends T> parser) {
            assert parser != null;
            this.parser = parser;
        }

        @Override
        public void reset() {
            present = false;
        }

        @Override
        public void parseFlag(CommandLineScanner.Flag flag) throws ParseException {
            assert flag.prefix.equals(prefix);
            if (present) {
                throw new ParseException(String.format(MSG_FLAG_MULTIPLE, prefix), flag.range);
            }
            try {
                value = parser.parse(flag.value);
            } catch (ParseException e) {
                if (flag.quoted) {
                    throw new ParseException(e.getMessage(), e, flag.range);
                } else {
                    throw e.indentRanges(flag.range.getStart());
                }
            }
            present = true;
            range = flag.range;
        }

    }

    public static class Flag<T> extends BaseFlag<T> {

        public Flag(String prefix, String name, Parser<? extends T> parser) {
            super(prefix, name, parser);
        }

        public T getValue() {
            assert present;
            return value;
        }

        public SubstringRange getRange() {
            assert present;
            return range;
        }

        @Override
        public boolean isOptional() {
            return false;
        }

    }

    public static class OptionalFlag<T> extends BaseFlag<T> {

        public OptionalFlag(String prefix, String name, Parser<? extends T> parser) {
            super(prefix, name, parser);
        }

        public Optional<T> getValue() {
            return isPresent() ? Optional.of(value) : Optional.empty();
        }

        public Optional<SubstringRange> getRange() {
            return isPresent() ? Optional.of(range) : Optional.empty();
        }

        @Override
        public boolean isOptional() {
            return true;
        }

    }
}
