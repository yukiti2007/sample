package scheduled;

import org.springframework.scheduling.annotation.Scheduled;

public class Task2 {

    @Scheduled(cron = "0/5 * * * * ?")
    protected void doTask2_1() throws Exception {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " is running task2-1 @" + i + "times");
            Thread.sleep(3000);
        }
        System.out.println(Thread.currentThread().getName() + " task2-1 is finished.");
    }

    @Scheduled(cron = "0/5 * * * * ?")
    protected void doTask2_2() throws Exception {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " is running task2-2 @" + i + "times");
            Thread.sleep(3000);
        }
        System.out.println(Thread.currentThread().getName() + " task2-2 is finished.");
    }
}
