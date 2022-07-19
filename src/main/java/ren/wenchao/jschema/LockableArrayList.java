package ren.wenchao.jschema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/*
 * This class keeps a boolean variable <tt>locked</tt> which is set to
 * <tt>true</tt> in the lock() method. It's legal to call lock() any number of
 * times. Any lock() other than the first one is a no-op.
 *
 * This class throws <tt>IllegalStateException</tt> if a mutating operation is
 * performed after being locked. Since modifications through iterator also use
 * the list's mutating operations, this effectively blocks all modifications.
 */
class LockableArrayList<E> extends ArrayList<E> {
    private static final long serialVersionUID = 1L;
    private boolean locked = false;

    public LockableArrayList() {
    }

    public LockableArrayList(int size) {
        super(size);
    }

    public LockableArrayList(List<E> types) {
        super(types);
    }

    public LockableArrayList(E... types) {
        super(types.length);
        Collections.addAll(this, types);
    }

    public List<E> lock() {
        locked = true;
        return this;
    }

    private void ensureUnlocked() {
        if (locked) {
            throw new IllegalStateException();
        }
    }

    @Override
    public boolean add(E e) {
        ensureUnlocked();
        return super.add(e);
    }

    @Override
    public boolean remove(Object o) {
        ensureUnlocked();
        return super.remove(o);
    }

    @Override
    public E remove(int index) {
        ensureUnlocked();
        return super.remove(index);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        ensureUnlocked();
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        ensureUnlocked();
        return super.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        ensureUnlocked();
        return super.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        ensureUnlocked();
        return super.retainAll(c);
    }

    @Override
    public void clear() {
        ensureUnlocked();
        super.clear();
    }
}