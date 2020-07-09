package bkushmyruk.homework.hw1_collection.arraydeque;

import java.util.*;

public class ArrayDequeCustom<T> implements Deque<T> {
    /**
     * The array in which the elements of the deque are stored.
     * All array cells not holding deque elements are always null.
     * The array always has at least one null slot (at tail).
     */
    private T[] elements;

    /**
     * The index of the element at the head of the deque (which is the
     * element that would be removed by remove() or pop()); or an
     * arbitrary number 0 <= head < elements.length equal to tail if
     * the deque is empty.
     */
    private int head;

    /**
     * The index at which the next element would be added to the tail
     * of the deque (via addLast(E), add(E), or push(E));
     * elements[tail] is always null.
     */
    private int tail;

    /**
     * The maximum size of array to allocate.
     * Some VMs reserve some header words in an array.
     * Attempts to allocate larger arrays may result in
     * OutOfMemoryError: Requested array size exceeds VM limit
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    public ArrayDequeCustom() {
        elements = (T[]) new Object[1];
    }

    private void growup() {
        final int oldSize = elements.length;
        int newSize = oldSize + 1;
        if (oldSize == MAX_ARRAY_SIZE) {
            throw new IllegalStateException("Sorry, deque too big");
        } else {
            final Object[] el = elements = Arrays.copyOf(elements, newSize);
            if (head != 0) {
                System.arraycopy(el, 0, el, head, oldSize);
            }
        }
    }

    private void growdown(int i) {
        final int oldSize = elements.length;
        int newSize = oldSize - 1;
        final Object[] el = elements;
        if (i == 0) {
            System.arraycopy(el, 1, el, 0, newSize);
        } else {
            System.arraycopy(el, 0, el, 0, newSize);
        }
        if (newSize == 0) {
            elements[0] = null;
        } else {
            elements = Arrays.copyOf(elements, newSize);
        }
    }

    private int nullCount(Object[] el) {
        int c = 0;
        for (int i = 0; i < el.length; i++) {
            if (el[i] == null) {
                c++;
            }
        }
        return c;
    }

    private int isFirstEmpty() {
        if (elements[0] == null) {
            return 1;
        } else {
            return 0;
        }
    }

    private void delete(int i) {
        final Object[] el = elements;
        final int oldSize = elements.length;
        int newSize = oldSize - 1;
        if (i == 0) {
            System.arraycopy(el, 1, elements, 0, newSize);
            elements = Arrays.copyOf(elements, newSize);
        } else if (i == newSize) {
            System.arraycopy(el, 0, elements, 0, i);
            elements = Arrays.copyOf(elements, newSize);
        } else {
            System.arraycopy(el, 0, elements, 0, i);
            System.arraycopy(el, i + 1, elements, i, oldSize - i - 1);
            elements = Arrays.copyOf(elements, newSize);
        }
    }

    @Override
    public void addFirst(T t) {
        if (t == null)
            throw new NullPointerException();
        final Object[] el = elements;
        if (el[0] == null) {
            el[0] = t;
        } else {
            ++head;
            growup();
            elements[--head] = t;
        }
    }


    @Override
    public void addLast(T t) {
        if (t == null)
            throw new NullPointerException();
        final Object[] el = elements;
        tail = el.length - 1;
        if (el[tail] == null) {
            el[tail] = t;
        } else {
            growup();
            elements[++tail] = t;
        }
    }

    @Override
    public boolean offerFirst(T t) {
        addFirst(t);
        return t != null;
    }

    @Override
    public boolean offerLast(T t) {
        addLast(t);
        return t != null;
    }

    @Override
    public T removeFirst() {
        T t = pollFirst();
        if (t == null)
            throw new NoSuchElementException();
        return t;
    }

    @Override
    public T removeLast() {
        T t = pollLast();
        if (t == null)
            throw new NoSuchElementException();
        return t;
    }

    @Override
    public T pollFirst() {
        int i = 0;
        T t = elements[i];
        growdown(i);
        if (t != null) {
            return t;
        } else {
            return null;
        }
    }

    @Override
    public T pollLast() {
        int i = elements.length - 1;
        T t = elements[i];
        growdown(i);
        if (t == null) {
            return null;
        } else {
            return t;
        }
    }

    @Override
    public T getFirst() {
        T t = elements[0];
        if (t == null) {
            throw new NoSuchElementException();
        } else {
            return t;
        }
    }

    @Override
    public T getLast() {
        T t = elements[elements.length - 1];
        if (t == null) {
            throw new NoSuchElementException();
        } else {
            return t;
        }
    }

    @Override
    public T peekFirst() {
        T t = elements[0];
        if (t != null) {
            return t;
        } else {
            return null;
        }
    }

    @Override
    public T peekLast() {
        T t = elements[elements.length - 1];
        if (t != null) {
            return t;
        } else {
            return null;
        }
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        byte br = 0;
        if (o != null) {
            final Object[] el = elements;
            for (int i = 0; i < el.length; i++) {
                if (o.equals(el[i])) {
                    delete(i);
                    ++br;
                    return true;
                }
                if (br != 0) break;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        byte br = 1;
        if (o != null) {
            final Object[] el = elements;
            for (int i = el.length - 1; i > 0; i--) {
                if (o.equals(el[i])) {
                    delete(i);
                    --br;
                    return true;
                }
                if (br == 0) break;
            }
        }
        return false;
    }

    @Override
    public boolean add(T t) {
        addLast(t);
        return true;
    }

    @Override
    public boolean offer(T t) {
        return offerLast(t);
    }

    @Override
    public T remove() {
        return removeFirst();
    }

    @Override
    public T poll() {
        return pollFirst();
    }

    @Override
    public T element() {
        return getFirst();
    }

    @Override
    public T peek() {
        return peekFirst();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        final Object[] coll = c.toArray();
        int counter = 0;
        for (int i = 0; i < coll.length; i++) {
            addFirst((T) coll[i]);
        }
        if (counter == coll.length) {
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        final Object[] coll = c.toArray();
        final Object[] el = elements;
        int size = el.length;
        int counter = 0;
        int a = 0;
        int b = 0;
        if (elements.length == coll.length) {
            for (int i = 0; i < size; i++) {
                a++;
                if (el[i] != coll[i]) {
                    el[i] = null;
                }
            }
            Object[] trans = new Object[size - nullCount(el)];
            for (int i = 0; i < size; i++) {
                b++;
                if (el[i] != null) {
                    trans[counter++] = el[i];
                }
            }
            System.arraycopy(trans, 0, elements, 0, trans.length);
            elements = Arrays.copyOf(elements, trans.length);
            if (a == size | b == size) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        final Object[] coll = c.toArray();
        final Object[] el = elements;
        int size = el.length;
        int counter = 0;
        int a = 0;
        int b = 0;
        if (elements.length == coll.length) {
            for (int i = 0; i < size; i++) {
                a++;
                if (el[i] == coll[i]) {
                    el[i] = null;
                }
            }
            Object[] trans = new Object[size - nullCount(el)];
            for (int i = 0; i < size; i++) {
                b++;
                if (el[i] != null) {
                    trans[counter++] = el[i];
                }
            }
            System.arraycopy(trans, 0, elements, 0, trans.length);
            elements = Arrays.copyOf(elements, trans.length);
            if (a == size | b == size) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        final Object[] el = elements;
        for (int i = 0; i < el.length; i++) {
            growdown(1);
        }
    }

    @Override
    public void push(T t) {
        addLast(t);
    }

    @Override
    public T pop() {
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        final Object[] coll = c.toArray();
        final Object[] el = elements;
        int counter = 0;
        if (el.length == coll.length) {
            for (int i = 0; i < el.length; i++) {
                counter++;
                if (el[i] != coll[i]) {
                    break;
                }
            }
        }
        if (counter == el.length) {
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        byte br = 0;
        if (o != null) {
            final Object[] el = elements;
            for (int i = 0; i < el.length; i++) {
                if (o.equals(el[i])) {
                    ++br;
                    return true;
                }
                if (br != 0) break;
            }
        }
        return false;
    }

    @Override
    public int size() {
        if (isEmpty() == true) {
            return 0;
        } else {
            return elements.length;
        }

    }

    @Override
    public boolean isEmpty() {
        int size = elements.length;
        return isFirstEmpty() == size;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        if (isEmpty() == true) {
            throw new NoSuchElementException();
        } else {
            final Object[] el = elements;
            return el;
        }
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        final Object[] el = elements;
        int size = el.length;
        if (isEmpty() == true) {
            throw new NoSuchElementException();
        } else if ((a.length == 0) || size > a.length) {
            a = Arrays.copyOf(a, size);
            for (int i = 0; i < size; i++) {
                a[i] = (T1) el[i];
            }
        } else if (size == a.length) {
            for (int i = 0; i < size; i++) {
                a[i] = (T1) el[i];
            }
        } else if (size < a.length) {
            for (int i = 0; i < size; i++) {
                a[i] = (T1) el[i];
                a = Arrays.copyOf(a, size);
            }
        }
        return a;
    }

    @Override
    public Iterator<T> descendingIterator() {
        return null;
    }
}

