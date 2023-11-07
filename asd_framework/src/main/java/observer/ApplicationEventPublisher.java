package observer;

import app.annotation.Service;

import java.lang.reflect.Method;
import java.util.HashMap;

@Service(name = "applicationEventPublisher")
public class ApplicationEventPublisher implements IApplicationEventPublisher {
    private HashMap<Method, Object> mapMethodObjListener = new HashMap<>();

    public void addMapListener(HashMap<Method, Object> mapListener) {
        this.mapMethodObjListener.clear();
        this.mapMethodObjListener.putAll(mapListener);
    }

    @Override
    public void publishEvent(Object event) {
        for (Method method : mapMethodObjListener.keySet()) {
            // Allow only valid method that has only one paramater
            if (method.getParameterTypes().length == 1) {
                if (method.getParameterTypes()[0].getName().equals(event.getClass().getName())) {
                   try {
                       method.invoke(mapMethodObjListener.get(method), event);
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
                }
            }
        }
    }
}
