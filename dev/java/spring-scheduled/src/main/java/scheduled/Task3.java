package scheduled;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

public class Task3 {
    @Async
    @Scheduled(cron = "0/5 * * * * ?")
    protected void doTask3_1() throws Exception {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " is running task3-1 @" + i + "times");
            Thread.sleep(3000);
        }
        System.out.println(Thread.currentThread().getName() + " task3-1 is finished.");
    }

    @Async
    @Scheduled(cron = "0/5 * * * * ?")
    protected void doTask3_2() throws Exception {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " is running task3-2 @" + i + "times");
            Thread.sleep(3000);
        }
        System.out.println(Thread.currentThread().getName() + " task3-2 is finished.");
    }
}
