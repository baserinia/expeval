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
//  A simple implementation of a generic map
//  Note: the map is implemented as a simple binary tree with no balancing
//      mechanism. Please do not use if for large datasets, since it won't
//      scale efficiently.
//
//******************************************************************************

package expeval;

public class Map<Key extends Comparable<Key>, Value> {
    private Entry<Key, Value> root; 
    private int size;

    // private linked list to store the stack
    private static class Entry<Key, Value> {
        private Key key; 
        private Value value; 
        private Entry<Key, Value> left; // reference to left child
        private Entry<Key, Value> right; // reference to right child
    }

    // Constructor to initialize an empty stack
    public Map() {
        root = null;
        size = 0;
    }

    // Returns true if this map is empty
    public boolean isEmpty() { return (root == null); }

    // Returns the number of items in the map
    public int size() { return size; }
    
    // Recursive put; for private use only
    private final void put(Entry<Key, Value> root, Entry<Key, Value> entry) {
        int cmp = entry.key.compareTo(root.key);
        if (cmp == 0) {
            root.value = entry.value;
            System.out.println("inside == " + entry.key + " " + entry.value);
        } if (cmp < 0) {
            if (root.left == null) {
                root.left = entry;
            } else {
                put(root.left, entry);
            }
        } else  {
            if (root.right == null) {
                root.right = entry;
            } else {
                put(root.right, entry);
            }
        }
    }

    // Recursive get; for private use only
    private final Value get(Entry<Key, Value> root, Key key) {
        if (root == null) return null;
        int cmp = key.compareTo(root.key);
        if (cmp == 0) {
            return root.value;
        } else if (cmp < 0) {
            return get(root.left, key);
        } else {
            return get(root.right, key);
        }
    }

    // Recursive containsKey; for private use only
    private final boolean containsKey(Entry<Key, Value> root, Key key) {
        if (root == null) return false;
        int cmp = key.compareTo(root.key);
        if (cmp == 0) {
            return true;
        } else if (cmp < 0) {
            return containsKey(root.left, key);
        } else {
            return containsKey(root.right, key);
        }
    }

    // Put a new item into the stack
    public void put(Key key, Value value) {
        Entry<Key, Value> newEntry = new Entry<>();
        newEntry.key = key;
        newEntry.value = value;
        if (root == null) {
            root = newEntry;
        } else {
            put(root, newEntry);
        }
        size++;
    }

    // return an item from the map
    public Value get(Key key) { return get(root, key); }
    
    // check if a key exists in the map
    public boolean containsKey(Key key) { return containsKey(root, key); }
}



