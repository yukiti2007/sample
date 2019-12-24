package scheduled;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

public class Task4 {
    @Async("TaskExecutorPool1")
    @Scheduled(cron = "0/5 * * * * ?")
    protected void doTask4_1() throws Exception {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " is running task4-1 @" + i + "times");
            Thread.sleep(3000);
        }
        System.out.println(Thread.currentThread().getName() + " task4-1 is finished.");
    }

    @Async("TaskExecutorPool2")
    @Scheduled(cron = "0/5 * * * * ?")
    protected void doTask4_2() throws Exception {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " is running task4-2 @" + i + "times");
            Thread.sleep(3000);
        }
        System.out.println(Thread.currentThread().getName() + " task4-2 is finished.");
    }
}
