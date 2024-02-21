import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void testRandomCalls() {
        int time = 100;
        StudentArrayDeque<Integer> stu = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ans = new ArrayDequeSolution<>();
        String error = "";
        while (time > 0) {
            int operation = StdRandom.uniform(4);
            if (operation == 0) {
                int addNum = StdRandom.uniform(1000);
                stu.addFirst(addNum);
                ans.addFirst(addNum);
                error = error + "addFirst(" + addNum + ")\n";
            } else if (operation == 1) {
                int addNum = StdRandom.uniform(1000);
                stu.addLast(addNum);
                ans.addLast(addNum);
                error = error + "addFirst(" + addNum + ")\n";
            } else if (operation == 2) {
                if (!stu.isEmpty()) {
                    int removeStu = stu.removeFirst();
                    int removeAns = ans.removeFirst();
                    error = error + "removeFirst()\n";
                    assertEquals(error, removeStu, removeAns);
                }
            } else {
                if (!stu.isEmpty()) {
                    int removeStu = stu.removeLast();
                    int removeAns = ans.removeLast();
                    error = error + "removeLast()\n";
                    assertEquals(error, removeStu, removeAns);
                }
            }
            time -= 1;
        }
    }
}
