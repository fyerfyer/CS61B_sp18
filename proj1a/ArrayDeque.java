public class ArrayDeque<T> {

    private T[] items;
    private int size;

    private int length;

    private int prev;

    private int last;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        length = 8;
        prev = 4;
        last = 4;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private int stepForward(int index) {
        if (index == 0) {
            return length - 1;
        }
        return index - 1;
    }

    private int stepBackward(int index, int module) {
        index %= module;
        if (index == module - 1) {
            return 0;
        }
        return index + 1;
    }

    private void larger() {
        T[] newArray = (T[]) new Object[length * 2];
        int ptr1 = prev;
        int ptr2 = length;
        while (ptr1 != last) {
            newArray[ptr2] = items[ptr1];
            ptr1 = stepBackward(ptr1, length);
            ptr2 = stepBackward(ptr2, length * 2);
        }
        prev = length;
        last = ptr2;
        items = newArray;
        length *= 2;
    }

    private void smaller() {
        T[] newArray = (T[]) new Object[length / 2];
        int ptr1 = prev;
        int ptr2 = length / 4;
        while (ptr1 != last) {
            newArray[ptr2] = items[ptr1];
            ptr1 = stepBackward(ptr1, length);
            ptr2 = stepBackward(ptr2, length / 2);
        }
        prev = length / 4;
        last = ptr2;
        items = newArray;
        length /= 2;
    }

    public void addFirst(T item) {
        if (size == length - 1) {
            larger();
        }
        prev = stepForward(prev);
        items[prev] = item;
        size += 1;
    }

    public void addLast(T item) {
        if (size == length - 1) {
            larger();
        }
        items[last] = item;
        last = stepBackward(last, length);
        size += 1;
    }

    public T removeFirst() {
        if (length >= 16 && length / size >= 4) {
            smaller();
        }
        if (size == 0) {
            return null;
        }
        T removeElement = items[prev];
        prev = stepBackward(prev, length);
        size -= 1;
        return removeElement;
    }

    public T removeLast() {
        if (length >= 16 && length / size >= 4) {
            smaller();
        }
        if (size == 0) {
            return null;
        }
        last = stepForward(last);
        size -= 1;
        return items[last];
    }

    public T get(int index) {
        if (index >= size) {
            return null;
        }
        int ptr = prev;
        for (int i = 0; i < index; i++) {
            ptr = stepBackward(ptr, length);
        }
        return items[ptr];
    }

    public void printDeque() {
        int ptr = prev;
        while (ptr != last) {
            System.out.print(items[ptr] + " ");
            ptr = stepBackward(ptr, length);
        }
    }
    
}
