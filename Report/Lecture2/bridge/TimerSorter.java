package Report.Lecture2.bridge;

public class TimerSorter extends Sorter {
    public TimerSorter(SortImple sortImple) {
        super(sortImple);
    }

    public void timerSort(Object obj[]) {
        long start = System.currentTimeMillis();
        sort(obj);
        long end = System.currentTimeMillis();
        System.out.println("time:" + (end - start));
    }
}
