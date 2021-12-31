package DataStructures;

import java.util.Arrays;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Created by Jacks on 08/20/17.
 * A custom ArrayList.
 */

public class ArrayList<E> implements Iterable<E> {

    public static final int DEFAULT_CAPACITY = 100;
    private E[] elementData;
    private int size;

    public ArrayList() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public ArrayList(int capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException("capacity: " + capacity);
        elementData = (E[]) new Object[capacity];
        size = 0;
    }

    public void add(E value) {
        ensureCapacity(size + 1);
        elementData[size] = value;
        size++;
    }

    public void add(int index, E value) {
        ensureCapacity(size + 1);
        for (int i = size; i > index; i--) {
            elementData[i] = elementData[i - 1];
        }
        elementData[index] = value;
        size++;
    }

    @SuppressWarnings("unchecked")
    public void addAll(ArrayList<E> other) {
        ensureCapacity(size + other.size);
        int initSize = other.size;
        for (int i = 0; i < initSize; i++)
            add(other.elementData[i]);
    }

    public void remove(int index) {
        checkIndex(index);
        for (int i = index; i < size - 1; i++)
            elementData[i] = elementData[i + 1];
        size--;
        elementData[size] = null;
    }

    public void removeAll(E target) {
        int i = 0;
        for (; i < size; i++)
            if (elementData[i].equals(target)) {
                remove(i);
            }
        for (i = size; i < elementData.length; i++)
            elementData[i] = null;
    }

    public void set(int index, E value) {
        checkIndex(index);
        elementData[index] = value;
    }

    public void clear() {
        for (int i = 0; i < size; i++)
            elementData[i] = null;
        size = 0;
    }

    public E get(int index) {
        checkIndex(index);
        return elementData[index];
    }

    public int size() {
        return size;
    }

    public int indexOf(E target) {
        for (int i = 0; i < size; i++)
            if (elementData[i].equals(target))
                return i;
        return -1;
    }

    public boolean contains(E target) {
        return indexOf(target) >= 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity(int capacity) {
        if (capacity > elementData.length) {
            int newCapacity = elementData.length * 2 + 1;
            if (capacity > newCapacity)
                newCapacity = capacity;
            elementData = Arrays.copyOf(elementData, newCapacity);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index: " + index);
        }
    }

    private void checkCapacity(int capacity) {
        if (capacity > elementData.length) {
            throw new IllegalStateException("exceeds list capacity");
        }
    }

    public String toString() {
        String array = "[";
        if (size != 0)
            array += elementData[0];
        for (int i = 1; i < size; i++)
            array += ", " + elementData[i];
        array += "]";
        return array;
    }

    // vvv Programming Project methods vvv //

    public void addAll(int index, ArrayList<E> list) {
        for (int i = 0; i < list.size; i++)
            add(list.elementData[i]);
    }

    public boolean containsAll(ArrayList<E> list) {
        for (int i = 0; i < list.size; i++) {
            for (int j = 0; j < size; j++)
                if (list.elementData[i] == elementData[j])
                    continue;
                return false;
        }
        return true;
    }

    public boolean equals(Object o) {
        if (o instanceof ArrayList && ((ArrayList) o).size == size) {
            for (int i = 0; i < size; i++)
                if (!elementData[i].equals(((ArrayList) o).elementData[i]))
                    return false;
            return true;
        } else {
            return false;
        }
    }

    public int lastIndexOf(Object o) {
        int i;
        for (i = size - 1; i >= 0; i--)
            if (o.equals(elementData[i]))
                break;
        return i;
    }

    public boolean remove(Object o) {
        for (int i = 0; i < size; i++)
            if (elementData[i].equals(o)) {
                remove(i);
                return true;
            }
        return false;
    }

    public void removeAll(ArrayList<E> list) {
        for (int i = 0; i < list.size; i++)
            for (int j = 0; j < size; j++) {
                if (elementData[j].equals(list.elementData[i])) {
                    remove(j);
                    j--;
                }
            }
    }

    public void retainAll(ArrayList<E> list) {
        outerloop:
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < list.size; j++) {
                if (elementData[i].equals(list.elementData[j]))
                    continue outerloop;
            }
            remove(i);
        }
    }

    public ArrayList<E> subList(int fromIndex, int toIndex) {
        return new Sublist(fromIndex, toIndex);
    }

    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    public ListIterator<E> iterator() {
        return new ArrayListIterator();
    }

    private class ArrayListIterator implements ListIterator<E> {
        private double removeOK;
        private int position;
        private ArrayList<E> list;

        public ArrayListIterator() {
            this.list = ArrayList.this;
            position = 0;
        }

        public boolean hasNext() {
            return position < size();
        }

        public E next() {
            if (!hasNext())
                throw new NoSuchElementException();
            position++;
            removeOK = -1;
            return list.get(position - 1);
        }

        public boolean hasPrevious() {
            return position != 0;
        }

        public E previous() {
            if (!hasPrevious())
                throw new NoSuchElementException();
            position--;
            removeOK = 0.5;
            return list.get(position);
        }

        public int nextIndex() {
            return position;
        }

        public int previousIndex() {
            return position - 1;
        }

        public void remove() {
            if (removeOK == 0)
                throw new IllegalStateException();
            ArrayList.this.remove(position + (int) removeOK);
            if (removeOK == -1)
                position--;
            removeOK = 0;
        }

        public void set(E e) {
            if (removeOK == 0)
                throw new IllegalStateException();
            ArrayList.this.set(position + (int) removeOK, e);
        }

        public void add(E e) {
            ArrayList.this.add(position, e);
            position++;
        }
    }

    private class Sublist extends ArrayList<E> {
        private E[] subData;
        private int size;

        public Sublist(int fromIndex, int toIndex) {
            for (int i = fromIndex; i < toIndex; i++)
                subData[i - fromIndex] = ArrayList.this.elementData[i];
            size = 100;
        }

        public int size() {
            return size;
        }

        public E get(int index) {
            return subData[index];
        }

        public void set(int index, E e) {
            subData[index] = e;
        }

        public void add(E e) {
            ensureCapacity(size);
            subData[size] = e;
            size++;
        }

        public void add(int index, E e) {
            ensureCapacity(size);
            for (int i = size; i > index; i--) {
                subData[i] = subData[i - 1];
            }
            subData[index] = e;
        }

        public void remove(int index) {
            super.remove(index);
            super.size--;
        }

        public void clear() {
            super.clear();
            super.size--;
        }

        private void ensureCapacity(int capacity) {
            if (capacity > subData.length) {
                int newCapacity = subData.length * 2 + 1;
                if (capacity > newCapacity)
                    newCapacity = capacity;
                elementData = Arrays.copyOf(elementData, newCapacity);
            }
        }
    }


}