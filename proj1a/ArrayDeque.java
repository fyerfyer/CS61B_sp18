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
        return (index + 1) % module;
    }

    private int stepBackward(int index) {
        return (index - 1 + length) % length;
    }

    private void resizeIfNecessary() {
        if(size == length) {
            Resize(2 * length);
        } else if(length >= 16 && size < length * 0.25) {
            Resize(length / 2);
        } 
    }

    private void Resize(int capacity) {
        T[] newArray = (T[]) new Object[capacity];
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

    public void addFirst(T item) {
        resizeIfNecessary();
        items[prevpos] = item;
        size += 1;
        prevpos = stepBackward(prevpos);
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
        if(isEmpty()) {
            return null;
        }

        prevpos = stepForward(prevpos, length);
        T removeElement = items[prevpos];
        items[prevpos] = null;
        size -= 1;
        resizeIfNecessary();
        return removeElement;
    }

    public T removeLast() {
        if(isEmpty()) {
            return null;
        }

        lastpos = stepBackward(lastpos);
        T removeElement = items[lastpos];
        items[lastpos] = null;
        size -= 1;
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
        int current = stepForward(prevpos, length);
        for(int i = 0 ; i < size ; i += 1) {
            System.out.println(items[current]);
            current = stepForward(current, length);
        }
    }

    public T get(int index) {
        if(index < 0 || index >= size) {
            return null;
        }

        int getIndex = (prevpos + index + 1) % length;
        return items[getIndex];
    }

}
