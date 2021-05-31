package cn.edu.sustech;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.HashMap;

public class ContainerImpl implements Container{

    Map<Class<?>, Class<?>> map = new HashMap<>();

    @Override
    public <T> void register(Class<T> serviceType) {
        if(serviceType == null){
            throw new IllegalArgumentException();
        }
        if(serviceType.isInterface()){
            throw new IllegalArgumentException();
        }
        if(Modifier.isAbstract(serviceType.getModifiers())){
            throw new IllegalArgumentException();
        }
        if(serviceType.getConstructors().length > 1){
            throw new IllegalArgumentException();
        }
        map.put(serviceType, serviceType);
    }

    @Override
    public <T> void register(Class<T> serviceType, Class<? extends T> implementationType) {
        if(serviceType == null){
            throw new IllegalArgumentException();
        }
        if(implementationType == null){
            throw new IllegalArgumentException();
        }
        if(implementationType.isInterface()){
            throw new IllegalArgumentException();
        }
        if( Modifier.isAbstract(implementationType.getModifiers())){
            throw new IllegalArgumentException();
        }
        if(implementationType.getConstructors().length > 1){
            throw new IllegalArgumentException();
        }
        map.put(serviceType, implementationType);
    }

    @Override
    public <T> T resolve(Class<T> serviceType) {
        if(serviceType == null){
            throw new IllegalArgumentException();
        }
        Class<T> type = (Class<T>) map.get(serviceType);
        System.out.println(type);
        if(type == null){
            throw new ServiceNotFoundException();
        }
        try {
            Class[] fieldTypes = type.getConstructors()[0].getParameterTypes();
            Object[] objects = new Object[fieldTypes.length];
            for(int i = 0; i < fieldTypes.length; i++){
                objects[i] = resolve(fieldTypes[i]);
            }
            T Object = (T) type.getConstructors()[0].newInstance(objects);
            while(true){
                System.out.println(type);
                Field[] fields = type.getDeclaredFields();
                for(Field f : fields)if(f.getAnnotation(Inject.class) != null) {
                    if (f.canAccess(Object)) {
                        f.set(Object, resolve((f.getType())));
                    } else {
                        f.setAccessible(true);
                        f.set(Object, resolve(f.getType()));
                        f.setAccessible(false);
                    }
                }
                type = (Class<T>) type.getSuperclass();
                if(type == null){
                    break;
                }
            }
            return Object;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
}
