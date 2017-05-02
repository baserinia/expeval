//******************************************************************************
//
// Copyright (c) 2017, Amir Baserinia (www.baserinia.com)
//
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice appear in all copies.
//
// THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
// REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY
// AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
// INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM
// LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE
// OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
// PERFORMANCE OF THIS SOFTWARE.
//
//******************************************************************************
//
//  A generic linked-list-based queue
//  Each queue item is of type Item.
//
//******************************************************************************

package expeval;

import java.util.Scanner;

public class Queue<Item> {
    private Node<Item> head; // head of queue
    private Node<Item> tail; // tail of queue
    private int size;        // size of the queue

    // private linked list to store the queue
    private static class Node<Item> {
        private Item item; // reference to an item
        private Node<Item> next; // reference to next node
    }

    // Constructor to initialize an empty queue
    public Queue() {
        head = tail = null;
        size = 0;
    }

    // Returns true if this queue is empty.
    public boolean isEmpty() { return (head == null); }

    // Returns the number of items in this queue.
    public int size() { return size; }

    // Put a new item into the queue.
    public void add(Item item) {
        Node<Item> oldTail = tail;
        tail = new Node<Item>();
        tail.item = item;
        tail.next = null;
        if (isEmpty()) {
            head = tail;
        } else {
            oldTail.next = tail;
        }
        size++;
    }

    // Remove and return the latest item from the queue.
    public Item remove() {
        if (isEmpty()) return null;
        Item item = head.item;
        head = head.next;
        if (isEmpty()) tail = null;
        size--;
        return item;
    }

    // Return (but do not remove) the latest item from the queue.
    public Item peek() {
        if (isEmpty()) return null;
        return head.item;
    }

    // Return a string representation of the queue.
    public String toString() {
        StringBuilder s = new StringBuilder();
        Node<Item> node = head;
        while (node != null) {
            s.append(node.item);
            s.append(' ');
            node = node.next;
        }
        return s.toString();
    }

    // Unit test
    public static void main(String[] args) {
        Queue<String> queue = new Queue<String>();
        System.out.print("Enter String (quit to exit):\n");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String str = scanner.nextLine();
            if (str.equals("quit")) break;
            if (!str.equals("pop")) {
                queue.add(str);
            } else if (!queue.isEmpty()) {
                queue.remove();
            }
            System.out.println("Queue: " + queue.toString());
        }
        System.out.println("(" + queue.size() + " items left on queue)");
    }
}



