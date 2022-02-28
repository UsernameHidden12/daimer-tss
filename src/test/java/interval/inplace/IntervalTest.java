package interval.inplace;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class IntervalTest {

    private static Stream<Arguments> overlappingIntervals() {
        return Stream.of(
                Arguments.of(new Interval<>(8, 12), new Interval<>(10, 12), new Interval<>(8, 12)),
                Arguments.of(new Interval<>(8, 12), new Interval<>(10, 12), new Interval<>(8, 10)),
                Arguments.of(new Interval<>(10, 14), new Interval<>(10, 12), new Interval<>(10, 14)),
                Arguments.of(new Interval<>(10, 14), new Interval<>(10, 12), new Interval<>(12, 14)),
                Arguments.of(new Interval<>(10, 14), new Interval<>(10, 14), new Interval<>(11, 13))
        );
    }

    private static Stream<Arguments> notOverlappingIntervals() {
        return Stream.of(
                Arguments.of(1, new Interval<>(10, 12), new Interval<>(8, 9)),
                Arguments.of(-1, new Interval<>(10, 12), new Interval<>(13, 14))
        );
    }

    @Test
    void givenAnIllegalInterval_whenInstantiating_thenThrowIllegalArgumentException() {
        assertThatThrownBy(() -> new Interval<>(1, 0)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenZeroLengthInterval_whenInstantiating_thenAllow() {
        assertThatCode(() -> new Interval<>(0, 0)).doesNotThrowAnyException();
    }

    @Test
    void givenAnInterval_whenAsList_thenReturnInRightOrder() {
        Interval<Integer> interval = new Interval<>(1, 2);
        List<Integer> expected = List.of(1, 2);

        List<Integer> actual = interval.asList();

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest(name = "Expect {0} for interval {1} overlapping {2}")
    @MethodSource("overlappingIntervals")
    void givenTwoIntervalsOverlapping_whenMerge_thenExtendInterval(Interval<Integer> expected, Interval<Integer> left, Interval<Integer> right) {
        Interval<Integer> actual = Interval.merge(left, right);

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest(name = "Expect 0 for interval {1} overlapping {2}")
    @MethodSource("overlappingIntervals")
    void givenTwoIntervalsOverlapping_whenCompareTo_thenReturnZero(Interval<Integer> ignored, Interval<Integer> interval, Interval<Integer> other) {
        int actual = interval.compareTo(other);

        assertThat(actual).isEqualTo(0);
    }

    @ParameterizedTest(name = "Expect {0} for interval {1} not overlapping {2}")
    @MethodSource("notOverlappingIntervals")
    void givenTwoIntervalsNotOverlapping_whenCompareTo_thenReturnZero(int expected, Interval<Integer> interval, Interval<Integer> other) {
        int actual = interval.compareTo(other);

        assertThat(actual).isEqualTo(expected);
    }

}