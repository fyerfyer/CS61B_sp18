public class ArrayDeque<T> {
    private T[] items;
    private int prevpos;
    private int lastpos;
    private int size;
    private int length;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        prevpos = 0;
        lastpos = 0;
        size = 0;
        length = 8;
    }

    private int stepForward(int index, int module) {
        index %= module;
        if(index == module - 1) {
            return 0;
        }
        return index + 1;
    }

    private int stepBackward(int index) {
        if(index == 0) {
            return length - 1;
        }
        return index - 1;
    }

    private void resizeIfNecessary() {
        if (size == length - 1) {
            Resizea();
        } else if (length >= 16 && size < length * 0.25) {
            Resizeb();
        }
    }

    private void Resizea() {
        T[] newArray = (T[]) new Object[length * 2];
        int ptr1 = prevpos;
        int ptr2 = length;
        while (ptr1 != lastpos) {
            newArray[ptr2] = items[ptr1];
            ptr1 = stepForward(ptr1, length);
            ptr2 = stepForward(ptr2, length * 2);
        }
        prevpos = length;
        lastpos = ptr2;
        items = newArray;
        length *= 2;
    }

    private void Resizeb() {
        T[] newArray = (T[]) new Object[length / 2];
        int ptr1 = prevpos;
        int ptr2 = length / 4;
        while (ptr1 != lastpos) {
            newArray[ptr2] = items[ptr1];
            ptr1 = stepForward(ptr1, length);
            ptr2 = stepForward(ptr2, length / 2);
        }
        prevpos = length / 4;
        lastpos = ptr2;
        items = newArray;
        length /= 2;
    }

    public void addFirst(T item) {
        resizeIfNecessary();
        prevpos = stepBackward(prevpos);
        items[prevpos] = item;
        size += 1;
//        resizeIfNecessary();
    }

    public void addLast(T item) {
        resizeIfNecessary();
        items[lastpos] = item;
        size += 1;
        lastpos = stepForward(lastpos, length);
//        resizeIfNecessary();
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }

        T removeElement = items[prevpos];
        prevpos = stepForward(prevpos, length);
        items[prevpos] = null;
        size -= 1;
        resizeIfNecessary();
        return removeElement;
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        lastpos = stepBackward(lastpos);
        size -= 1;
        T removeElement = items[lastpos];
        resizeIfNecessary();
        return removeElement;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void printDeque() {
        int current = prevpos;
        for (int i = 0; i < size; i += 1) {
            System.out.print(items[current] + " ");
            current = stepForward(current, length);
        }
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        int prev = prevpos;
        for (int i = 0; i < index; i += 1) {
            prev = stepForward(prev, length);
        }
        return items[prev];
    }
}