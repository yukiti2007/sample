package scheduled;

import org.springframework.scheduling.annotation.Scheduled;

public class Task1 {
    @Scheduled(cron = "0/5 * * * * ?")
    protected void doTask1() throws Exception {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " is running task1 @" + i + "times");
            Thread.sleep(3000);
        }
        System.out.println(Thread.currentThread().getName() + " task1 is finished.");
    }
}
