package cn.edu.sustech;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

class AA {
    @Inject
    private int field;

    public AA(BB bb, CC cc) {
    }

    public AA(int field) {

    }

    public int getField() {
        return field;
    }
}

class BB {
}

class CC {
}

public class Demo {
    public static void main(String[] args) {
        try {
            BB bObject = (BB) BB.class.getDeclaredConstructor().newInstance();
            CC cObject = (CC) CC.class.getDeclaredConstructor().newInstance();

            //get all constructors from AA
            Constructor[] constructorsA = AA.class.getConstructors();
            Arrays.stream(constructorsA).forEach(System.out::println);

            //get all parameter types from constructor public AA(BB bb, CC cc)
            Class[] fieldTypes = constructorsA[0].getParameterTypes();
            Arrays.stream(fieldTypes).forEach(System.out::println);

            //Why need to convert Class[] type to Object[] type?
            Object[] objects = new Object[]{bObject, cObject};

            //how to instantiate AA?
            AA aObject = (AA) AA.class.getDeclaredConstructors()[0].newInstance(objects);

            //How to inject value into a field?
            Field field = aObject.getClass().getDeclaredField("field");
            if (field.getAnnotation(Inject.class) != null) {
                boolean access = field.canAccess(aObject);
                field.setAccessible(true);
                field.set(aObject, 1);
                field.setAccessible(false);
            }
            System.out.println(aObject.getField());

            //Now you can feel relax finishing this assignment!

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
