package deque;

public class LinkedListDeque<T> {

    //内部类,表示单个结点
    public class StuffNode {
        StuffNode prev;
        T item;
        StuffNode next;

        //这个构造函数专门用来构造哨兵结点的,它创建之后才有地址，在构造函数里面无法设置自己指向自己，因为地址还没出来
        public StuffNode(T i) {
            item = i;
        }

        public StuffNode(T i, StuffNode prev, StuffNode next) {
            this.prev = prev;
            this.next = next;
            item = i;
        }
    }

    //哨兵结点
    private StuffNode sentinel;
    private int size;

    //空构造,构造一个循环哨兵链表
    public LinkedListDeque() {
        sentinel = new StuffNode(null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    //深拷贝
    public LinkedListDeque(LinkedListDeque other) {
        this();
        StuffNode p=other.sentinel.next;
        while (p!=other.sentinel){
            addLast(p.item);
            p=p.next;
            size++;
        }
    }

    //O(1)操作
    public void addFirst(T item) {
        // 1.写出正常情况的头插代码逻辑
        // 2.这正常情况的逻辑代码恰好也正确维护了 空链表加第一个结点(也是最后一个结点)哨兵结点的prev域和next域。
        size++;
        sentinel.next = new StuffNode(item, sentinel, sentinel.next);
        sentinel.next.next.prev = sentinel.next;
    }

    //O(1)操作
    public void addLast(T item) {
        //1.写出正常情况的尾插代码逻辑
        //2.恰好适配 空链表尾插(同时也是头插)的情况
        size++;
        sentinel.prev.next = new StuffNode(item, sentinel.prev, sentinel);
        sentinel.prev = sentinel.prev.next;
    }

    //O(1)操作。删除并返回双端队列第一个结点。如果不存在这样的项目，则返回 null。
    public T removeFirst() {
        if (sentinel.next == sentinel)
            return null;

        //1.写出正常情况的头删代码逻辑
        //2.恰好适配 只有一个结点的头删
        size--;
        StuffNode first = sentinel.next;
        sentinel.next = first.next;
        sentinel.next.prev = sentinel;
        return first.item;
    }

    //O(1)操作。删除并返回双端队列最后一个结点。如果不存在这样的项目，则返回 null。
    public T removeLast() {
        if (sentinel.prev == sentinel)
            return null;

        //1.写出正常情况的尾删代码逻辑
        //2.恰好适配 只有一个结点的尾删
        size--;
        StuffNode last = sentinel.prev;
        sentinel.prev = last.prev;
        sentinel.prev.next = sentinel;
        return last.item;
    }

    //如果 deque 为空，则返回 true，否则返回 false
    public boolean isEmpty() {
        if (sentinel.prev == sentinel&&sentinel.next==sentinel)
            return true;
        else
            return false;
    }

    public int size() {
        return size;
    }

    //从头到尾打印双端队列中的结点，用空格分隔。打印完所有结点后，打印出一个新行
    public void printDeque() {
        StuffNode p = sentinel.next;
        while (p != sentinel) {
            System.out.println(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    //获取给定索引处的结点(0是第一个) 如果不存在这样的结点，则返回 null。不能改变双端队列
    public T get(int index) {
        int cnt = 0;
        StuffNode p = sentinel.next;
        while (p != sentinel) {
            if (cnt == index)
                return p.item;
            cnt++;
            p = p.next;
        }
        return null;
    }

    public T getRecursive(int index) {
        StuffNode p = sentinel.next;
        return realGetRecursive(index, p);
    }

    public T realGetRecursive(int index, StuffNode p) {
        if (p != sentinel && index == 0) {
            return p.item;
        } else if (p != sentinel && index > 0)
            return realGetRecursive(index - 1, p.next);
        else
            return null;
    }

//    public boolean equals(Object o){
//
//    }
}
