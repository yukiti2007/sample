package scheduled;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringContextHolder implements ApplicationContextInitializer {

    private static ApplicationContext applicationContext;

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        if (applicationContext == null) {
            applicationContext = configurableApplicationContext;
        }
    }

    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return (T) applicationContext.getBean(clazz);
    }

    public static void cleanApplicationContext() {
        applicationContext = null;
    }
}
