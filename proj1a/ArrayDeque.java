public class ArrayDeque<T> {
    private T[] items;
    private int prevpos;
    private int lastpos;
    private int size;
    private static int init_capacity = 8;

    public ArrayDeque() {
        items = (T[]) new Object[init_capacity];
        prevpos = 0;
        lastpos = 0;
        size = 0;
    }

    private int stepForward(int index) {
        return (index + 1) % items.length;
    }

    private int stepBackward(int index) {
        return (index - 1 + items.length) % items.length;
    }

    private void resizeIfNecessary() {
        if(size == items.length) {
            Resize(2 * size); 
        } else if(items.length >= 16 && size < items.length * 0.25) {
            Resize(size / 2);
        } 
    }

    private void Resize(int capacity) {
        T[] newArray = (T[]) new Object[capacity];
        int current = stepForward(prevpos);
        for(int i = 0 ; i < size ; i += 1) {
            newArray[i] = items[current];
            current = stepForward(current);
        }
        lastpos = size;
        prevpos = capacity - 1; 
    }

    public void addFirst(T item) {
        resizeIfNecessary();
        items[prevpos] = item;
        size += 1;
        prevpos = stepBackward(prevpos);
        resizeIfNecessary();
    }

    public void addLast(T item) {
        resizeIfNecessary();
        items[lastpos] = item;
        size += 1;
        lastpos = stepForward(lastpos);
        resizeIfNecessary();
    }

    public T removeFirst() {
        prevpos = stepForward(prevpos);
        T removeElement = items[prevpos];
        items[prevpos] = null;
        size -= 1;
        resizeIfNecessary();
        return removeElement;
    }

    public T removeLast() {
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
        int current = stepForward(prevpos);
        for(int i = 0 ; i < size ; i += 1) {
            System.out.println(items[current]);
            current = stepForward(current);
        }
    }

    public T get(int index) {
        int pos = stepForward(prevpos + 1 + index);
        return items[pos];
    }
}
