package deque;

public class ArrayDeque<T> implements Deque<T> {
    private int size;
    private int firstItemNextIndex;
    private int lastItemNextIndex;
    private T[] arr;

    //不变量: first和last相同说明数组只剩一个空位了。size==arr.lenth说明满了,此时有可能last>first(此时first和last都在中间)，有可能也是last<first(此时first在最后一个索引.last在第一个索引)
    //要用指针判断队列已满的条件是:  addOne(first)==last

    public ArrayDeque() {
        arr = (T[]) new Object[8];
        size = 0;
        firstItemNextIndex = 0;
        lastItemNextIndex = 1;
    }

    public ArrayDeque(int capacity) {
        arr = (T[]) new Object[capacity];
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
        T[] tmp = (T[]) new Object[capacity];
        //第一次做的时候，是想着从旧数组的第一个元素出发，依次拿到每个元素然后addLast到新数组的中间部分里，直到遇到最后一个元素，结束条件用first和last来判断的，很麻烦。
        //其实只要先拷贝元素到新数组中，最后来指定first和last指针就行。
        int oldindex = addOne(lastItemNextIndex);
        for (int i = 0; i < size; i++) {
            tmp[i] = arr[oldindex];
            oldindex = addOne(oldindex);
        }
        this.arr = tmp;
        firstItemNextIndex = arr.length - 1;
        lastItemNextIndex = size;
    }


    public void addFirst(T item) {
        ifEnlarge();
        arr[firstItemNextIndex] = item;
        firstItemNextIndex = subOne(firstItemNextIndex);
        size++;
    }

    public void addLast(T item) {
        ifEnlarge();
        arr[lastItemNextIndex] = item;
        lastItemNextIndex = addOne(lastItemNextIndex);
        size++;
    }

    //删除并返回双端队列第一个结点。如果不存在这样的项目，则返回 null。
    public T removeFirst() {
        if (size == 0)
            return null;
        ifShrink();
        firstItemNextIndex = addOne(firstItemNextIndex);
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
        lastItemNextIndex = subOne(lastItemNextIndex);
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
        //能把六种情况合并
        int i = addOne(firstItemNextIndex);
        for (int j = 0; j < size; j++) {
            System.out.print(arr[i] + " ");
            i = addOne(i);
        }
        System.out.println();
    }

    //获取给定索引处的结点(0是第一个) 如果不存在这样的结点，则返回 null。不能改变双端队列
    public T get(int index) {
        if (index > size - 1 || index < 0)
            return null;

//        if (size != arr.length && firstItemNextIndex < lastItemNextIndex)
//            return arr[firstItemNextIndex + index + 1];
//        else {
//            if (firstItemNextIndex + index < arr.length)  //想拿first到数组尾之间的元素
//                return arr[firstItemNextIndex + 1 + index];
//            else   //想拿到0到队列尾之间的元素
//                return arr[firstItemNextIndex + 1 + index - arr.length];
//        }

        //改进版:
        //类似地，在get时，也可以使用这个思想,合并了以上三种情况。
        int start = addOne(firstItemNextIndex);
        return arr[(start + index) % arr.length];
    }

    private int addOne(int Index) {
        return (Index + 1) % arr.length;
    }

    private int subOne(int Index) {
        return (Index - 1 + arr.length) % arr.length;
    }


}
