package ren.wenchao.jschema;

import java.util.Map;

/**
 * Useful as key of {@link Map}s when traversing two schemas at the same time
 * and need to watch for recursion.
 */
public  class SeenPair {
    private Object s1;
    private Object s2;

    public SeenPair(Object s1, Object s2) {
        this.s1 = s1;
        this.s2 = s2;
    }

    public boolean equals(Object o) {
        if (!(o instanceof SeenPair))
            return false;
        return this.s1 == ((SeenPair) o).s1 && this.s2 == ((SeenPair) o).s2;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(s1) + System.identityHashCode(s2);
    }
}
