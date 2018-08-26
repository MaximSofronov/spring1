package quoters;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class ProfilingHandlerBeanPostProcessor implements BeanPostProcessor {

    private Map<String, Class> map = new HashMap<String, Class>();

    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        Class<?> beanClass = o.getClass();
        if (beanClass.isAnnotationPresent(Profiling.class)) {
            map.put(s, beanClass);
        }

        return o;
    }

    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        Class beanClass = map.get(s);

        if (beanClass!=null) {
            return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), new InvocationHandler() {
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return null;
                }
            });
        }

        return o;
    }
}
