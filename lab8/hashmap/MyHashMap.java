package hashmap;

import java.util.*;

/**
 * A hash table-backed Map implementation. Provides amortized constant time
 * access to elements via get(), remove(), and put() in the best case.
 * <p>
 * Assumes null keys will never be inserted, and does not resize down upon remove().
 *
 * @author fuzaiyo
 */
public class MyHashMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        HashMap hashMap=new HashMap();
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private static final int INIT_CAPACITY = 16;
    private static final double INIT_LOADFACTOR = 0.75;
    private Collection<Node>[] buckets;
    private HashSet<K> keySet = new HashSet<K>();
    private int tableSize;
    private int itemSize;
    private double loadFactor;


    /**
     * Constructors
     */
    public MyHashMap() {
        this(INIT_CAPACITY,INIT_LOADFACTOR);
    }

    public MyHashMap(int initialSize) {
       this(initialSize,INIT_LOADFACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad     maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.loadFactor= maxLoad;
        tableSize = initialSize;
        itemSize=0;
        buckets=createTable(initialSize);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     * <p>
     * The only requirements of a hash table bucket are that we can:
     * 1. Insert items (`add` method)
     * 2. Remove items (`remove` method)
     * 3. Iterate through items (`iterator` method)
     * <p>
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     * <p>
     * Override this method to use different data structures as
     * the underlying bucket type
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        //我选择用链表作为bucket
        return  (LinkedList<Node>) new LinkedList();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        //泛型数组向下强转
        buckets = (Collection<Node>[]) new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            buckets[i]=createBucket();
        }
        return buckets;
    }

    private class KeysIterator<K> implements Iterator<K> {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public K next() {
            return null;
        }
    }

    @Override
    public void clear() {
        tableSize=INIT_CAPACITY;
        loadFactor=INIT_LOADFACTOR;
        itemSize=0;
        buckets=createTable(tableSize);
    }

    @Override
    public boolean containsKey(K key) {
        return false;
    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public int size() {
        return itemSize;
    }

    @Override
    public void put(K key, V value) {





        //插完再扩容，因为前面的put可能只是更新value值
        if(itemSize/tableSize>=loadFactor)
            resize(tableSize*2);

    }

    private void resize(int newcapacity) {
        tableSize=newcapacity;

    }

    @Override
    public Set<K> keySet() {
        //加入了一个新的Key时，维护这个keySet
        return keySet;
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return new KeysIterator();
    }


}
