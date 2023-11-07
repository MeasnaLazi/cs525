package app.context;

import app.annotation.*;
import app.annotation.EventListener;
import app.model.JoinPoint;
import app.proxy.GeneralProxy;
import app.schedule.ScheduleTimer;
import app.util.Utils;
import observer.ApplicationEventPublisher;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class FWContext {

    private static Object appType;
//    private static List<Object> serviceTypes;
    private static List<Object> allTypes = new ArrayList<>();
    private static Map<String, Object> mapTypeHasName = new HashMap<>();
    private static Map<String, List<Object>> mapTypeOfProfiles = new HashMap<>();

    //pub-sub
    private static HashMap<Method, Object> mapMethodObjListener = new HashMap<>();

    private static Properties properties = Utils.getProperties("application.properties");

    private static boolean isProxy = false;
    private static HashMap<Object, Object> mapClassObjAndProxyObj = new HashMap<>();

    private static List<JoinPoint> beforeJoinPoints = new ArrayList<>();
    private static List<JoinPoint> afterJoinPoints = new ArrayList<>();
    private static List<JoinPoint> aroundJoinPoints = new ArrayList<>();

    public void start(Class<?> clazz) {
        try {
            Reflections reflections = new Reflections(clazz.getPackageName());
            scanImpClass(reflections, App.class);

            if (allTypes.size() != 1) {
                return;
            }

            initPubSub();
            scanImpClassAspect(reflections);

            appType = allTypes.get(0);
            isProxy = appType.getClass().isAnnotationPresent(EnableAsync.class) || !reflections.getTypesAnnotatedWith(Aspect.class).isEmpty();

            scanImpClass(reflections, Service.class);
            scanImpClassProfile(reflections);

            performDependencyInjection(allTypes);
            connectPubSubInstance();
            invokeCommandLineRunner();
        } catch (Exception e) {
            if (e.getMessage().equalsIgnoreCase("could not use param ''")) {
                System.out.println("You can not use framework with default package!");
            } else {
                e.printStackTrace();
            }
        }
    }

    private void scanImpClass(Reflections reflections, Class<? extends Annotation> annotation) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Set<Class<?>> types = reflections.getTypesAnnotatedWith(annotation);
        ArrayList<Class<?>> nonDefaultConstructorTypes = new ArrayList<>();

        for (Class<?> type : types) {
            try {
                // Default Constructor
                Object obj = type.getDeclaredConstructor().newInstance();
                createObjOrProxyObj(type, obj);

            } catch (Exception e) {
                nonDefaultConstructorTypes.add(type);
            }
        }

        for (Class<?> type : nonDefaultConstructorTypes) {
            for (Constructor<?> constructor : type.getDeclaredConstructors()) {
                // Only constructor that has @Autowired
                if (constructor.isAnnotationPresent(Autowired.class)) {
                    Object[] params = new Object[constructor.getParameterTypes().length];
                    for (int i = 0; i < constructor.getParameterTypes().length; i++) {
                        params[i] = getBeanOfType(constructor.getParameterTypes()[i], allTypes);
                    }

                    // Constructor has parameter(s)
                    Object obj = constructor.newInstance(params);
                    createObjOrProxyObj(type, obj);
                }
            }
        }
    }

    private void createObjOrProxyObj(Class<?> type, Object obj) {

        // ** if has many interface, so what to do?
        if (isProxy && type.getInterfaces().length > 0) {
            ClassLoader classLoader = type.getInterfaces()[0].getClassLoader();
            Object objProxy =
                    Proxy.newProxyInstance(classLoader,
                            new Class[] { type.getInterfaces()[0] },
                            new GeneralProxy(obj, beforeJoinPoints, afterJoinPoints, aroundJoinPoints));
            mapClassObjAndProxyObj.put(type, objProxy);
            addObjectToListAndMap(type, obj);
            addObjectToListAndMap(type, objProxy);
        } else {
            mapClassObjAndProxyObj.put(type, obj);
            addObjectToListAndMap(type, obj);
        }
    }

    private void scanImpClassProfile(Reflections reflections) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(Profile.class);
        for (Class<?> type : types) {
            for (Annotation ann : type.getAnnotations()) {
                if (ann instanceof Profile) {
                    String value = ((Profile) ann).value();
                    List<Object> objects = mapTypeOfProfiles.get(value);
                    if (objects == null) {
                        objects = new ArrayList<>();
                    }
                    try {
                        Object proxyInstance = mapClassObjAndProxyObj.get(type);
//                        Object instance = getBeanOfType(type, allTypes);
                        objects.add(proxyInstance);
                        mapTypeOfProfiles.put(value, objects);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void scanImpClassAspect(Reflections reflections) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Set<Class<?>> types = reflections.getTypesAnnotatedWith(Aspect.class);
        for (Class<?> type : types) {

            Object instance = type.getDeclaredConstructor().newInstance();
            createObjOrProxyObj(type, instance);

            for (Method method : type.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Before.class) || method.isAnnotationPresent(After.class) || method.isAnnotationPresent(Around.class)) {
                    for (Annotation ann : method.getAnnotations()) {
                        if (ann instanceof Before) {
                            String pointcut = ((Before) ann).pointcut();
                            beforeJoinPoints.add(new JoinPoint(instance, method, pointcut));
                        } else if (ann instanceof  After) {
                            String pointcut = ((After) ann).pointcut();
                            afterJoinPoints.add(new JoinPoint(instance, method, pointcut));
                        } else if (ann instanceof  Around) {
                            String pointcut = ((Around) ann).pointcut();
                            aroundJoinPoints.add(new JoinPoint(instance, method, pointcut));
                        }
                    }
                }
            }
        }
    }

    private void addObjectToListAndMap(Class<?> type, Object obj) {
        allTypes.add(obj);

        if (type.isAnnotationPresent(Service.class)) {
            for (Annotation ann : type.getAnnotations()) {
                if (ann instanceof Service) {
                    String name = ((Service) ann).name();
                    mapTypeHasName.put(name, obj);
                }
            }
        }
    }

    private void performDependencyInjection(List<Object> types) {
        try {
            for (Object serviceImpObj : types) {
                fieldInjection(serviceImpObj);
                setterInjection(serviceImpObj);
                methodInjection(serviceImpObj);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void connectPubSubInstance() {
        Object findApplicationEventPublisher = getBeanOfType(ApplicationEventPublisher.class, allTypes);
        if (findApplicationEventPublisher != null) {
            ((ApplicationEventPublisher)findApplicationEventPublisher).addMapListener(mapMethodObjListener);
        }
    }
    private void invokeCommandLineRunner() {
        try {
            Method runMethod = null;
            for (Method method : appType.getClass().getDeclaredMethods()) {
//                if (method.getDeclaringClass().getSuperclass() == CommandLineRunner.class) {
                if (method.getName().equals("run")) {
                    runMethod = method;
                }
            }

            if (runMethod != null) {
                runMethod.invoke(appType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // we need to scan static package for init class, it is not related to application package
    private void initPubSub() {
        try {
            Reflections reflections = new Reflections("observer");
            scanImpClass(reflections, Service.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fieldInjection(Object serviceImpObj) {
        try {
            for (Field field : serviceImpObj.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    String qualifier = "";

                    // Get Qualifier value
                    if (field.isAnnotationPresent(Qualifier.class)) {
                        for (Annotation annotation : field.getAnnotations()) {
                            if (annotation instanceof Qualifier) {
                                qualifier = ((Qualifier) annotation).value();
                            }
                        }
                    }
                    Class<?> fieldType = field.getType();
                    Object instance;

                    if (qualifier.isEmpty()) {
                        if (fieldType.isInterface() && properties != null && properties.getProperty("profiles.active") != null) {
                            String profile = properties.getProperty("profiles.active");
                            List<Object> objProfiles = mapTypeOfProfiles.get(profile);
                            instance = getBeanOfType(fieldType, objProfiles);
                        } else {
                            instance = mapClassObjAndProxyObj.get(fieldType);//getBeanOfType(fieldType, allTypes);
                        }
                    } else {
                        instance = mapTypeHasName.get(qualifier);
                    }

                    field.setAccessible(true);
                    field.set(serviceImpObj, instance);
                } else if (field.isAnnotationPresent(Value.class)) {
                    String key = "";
                    for (Annotation annotation : field.getAnnotations()) {
                        if (annotation instanceof Value) {
                            key = ((Value) annotation).key();
                            break;
                        }
                    }

                    if (!key.isEmpty() && properties != null) {
                        field.setAccessible(true);
                        field.set(serviceImpObj, properties.get(key));
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setterInjection(Object serviceImpObj) {
        try {
            for (Method method : serviceImpObj.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Autowired.class)) {
                    // Setter method
                    if (method.getReturnType().equals(Void.TYPE) && method.getParameterTypes().length == 1) {

                        String qualifier = "";
                        if (method.isAnnotationPresent(Qualifier.class)) {
                            for (Annotation annotation : method.getAnnotations()) {
                                if (annotation instanceof Qualifier) {
                                    qualifier = ((Qualifier) annotation).value();
                                }
                            }
                        }

                        Object instance;
                        if (qualifier.isEmpty()) {
                            instance = getBeanOfType(method.getParameterTypes()[0], allTypes);
                        } else {
                            instance = mapTypeHasName.get(qualifier);
                        }
                        method.invoke(serviceImpObj, instance);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void methodInjection(Object serviceImpObj) {
        try {
            for (Method method : serviceImpObj.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Scheduled.class)) {
                    if (method.getReturnType().equals(Void.TYPE)) {
                        Object runSchedule = 0;
                        for (Annotation annotation : method.getAnnotations()) {
                            if (annotation instanceof Scheduled) {
                                runSchedule = ((Scheduled) annotation).fixedRate();
                                if ((int)runSchedule == 0) {
                                    runSchedule = ((Scheduled) annotation).cron();
                                }
                            }
                        }
                        ScheduleTimer timer = new ScheduleTimer.Builder()
                                .withMethod(method)
                                .withInstance(serviceImpObj)
                                .runEvery(runSchedule)
                                .build();
                        timer.start();
                    }
                }
                if (method.isAnnotationPresent(EventListener.class)) {
                    if (method.getReturnType().equals(Void.TYPE)) {
                        mapMethodObjListener.put(method, serviceImpObj);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object getBeanOfType(Class<?> interfaceClass, List<Object> types) {
        Object service = null;
        try {
            for (Object theClass : types) {
                if (theClass.getClass().equals(interfaceClass)) {
                    service = theClass;
                } else {
                    Class<?>[] interfaces = theClass.getClass().getInterfaces();
                    for (Class<?> theInterface : interfaces) {
                        if (theInterface.getName().contentEquals(interfaceClass.getName())) {
                            service = theClass;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return service;
    }

   public Object getBeanByName(String name) {
        return mapTypeHasName.get(name);
   }

    public Object getBeanByClass(Class<?> interfaceClass) {
        return getBeanOfType(interfaceClass, allTypes);
    }

}


