package quoters;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class DeprecetionHandlerBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        for (String name : configurableListableBeanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = configurableListableBeanFactory.getBeanDefinition(name);
            final String beanClassName = beanDefinition.getBeanClassName();
            try {
                final Class<?> beanClass = Class.forName(beanClassName);
                final DeprecatedClass annotation = beanClass.getAnnotation(DeprecatedClass.class);
                if (annotation != null) {
                    beanDefinition.setBeanClassName(annotation.newImpl().getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
