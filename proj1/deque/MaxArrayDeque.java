package deque;

import java.util.Comparator;


public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> c;

    public MaxArrayDeque(Comparator<T> c) {
        this.c = c;
    }

    public T max() {
        if (size() == 0)
            return null;
        T max = get(0);
        for (int i = 0; i < size(); i++) {
            if (c.compare(max, get(i)) > 0)
                max = get(i);
        }
        return max;
    }

    public T max(Comparator<T> c) {
        if (size() == 0)
            return null;
        T max = get(0);
        for (int i = 0; i < size(); i++) {
            if (c.compare(max, get(i)) > 0)
                max = get(i);
        }
        return max;
    }
}
