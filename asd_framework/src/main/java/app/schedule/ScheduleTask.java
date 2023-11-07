package app.schedule;

import java.lang.reflect.Method;
import java.util.TimerTask;

public class ScheduleTask extends TimerTask {

    private Method method;
    private Object instance;
    private Object[] params;

    public ScheduleTask(Method method, Object instance, Object[] params) {
        this.method = method;
        this.instance = instance;
        this.params = params;
    }

    @Override
    public void run() {
        try {
            if (this.params != null && this.params.length > 0) {
                this.method.invoke(instance, params);
            } else {
                this.method.invoke(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
