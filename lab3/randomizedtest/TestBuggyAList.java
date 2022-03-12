package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    // YOUR TESTS HERE
    //1.随机调用测试:一个类的不同方法随机调用
    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();

        int N = 500;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                System.out.println("size: " + size);
            } else if (operationNumber == 2) {
                if (L.size() == 0) {
                    continue;
                }
                System.out.println("last item:  " + L.getLast());

            } else {
                if (L.size() == 0) {
                    continue;
                }
                L.removeLast();
                System.out.println("移除了最后一个元素");
            }
        }
    }

    //2.随机比较测试 拿一个类的每一个方法和已确定的类的方法比较
    @Test
    public void randomizedTest2() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> Bug = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);

            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                Bug.addLast(randVal);
                L.addLast(randVal);
                assertEquals(L.size(), Bug.size());

            } else if (operationNumber == 1) {
                // size
                int Lsize = L.size();
                int Bugsize = L.size();
                assertEquals(Lsize, Bugsize);

            } else if (operationNumber == 2) {
                if (L.size() == 0 || Bug.size() == 0) {
                    continue;
                }
                int Lsize = L.size();
                int Bugsize = L.size();
                assertEquals(L.removeLast(), Bug.removeLast());

            } else {
                if (L.size() == 0 || Bug.size() == 0) {
                    continue;
                }
                assertEquals(L.getLast(), Bug.getLast());
            }
        }
    }
}
