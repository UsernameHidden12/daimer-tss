package interval.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class InputParser<T extends Comparable<T>> {

    private static final String REGEX_BETWEEN_INTERVALS = "]\\s*,\\s*\\[";
    private static final String REGEX_START_INTERVALS = "\\[\\s*\\[\\s*";
    private static final String REGEX_END_INTERVALS = "\\s*]\\s*]";
    private static final Logger logger = LoggerFactory.getLogger(InputParser.class);

    private final Supplier<BufferedReader> bufferedReaderProducer;
    private final TypeConverter<T> typeConverter;

    InputParser(Supplier<BufferedReader> bufferedReaderProducer, TypeConverter<T> typeConverter) {
        this.bufferedReaderProducer = bufferedReaderProducer;
        this.typeConverter = typeConverter;
    }

    public InputParser(TypeConverter<T> typeConverter) {
        this(() -> new BufferedReader(new InputStreamReader(System.in)), typeConverter);
    }

    /**
     * Read one line from STDIN and generate a list of T of converted entries for input pattern [[x0, y0],[x1, y1]].
     * Should STDIN be empty or conversion fail at any point during, then return an empty Optional.
     * @return Optional of list if parsing and conversion is successful, else empty Optional.
     */
    public Optional<List<List<T>>> readFromStdin() {
        try (BufferedReader reader = bufferedReaderProducer.get()) {
            String intervals = reader.readLine();
            if (intervals.length() == 0) {
                return Optional.empty();
            }
            List<List<T>> collectedIntervals = Arrays.stream(splitIntoIndividualIntervalsIgnoringWhitespaceBetween(intervals))
                    .map(this::splitIntervalValues)
                    .map(this::convertToTypedList)
                    .collect(Collectors.toList());
            return Optional.of(collectedIntervals);
        } catch (RuntimeException | IOException e) {
            logger.error("Couldn't read stdin. Returning empty list.");
            return Optional.empty();
        }
    }

    private String[] splitIntoIndividualIntervalsIgnoringWhitespaceBetween(String intervals) {
        String[] removeOpeningBraces = intervals.split(REGEX_START_INTERVALS);
        String[] removeClosingBraces = removeOpeningBraces[1].split(REGEX_END_INTERVALS);
        return removeClosingBraces[0].split(REGEX_BETWEEN_INTERVALS);
    }

    private String[] splitIntervalValues(String interval) {
        return interval.split("\\s*,\\s*");
    }

    private List<T> convertToTypedList(String[] splits) {
        return List.of(getElement(splits, 0), getElement(splits, 1));
    }

    private T getElement(String[] splits, int index) throws IllegalArgumentException {
        String formattedStringRepresentation = trimWhiteSpace(splits[index]);
        try {
            return typeConverter.get(formattedStringRepresentation);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException();
        }
    }

    private String trimWhiteSpace(String splits) {
        return splits.trim();
    }

}
