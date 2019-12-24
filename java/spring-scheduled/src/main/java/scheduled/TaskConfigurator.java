package scheduled;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


@EnableAsync
@Configuration
public class TaskConfigurator implements SchedulingConfigurer, AsyncConfigurer {

//    @Bean("taskScheduler1")
//    public TaskScheduler taskScheduler1() {
//        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
//        taskScheduler.setPoolSize(10);
//        taskScheduler.setThreadNamePrefix("##TaskScheduler1-");
//        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
//        taskScheduler.setAwaitTerminationSeconds(10);
//        taskScheduler.initialize();
//
//        taskScheduler.schedule(new scheduled.Task5(), new CronTrigger("0/5 * * * * ?"));
//
//        return taskScheduler;
//    }
//
//    @Bean("taskScheduler2")
//    public TaskScheduler taskScheduler2() {
//        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
//        taskScheduler.setPoolSize(10);
//        taskScheduler.setThreadNamePrefix("##TaskScheduler2-");
//        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
//        taskScheduler.setAwaitTerminationSeconds(10);
//        taskScheduler.initialize();
//
//        taskScheduler.schedule(new scheduled.Task5(), new CronTrigger("0/5 * * * * ?"));
//
//        return taskScheduler;
//    }
//
    @Bean("TaskExecutorPool1")
    public ThreadPoolTaskExecutor taskExecutor1() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("@@TaskExecutor1-");
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Bean("TaskExecutorPool2")
    public ThreadPoolTaskExecutor taskExecutor2() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("@@TaskExecutor2-");
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Bean
    public Task1 task1() {
        return new Task1();
    }

    @Bean
    public Task2 task2() {
        return new Task2();
    }

    @Bean
    public Task3 task3() {
        return new Task3();
    }

    @Bean
    public Task4 task4() {
        return new Task4();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(Executors.newScheduledThreadPool(5));
    }

    @Override
    public Executor getAsyncExecutor() {
        return Executors.newScheduledThreadPool(5);
    }
}
