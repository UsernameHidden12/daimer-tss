package interval.inplace;

import java.util.List;
import java.util.Objects;

final class Interval<T extends Comparable<T>> implements Comparable<Interval<T>> {

    private final T start;
    private final T end;

    /**
     * Instantiate a closed interval of [start, end].
     * @param start Start of interval (including)
     * @param end End of interval (including)
     * @throws IllegalArgumentException When start > end
     */
    Interval(final T start, final T end) {
        if (start.compareTo(end) > 0) {
            throw new IllegalArgumentException("Start of interval cannot be larger than end of interval");
        }
        this.start = start;
        this.end = end;
    }


    /**
     * Return interval in list representation.
     * @return Returns List.of(start, end)
     */
    List<T> asList() {
        return List.of(start, end);
    }

    /**
     * Return a new interval based on two intervals.
     * @param interval Interval to merge with
     * @param other Other interval to merge with
     * @param <T> Type for intervals
     * @return New interval which has as start min(interval.start, other.start) and as end max(interval.end, other.end)
     */
    static <T extends Comparable<T>> Interval<T> merge(Interval<T> interval, Interval<T> other) {
        T minStart = Interval.getMinStart(interval, other);
        T maxEnd = Interval.getMaxEnd(interval, other);
        return new Interval<>(minStart, maxEnd);
    }

    private static <T extends Comparable<T>> T getMinStart(Interval<T> interval, Interval<T> other) {
        if (interval.start.compareTo(other.start) < 0) {
            return interval.start;
        } else {
            return other.start;
        }
    }

    private static <T extends Comparable<T>> T getMaxEnd(Interval<T> interval, Interval<T> other) {
        if (interval.end.compareTo(other.end) > 0) {
            return interval.end;
        } else {
            return other.end;
        }
    }

    /**
     * Method to compare intervals during merging for an intersection.
     * For disjunctive intervals this returns the natural order based on the start of the intervals.
     * @param other Interval to compare to
     * @return 0 when intervals intersect, -1 when this is before the other, 1 when other interval is before this.
     */
    @Override
    public int compareTo(Interval<T> other) {
        if (intervalsOverlap(other)) {
            return 0;
        }
        return start.compareTo(other.start);
    }

    private boolean intervalsOverlap(Interval<T> other) {
        final boolean thisOverlapToLeft = end.compareTo(other.start) >= 0;
        final boolean thisOverlapToRight = other.end.compareTo(start) >= 0;
        return thisOverlapToLeft && thisOverlapToRight;
    }

    /**
     * Implemented to allow using `assertThat()` from assertJ. Does a deep equality comparison.
     * @param o Other interval
     * @return Deep equality of intervals.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interval<?> interval = (Interval<?>) o;
        return start.equals(interval.start) && end.equals(interval.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    /**
     * Implemented to pretty-print test namings
     * @return String representation of interval
     */
    @Override
    public String toString() {
        return String.format("[%s, %s]", start, end);
    }

}
