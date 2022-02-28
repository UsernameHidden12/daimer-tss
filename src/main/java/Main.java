import interval.Application;
import interval.input.InputParser;
import interval.inplace.IntervalMerger;
import interval.input.TypeConverter;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        Application<BigDecimal> application = new Application<>(new InputParser<>(new TypeConverter<>(BigDecimal.class)), new IntervalMerger<>());
        application.readInputAndPrintMergedResults();
    }

}
