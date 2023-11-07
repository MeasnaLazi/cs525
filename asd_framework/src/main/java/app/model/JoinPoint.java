package app.model;

import java.lang.reflect.Method;

public class JoinPoint {
    private Object instance;
    private Method method;
    private String pointCute;
    private Object[] args;

    public JoinPoint(Object instance, Method method, String pointCute) {
        this.instance = instance;
        this.method = method;
        this.pointCute = pointCute;
    }

    public Object getInstance() {
        return instance;
    }

    public Method getMethod() {
        return method;
    }

    public String getPointCute() {
        return pointCute;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return instance.getClass().getName() + " " + method.getName();
    }
}
