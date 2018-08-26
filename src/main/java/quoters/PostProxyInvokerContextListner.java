package quoters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Method;

public class PostProxyInvokerContextListner implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ConfigurableListableBeanFactory factory;

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext context = contextRefreshedEvent.getApplicationContext();
        final String[] names = context.getBeanDefinitionNames();

        for (String name : names) {
            final BeanDefinition beanDefinition = factory.getBeanDefinition(name);
            final String originalClassName = beanDefinition.getBeanClassName();
            try {
                final Class<?> originalClass = Class.forName(originalClassName);
                for (Method method : originalClass.getMethods()) {
                    if (method.isAnnotationPresent(PostProxy.class)) {
                        final Object bean = context.getBean(name);
                        final Method currentMethod = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
                        currentMethod.invoke(bean);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
