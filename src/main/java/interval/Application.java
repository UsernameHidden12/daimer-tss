package interval;

import interval.inplace.IntervalMerger;
import interval.input.InputParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public final class Application<T extends Comparable<T>> {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private final InputParser<T> inputParser;
    private final IntervalMerger<T> intervalMerger;

    /**
     * Configuration of application for injected InputParser and IntervalMerger
     * @param inputParser Parser to use for reading from STDIN
     * @param intervalMerger Service merging and returning merged intervals
     */
    public Application(InputParser<T> inputParser, IntervalMerger<T> intervalMerger) {
        this.inputParser = inputParser;
        this.intervalMerger = intervalMerger;
    }

    /**
     * Load input data from STDIN, merge intervals on successful parsing and print them to STDOUT. If parsing fails
     * return early with an error message.
     */
    public void readInputAndPrintMergedResults() {
        Optional<List<List<T>>> intervals = inputParser.readFromStdin();
        if (intervals.isEmpty()) {
            logger.error("Standard input needs to be list of intervals in the form [[x_0, y_0], [x_1, y_1]]");
            return;
        }

        List<List<T>> intervalsMerged = intervalMerger.merge(intervals.get());
        System.out.println(intervalsMerged);
    }

}
