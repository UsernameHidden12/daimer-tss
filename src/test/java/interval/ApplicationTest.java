package interval;

import interval.inplace.IntervalMerger;
import interval.input.InputParser;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationTest {

    private Application<Long> underTest;

    @Mock
    private InputParser<Long> inputParser;
    @Mock
    private IntervalMerger<Long> intervalMerger;

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        underTest = new Application<>(inputParser, intervalMerger);
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void givenParsingError_whenReadInputAndPrintMergedResults_thenReturnEarlyAndWarnUser() {
        when(inputParser.readFromStdin()).thenReturn(Optional.empty());
        LogCaptor logCaptor = LogCaptor.forClass(Application.class);

        underTest.readInputAndPrintMergedResults();

        assertThat(logCaptor.getErrorLogs()).containsExactly("Standard input needs to be list of intervals in the form [[x_0, y_0], [x_1, y_1]]");
        verifyNoInteractions(intervalMerger);
    }

    @Test
    void givenValidInput_whenReadInputAndPrintMergedResults_thenMergeIntervals() {
        List<List<Long>> intervalList = List.of(List.of(1L, 3L));
        List<List<Long>> mergedIntervals = List.of(List.of(1L, 3L));
        when(inputParser.readFromStdin()).thenReturn(Optional.of(intervalList));
        when(intervalMerger.merge(intervalList)).thenReturn(mergedIntervals);
        LogCaptor logCaptor = LogCaptor.forClass(Application.class);

        underTest.readInputAndPrintMergedResults();

        assertThat(logCaptor.getErrorLogs()).isEmpty();
        assertThat(outputStreamCaptor.toString().trim()).isEqualTo(mergedIntervals.toString());
    }

}