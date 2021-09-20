import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CalcTest {
    List<Integer> listArgs = new ArrayList<>();
    String fileName = "test.txt";

    @BeforeEach
    public void prepare() {

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String str;
            while ((str = reader.readLine()) != null) {
                listArgs.add(Integer.parseInt(str));
            }
        } catch (IOException exc) {
            System.out.println(exc);

        }

    }

    @Test
    public void test() throws InterruptedException {
        List<Thread> listThreads = new ArrayList<>();
        for (var elem : listArgs) {
            Thread t = new Thread(() -> {
                Calc calculator = new Calc();
                long l = calculator.calc(elem);
                Thread.yield();
                calculator.print(elem, l);
            });
            t.start();
            listThreads.add(t);
        }
        for (var thr : listThreads) {
            thr.join();
        }
    }

    @Test
    public void testSync() throws InterruptedException {
        List<Thread> listThreads = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            var ref = new Object() {
                Boolean flag = true;
            };
            while (ref.flag) {
                Thread t = new Thread(() -> {
                    String str = null;
                    synchronized (reader) {
                        if (ref.flag) {
                            try {
                                str = reader.readLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else return;
                        if (str == null) {
                            ref.flag = false;
                            return;
                        }
                    }
                    int n = Integer.parseInt(str);
                    Calc calculator = new Calc();
                    long l = calculator.calc(n);
                    Thread.yield();
                    calculator.print(n, l);
                });
                listThreads.add(t);
                t.start();
            }
        } catch (IOException exc) {
            System.out.println(exc);
        }

        for (var thr : listThreads) {
            thr.join();
        }
    }
}
