package ba.unsa.etf.rpr;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Helper class to enable parallel counting of operations in saved results
 *
 * @author ksljivo1
 */

public class ParallelCount extends Thread {
    private char search;
    private Optional<String> combined;
    private long broj;

    public ParallelCount(char search, Optional<String> combined) {
        this.search = search;
        this.combined = combined;
    }

    @Override
    public void run() {
        broj = combined.map(s -> s.chars().filter(c -> c == search)).orElseGet(IntStream::empty).count();
    }

    public long getBroj() {
        return broj;
    }
}
