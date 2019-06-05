package com.n33.jcu.collections.list;


/**
 * 学并发包先学主要数据结构
 *
 * @author N33
 * @date 2019/6/5
 */
public class LinkedList<E> {

    private Node<E> first;

    private final Node<E> NULL = (Node<E>) null;

    private final static String PLAIN_NULL = "null";

    private int size;

    public LinkedList() {
        this.first = null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return (size() == 0);
    }


    private static <E> LinkedList<E> of(E... elements) {
        final LinkedList<E> list = new LinkedList<>();
        if (elements.length != 0) {
            for (E ele : elements) {
                list.addFirst(ele);
            }
        }
        return list;
    }


    private LinkedList<E> addFirst(E e) {
        final Node<E> newNode = new Node<>(e);
        newNode.next = first;
        this.size++;
        this.first = newNode;
        return this;
    }


    public boolean contains(E e) {
        Node<E> current = first;
        while (current != null) {
            if (current.value == e) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public E removeFirst() {
        /**
         * also return the NULL always when the linked list is empty
         */
        if (this.isEmpty()) throw new NoElementException("The linked list is empty");

        Node<E> node = first;
        first = node.next;
        size--;
        return node.value;

    }

    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "[]";
        } else {
            StringBuilder builder = new StringBuilder("[");
            Node<E> current = first;
            while (current != null) {
                builder.append(current.toString()).append(",");
                current = current.next;
            }
            builder.replace(builder.length() - 1, builder.length(), "]");
            return builder.toString();
        }
    }

    static class NoElementException extends RuntimeException {
        public NoElementException(String message) {
            super(message);
        }
    }


    private static class Node<E> {
        E value;
        Node<E> next;

        public Node(E value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return null != value ? value.toString() : PLAIN_NULL;
        }
    }


    public static void main(String[] args) {

        final LinkedList<String> list = LinkedList.of("Hello", "World", "Scala", "Java", "Thread");

        list.addFirst("Concurrency").addFirst("Test");

        System.out.println(list.size());
        System.out.println(list.contains("Scala"));
        System.out.println(list.contains("1"));
        System.out.println("======================");
        System.out.println(list);

        while (!list.isEmpty()) {
            System.out.println(list.removeFirst());
        }

        System.out.println(list.size());
        System.out.println(list.isEmpty());
    }


}
