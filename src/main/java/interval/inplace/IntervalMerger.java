package interval.inplace;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class IntervalMerger<T extends Comparable<T>> {

    private final IntervalTree<T> intervalTree;

    IntervalMerger(IntervalTree<T> intervalTree) {
        this.intervalTree = intervalTree;
    }

    /**
     * Service to merge a list of intervals and return them merged and in order.
     */
    public IntervalMerger() {
        this(new IntervalTree<>());
    }

    /**
     * Method to merge an unsorted list of intervals and return them merged in ascending order by start of interval.
     * @param intervals Unordered list of intervals
     * @return Sorted list of merged intervals (ascending by start of interval)
     */
    public List<List<T>> merge(List<List<T>> intervals) {
        return merge(intervals.stream())
                .collect(Collectors.toList());
    }

    Stream<List<T>> merge(Stream<List<T>> intervals) {
        intervals.forEach(intervalTree::insert);
        return intervalTree.getIntervals().stream();
    }

}
