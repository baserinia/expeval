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
//  A generic linked-list-based stack
//
//******************************************************************************

package expeval;

import java.util.Scanner;

public class Stack<Item> {
   private Node<Item> head; // head of stack
   private int size;      // size of the stack

   // private linked list to store the stack
   private static class Node<Item> {
      private Item item; // reference to an item
      private Node<Item> next; // reference to next node
   }

   // Constructor to initialize an empty stack
   public Stack() {
      head = null;
      size = 0;
   }

   // Returns true if this stack is empty.
   public boolean isEmpty() { return (head == null); }

   // Returns the number of items in this stack.
   public int size() { return size; }

   // Put a new item into the stack.
   public void push(Item item) {
      Node<Item> oldHead = head;
      head = new Node<>();
      head.item = item;
      head.next = oldHead;
      size++;
   }

   // Remove and return the latest item from the stack.
   public Item pop() {
      if (isEmpty()) return null;
      Item item = head.item;
      head = head.next;
      size--;
      return item;
   }

   // Return (but do not remove) the latest item from the stack.
   public Item peek() {
      if (isEmpty()) return null;
      return head.item;
   }

   // Return a string representation of the stack.
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
      Stack<String> stack = new Stack<>();
      System.out.print("Enter String (quit to exit):\n");
      Scanner scanner = new Scanner(System.in);
      while (true) {
         System.out.print("> ");
         String str = scanner.nextLine();
         if (str.equals("quit")) break;
         if (!str.equals("pop")) {
            stack.push(str);
         } else if (!stack.isEmpty()) {
            stack.pop();
         }
         System.out.println("Stack: " + stack.toString());
      }
      System.out.println("(" + stack.size() + " items left on stack)");
   }
}



