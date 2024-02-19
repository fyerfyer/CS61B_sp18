public class LinkedListDeque<T> {
    private class Node {
        private T item;
        private Node prev;
        private Node next;

        public Node(T item, Node prev, Node next) {
            this.item=item;
            this.prev=prev;
            this.next=next;
        }
    } 

    private Node sentinal;
    private int size;

    public LinkedListDeque() {
        sentinal = new Node(null, null, null);
        sentinal.prev = sentinal;
        sentinal.next = sentinal;
        size = 0;
    }

    public void addFirst(T item) {
        Node newNode = new Node(item, sentinal, sentinal.next);
        sentinal.next.prev = newNode;
        sentinal.next = newNode;
        size += 1;
    }

    public void addLast(T item) {
        Node newNode = new Node(item, sentinal.prev, sentinal);
        sentinal.prev.next = newNode;
        sentinal.prev = newNode;
        size += 1;
    }

    public T removeFirst() {
        if(isEmpty()) {
            return null;
        }

        Node removeNode = sentinal.next;
        sentinal.next.next.prev = sentinal;
        sentinal.next = sentinal.next.next;
        size -= 1;
        return removeNode.item;
    }

    public T removeLast() {
        if(isEmpty()) {
            return null;
        }

        Node removeNode = sentinal.prev;
        sentinal.prev.prev.next = sentinal;
        sentinal.prev = sentinal.prev.prev;
        size -= 1;
        return removeNode.item;
    }

    public void printDeque() {
        if(isEmpty()) {
            return;
        }

        Node newNode = sentinal.next;
        while(newNode != sentinal) {
            System.out.println(newNode.item);
            newNode = newNode.next;
        }
    }

    public T get(int index) {
        if(index >= size) {
            return null;
        }

        Node newNode = sentinal.next;
        while(index > 0) {
            newNode = newNode.next;
            index -= 1;
        }
        return newNode.item;
    }

    public T getRecursive(int index) {
        if(index >= size || index<0) {
            return null;
        }
        
        Node newNode = sentinal.next;
        return getRecursiveHelper(newNode, index);
    }

    private T getRecursiveHelper(Node newNode, int index) {
        if(index == 0) {
            return newNode.item;
        }

        return getRecursiveHelper(newNode.next, index-1);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}