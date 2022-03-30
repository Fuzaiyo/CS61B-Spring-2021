package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private BSTNode root;
    private int size;


    public BSTMap() {
        size = 0;
        root = null;
    }

    private class BSTNode {
        K key;
        V value;
        BSTNode left;
        BSTNode right;

        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    //按照Key的增加顺序打印出BSTMap:  本质是中根遍历
    public void printInOrder() {
        InRootOreder(root);
    }

    //中根次序遍历BST,得到递增顺序的元素
    private void InRootOreder(BSTNode root) {
        if (root == null)
            return;
        InRootOreder(root.left);
        System.out.println("Key:" + root.key.toString() + "  value:" + root.value.toString());
        InRootOreder(root.right);
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key)!=null;
    }

    @Override
    public V get(K key) {
        BSTNode tmp = root;
        tmp = searchKey(tmp, key);
        if (tmp != null)
            return tmp.value;
        else
            return null;
    }

    private BSTNode searchKey(BSTNode root, K key) {
        if (root == null)
            return null;

        int cmp = root.key.compareTo(key);
        if (cmp == 0) {
            return root;
        } else if (cmp < 0)
            return searchKey(root.left, key);
        else
            return searchKey(root.right, key);
    }

    @Override
    public int size() {
        return size;
    }

    private BSTNode putHelper(BSTNode root, BSTNode p) {
        if (root == null) {
            root = p;
            size++;
            return root;
        }

        //如果参数的key比当前结点小，返回-1,去左边插
        int cmp = root.key.compareTo(p.key);
        if (cmp < 0)
            root.left = putHelper(root.left, p);
        //如果参数的key比当前结点大，返回1,去右边插
        else if(cmp>0)
            root.right = putHelper(root.right, p);
        //如果参数的key和当前结点的key相等，更新当前结点的value
        else
            root.value=p.value;

        return root;
    }

    @Override
    public void put(K key, V value) {
        BSTNode p = new BSTNode(key, value);
        root = putHelper(root, p);
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }
}
