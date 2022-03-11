package deque;

import java.util.Iterator;

public class ArrayDeque<T> {
    private int size;
    private int firstItemNextIndex;
    private int lastItemNextIndex;
    private T[] arr;

    //不变量: first和last相同说明数组只剩一个空位了。size==arr.lenth说明满了,此时有可能last>first(此时first和last都在中间)，有可能也是last<first(此时first在最后一个索引.last在第一个索引)

    public ArrayDeque() {
        arr = (T[]) new Object[8];
        size = 0;
        firstItemNextIndex = 0;
        lastItemNextIndex = 1;
    }

    //深拷贝
    public ArrayDeque(ArrayDeque other) {
        arr = (T[]) new Object[other.arr.length];
        size = other.size;
        for (int i = 0; i < other.arr.length; i++) {
            arr[i] = (T) other.arr[i];
        }
        firstItemNextIndex = other.firstItemNextIndex;
        lastItemNextIndex = other.lastItemNextIndex;
    }

    private void resize(int capacity) {
        T[] tmp = arr;
        arr = (T[]) new Object[capacity];
        size = 0;

        int oldfirst = firstItemNextIndex;
        int oldlast = lastItemNextIndex;
        firstItemNextIndex = capacity / 2;
        lastItemNextIndex = firstItemNextIndex + 1;

        //拿到旧数组中的每个元素
        int index = (oldfirst + 1) % tmp.length;  //index为第一个元素的下标
        int check = index;
        int end = oldlast;
        if (oldlast == 0) {
            end = tmp.length;
        }
        //真的很累 想不到怎么总结这段代码，数组是满的时候，index和end是相等的,只能用do while先做一次
        do {
            if (tmp[index] != null)
                addLast(tmp[index]);
            index++;
            index = index % tmp.length;
            if (index == check) //保证一次性的时序输出，当头再次遇到头时就结束。
                break;
        } while (index != end);

        tmp = null;
    }

    private int calcuIndex() {
        return 0;
    }

    public void addFirst(T item) {
        ifEnlarge();
        arr[firstItemNextIndex] = item;
        firstItemNextIndex--;
        if (firstItemNextIndex < 0)
            firstItemNextIndex = arr.length - 1;
        size++;
    }

    public void addLast(T item) {
        ifEnlarge();
        arr[lastItemNextIndex] = item;
        lastItemNextIndex++;
        if (lastItemNextIndex == arr.length)
            lastItemNextIndex = 0;
        size++;
    }

    //删除并返回双端队列第一个结点。如果不存在这样的项目，则返回 null。
    public T removeFirst() {
        if (size == 0)
            return null;
        ifShrink();
        if (firstItemNextIndex == arr.length - 1) {
            firstItemNextIndex = -1;
        }
        firstItemNextIndex++;
        T item = arr[firstItemNextIndex];
        arr[firstItemNextIndex] = null;
        size--;
        return item;
    }

    //删除并返回双端队列最后一个结点。如果不存在这样的项目，则返回 null。
    public T removeLast() {
        if (size == 0)
            return null;
        ifShrink();
        if (lastItemNextIndex == 0) {
            lastItemNextIndex = arr.length;
        }
        lastItemNextIndex--;
        T item = arr[lastItemNextIndex];
        arr[lastItemNextIndex] = null;
        size--;
        return item;
    }

    private void ifEnlarge() {
        if (size == arr.length)
            resize(size * 2);
    }

    private void ifShrink() {
        if (arr.length > 16 && (double) size / (double) arr.length < 0.25)
            resize(arr.length / 2);
    }

    //如果 deque 为空，则返回 true，否则返回 false
    public boolean isEmpty() {
        if (size == 0)
            return true;
        else
            return false;
    }

    public int size() {
        return size;
    }

    //从头到尾打印双端队列中的结点，用空格分隔。打印完所有结点后，打印出一个新行
    private void printDeque1() {
        //打印我发现有六种情况,分别对应了六种不同的遍历方式。。。
        // 1.first>last且数组满了(first在数组尾,last为0，所以令first为0循环一遍数组)
        // 2.first<last且数组满了(first在数组头下标0的位置，last在数组头+1)
        // 3.first>last但数组未满 (先是中间某段到尾，然后是尾到中间
        // 4.first<last但数组未满 (普通情况)
        // 5.first=last，数组只空出一个元素的时候(这时候first和last可能同时在数组头和数组尾,输出方式是 从first开始只输出一次)
        if (firstItemNextIndex < lastItemNextIndex && size != arr.length) {
            for (int i = firstItemNextIndex + 1; i < lastItemNextIndex; i++) {
                System.out.print(arr[i] + " ");
            }
            System.out.println();
        } else {
            for (int i = (firstItemNextIndex + 1) % arr.length; i < arr.length; i++) {
                if (arr[i] != null)
                    System.out.print(arr[i] + " ");
            }
            if (lastItemNextIndex != firstItemNextIndex && (lastItemNextIndex != 0 || lastItemNextIndex != size - 1))
                for (int i = 0; i < lastItemNextIndex; i++) {
                    System.out.print(arr[i] + " ");
                }
            System.out.println();
        }
    }

    public void printDeque() {
        int index = (firstItemNextIndex + 1) % arr.length;  //index为第一个元素的下标
        int check = index;
        int end = lastItemNextIndex;
        if (lastItemNextIndex == 0) {
            end = arr.length;
        }
        //真的很累 想不到怎么总结这段代码，数组是满的时候，index和end是相等的,只能用do while先做一次
        do {
            if (arr[index] != null)
                System.out.print(arr[index] + " ");
            index++;
            index = index % arr.length;
            if (index == check) //保证一次性的时序输出，当头再次遇到头时就结束。
                break;
        } while (index != end);
        System.out.println();
    }

    //获取给定索引处的结点(0是第一个) 如果不存在这样的结点，则返回 null。不能改变双端队列
    public T get(int index) {
        if (index > size - 1||index<0)
            return null;
        if (size != arr.length && firstItemNextIndex < lastItemNextIndex)
            return arr[firstItemNextIndex + index + 1];
        else {
            if (firstItemNextIndex + index < arr.length)  //想拿first到数组尾之间的元素
                return arr[firstItemNextIndex + 1 + index];
            else   //想拿到0到队列尾之间的元素
                return arr[firstItemNextIndex + 1 + index - arr.length];
        }
    }

    //    public Iterator<T> iterator(){
//        return
//    }

//    public boolean equals(Object o) {
//
//    }

}
