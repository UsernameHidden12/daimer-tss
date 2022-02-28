package interval.inplace;

import java.util.*;
import java.util.stream.Collectors;

final class IntervalTree<T extends Comparable<T>> {

    /**
     * A SortedMap<T, T> is used here as it allows efficient querying, insertion and deletion while also returning the
     * element in question. A SortedSet<T> would be sufficient but makes the implementation a bit more awkward as one
     * cannot get the element queried elegantly. Both approaches are viable. The Map is used for sake of convenience.
     * Important is the requirement of sorting the elements as otherwise the behaviour of Interval<T> is not guaranteed
     * to be well-behaved.
     */
    private final SortedMap<Interval<T>, Interval<T>> intervalTree;

    /**
     * Generate a new interval tree which ensures minimal space usage.
     * @param intervalTree IntervallTree to use for insertion.
     */
    IntervalTree(SortedMap<Interval<T>, Interval<T>> intervalTree) {
        this.intervalTree = intervalTree;
    }

    IntervalTree() {
        this(new TreeMap<>());
    }

    /**
     * Insert single interval into the tree. This method ensures that at the end no overlapping intervals exist by
     * merging those that overlap during insertion.
     * @param interval Interval to add into the tree
     */
    void insert(final List<T> interval) {
        Interval<T> toInsert = new Interval<>(interval.get(0), interval.get(1));
        insert(toInsert);
    }

    private void insert(final Interval<T> interval) {
        Interval<T> toInsert = interval;
        while (intervalTree.containsKey(toInsert)) {
            final Interval<T> other = intervalTree.remove(toInsert);
            toInsert = Interval.merge(toInsert, other);
        }
        intervalTree.put(toInsert, toInsert);
    }

    /**
     * Get current list of disjunctive intervals.
     * @return List of disjunctive intervals, ordered by their start point.
     */
    List<List<T>> getIntervals() {
        return getIntervalList().stream()
                .map(Interval::asList)
                .collect(Collectors.toList());
    }

    private List<Interval<T>> getIntervalList() {
        return new ArrayList<>(intervalTree.keySet());
    }

}
