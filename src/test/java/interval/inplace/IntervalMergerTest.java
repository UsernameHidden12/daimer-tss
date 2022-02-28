package interval.inplace;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

class IntervalMergerTest {

    private IntervalMerger<Long> underTest;

    private static Stream<Arguments> repeatingIntervals() {
        return Stream.of(
                Arguments.of(List.of(List.of(1L, 2L)), List.of(List.of(1L, 2L), List.of(1L, 2L))),
                Arguments.of(List.of(List.of(1L, 2L), List.of(3L, 4L)), List.of(List.of(1L, 2L), List.of(3L, 4L), List.of(1L, 2L)))
        );
    }

    private static Stream<Arguments> nonInterleavingIntervals() {
        return Stream.of(
                Arguments.of(List.of(List.of(1L, 2L)), List.of(List.of(1L, 2L))),
                Arguments.of(List.of(List.of(1L, 2L), List.of(3L, 4L)), List.of(List.of(1L, 2L), List.of(3L, 4L))),
                Arguments.of(List.of(List.of(1L, 2L), List.of(3L, 4L)), List.of(List.of(3L, 4L), List.of(1L, 2L))),
                Arguments.of(List.of(List.of(1L, 2L), List.of(3L, 4L), List.of(5L, 6L)), List.of(List.of(1L, 2L), List.of(3L, 4L), List.of(5L, 6L))),
                Arguments.of(List.of(List.of(1L, 2L), List.of(3L, 4L), List.of(5L, 6L)), List.of(List.of(1L, 2L), List.of(5L, 6L), List.of(3L, 4L))),
                Arguments.of(List.of(List.of(1L, 2L), List.of(3L, 4L), List.of(5L, 6L)), List.of(List.of(5L, 6L), List.of(1L, 2L), List.of(3L, 4L))),
                Arguments.of(List.of(List.of(1L, 2L), List.of(3L, 4L), List.of(5L, 6L)), List.of(List.of(3L, 4L), List.of(5L, 6L), List.of(1L, 2L))),
                Arguments.of(List.of(List.of(1L, 2L), List.of(3L, 4L), List.of(5L, 6L)), List.of(List.of(5L, 6L), List.of(3L, 4L), List.of(1L, 2L)))
        );
    }

    private static Stream<Arguments> interleavingIntervalsOneSide() {
        return Stream.of(
                Arguments.of(List.of(List.of(1L, 3L)), List.of(List.of(1L, 2L), List.of(2L, 3L))),
                Arguments.of(List.of(List.of(1L, 3L)), List.of(List.of(2L, 3L), List.of(1L, 2L))),
                Arguments.of(List.of(List.of(1L, 4L)), List.of(List.of(1L, 3L), List.of(2L, 4L))),
                Arguments.of(List.of(List.of(1L, 4L)), List.of(List.of(2L, 4L), List.of(1L, 3L)))
        );
    }

    private static Stream<Arguments> interleavingIntervalsBothSides() {
        return Stream.of(
                Arguments.of(List.of(List.of(1L, 4L)), List.of(List.of(2L, 3L), List.of(1L, 4L))),
                Arguments.of(List.of(List.of(1L, 4L)), List.of(List.of(1L, 4L), List.of(2L, 3L)))
        );
    }

    @BeforeEach
    void setUp() {
        underTest = new IntervalMerger<>();
    }

    @Test
    void givenEmptyList_whenMerging_thenReturnEmptyList() {
        List<List<Long>> actual = underTest.merge(Collections.emptyList());

        Assertions.assertThat(actual).isNotNull().isEmpty();
    }

    @Test
    void givenInterleavingList_whenMerging_thenReturnInterleaving() {
        List<List<Long>> expected = List.of(List.of(0L, 0L), List.of(1L, 1L), List.of(3L, 7L));
        List<List<Long>> intervals = List.of(List.of(5L, 7L), List.of(5L, 5L), List.of(1L, 1L), List.of(0L, 0L), List.of(3L, 3L), List.of(4L, 5L), List.of(1L, 1L), List.of(3L, 4L));

        intervalTest(intervals, expected);
    }

    private void intervalTest(List<List<Long>> intervals, List<List<Long>> expected) {
        List<List<Long>> actual = underTest.merge(intervals);

        Assertions.assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @ParameterizedTest(name = "Expect {0} for non-overlapping intervals {1}")
    @MethodSource("nonInterleavingIntervals")
    void givenNonInterleavingIntervals_whenMerging_thenReturnListAsIs(List<List<Long>> expected, List<List<Long>> intervals) {
        intervalTest(intervals, expected);
    }

    @ParameterizedTest(name = "Expect {0} for one sided overlapping intervals {1}")
    @MethodSource("interleavingIntervalsOneSide")
    void givenOneSidedInterleavingIntervals_whenMerging_thenReturnMergedIntervals(List<List<Long>> expected, List<List<Long>> intervals) {
        intervalTest(intervals, expected);
    }

    @ParameterizedTest(name = "Expect {0} for two sided overlapping intervals {1}")
    @MethodSource("interleavingIntervalsBothSides")
    void givenCompletelyInterleavingIntervals_whenMerging_thenReturnMergedIntervals(List<List<Long>> expected, List<List<Long>> intervals) {
        intervalTest(intervals, expected);
    }

    @ParameterizedTest(name = "Expect {0} for intervals with repetition {1}")
    @MethodSource("repeatingIntervals")
    void givenRepeatingIntervals_whenMerging_thenReturnMergedIntervals(List<List<Long>> expected, List<List<Long>> intervals) {
        intervalTest(intervals, expected);
    }

    @Test
    void givenIntervalOverlappingOverAllAtEnd_whenMerging_thenReturnSingleInterval() {
        List<List<Long>> expected = List.of(List.of(1L, 10L));
        List<List<Long>> intervals = List.of(List.of(1L, 2L), List.of(9L, 10L), List.of(1L, 10L));

        intervalTest(intervals, expected);
    }

}