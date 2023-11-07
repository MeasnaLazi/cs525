package app.model;

import java.lang.reflect.Method;

public class ProceedingJoinPoint {
    private Object targetInstance;
    private Method targetMethod;
    private Object[] targetArgs;
    private JoinPoint joinPoint;

    public ProceedingJoinPoint(Object instance, Method method, Object[] targetArgs, JoinPoint joinPoint) {
        this.targetInstance = instance;
        this.targetMethod = method;
        this.targetArgs = targetArgs;
        this.joinPoint = joinPoint;
    }

    public JoinPoint getJoinPoint() {
        return joinPoint;
    }

    public void proceed() {
        try {
            this.targetMethod.invoke(this.targetInstance, this.targetArgs);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Object getTargetInstance() {
        return targetInstance;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getTargetArgs() {
        return targetArgs;
    }
}
