package app.proxy;

import app.annotation.Async;
import app.model.JoinPoint;
import app.model.ProceedingJoinPoint;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GeneralProxy implements InvocationHandler {

    private Object target;
    private List<JoinPoint> beforeJoinPoints;
    private List<JoinPoint> afterJoinPoints;
    private List<JoinPoint> aroundJoinPoints;

    public GeneralProxy(Object target, List<JoinPoint> beforeJoinPoints, List<JoinPoint> afterJoinPoints, List<JoinPoint> aroundJoinPoints) {
        this.target = target;
        this.beforeJoinPoints = beforeJoinPoints;
        this.afterJoinPoints = afterJoinPoints;
        this.aroundJoinPoints = aroundJoinPoints;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Object returnValue = null;

        Method targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());

        if (targetMethod.isAnnotationPresent(Async.class)) {
            CompletableFuture.runAsync(() -> {
                try {
//                    System.out.println("method call Asynchronous");
                    if (isNotProceedRound(targetMethod, args)) {
                        proceedPointCuts(beforeJoinPoints, targetMethod, args);
                        targetMethod.invoke(target, args);
                        proceedPointCuts(afterJoinPoints, targetMethod, args);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
//            System.out.println("method call Synchronous");
                if (isNotProceedRound(targetMethod, args)) {
                    proceedPointCuts(beforeJoinPoints, targetMethod, args);
                    returnValue = targetMethod.invoke(target, args);
                    proceedPointCuts(afterJoinPoints, targetMethod, args);
                }
        }

       return returnValue;
    }

    private void proceedPointCuts(List<JoinPoint> joinPoints, Method method, Object[] args) {
        try {
            for (JoinPoint joinPoint : joinPoints) {
                String[] data = joinPoint.getPointCute().split("\\.");
                String[] targetData = target.getClass().getName().split("\\.");

                if (data.length != 2) {
                    return;
                }

                String targetClassName = targetData.length > 0 ? targetData[targetData.length - 1].trim() :  target.getClass().getName();
                String className = data[0].trim();
                String methodName = data[1];

                if (targetClassName.equals(className) && method.getName().equals(methodName)) {
                    joinPoint.setArgs(args);
                    joinPoint.getMethod().invoke(joinPoint.getInstance(), joinPoint);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isNotProceedRound(Method method, Object[] args) {
        try {
            for (JoinPoint joinPoint : aroundJoinPoints) {
                String[] data = joinPoint.getPointCute().split("\\.");
                String[] targetData = target.getClass().getName().split("\\.");

                if (data.length != 2) {
                    return true;
                }

                String targetClassName = targetData.length > 0 ? targetData[targetData.length - 1].trim() :  target.getClass().getName();
                String className = data[0].trim();
                String methodName = data[1];

                if (targetClassName.equals(className) && method.getName().equals(methodName)) {
                    joinPoint.setArgs(args);
                    ProceedingJoinPoint proceedingJoinPoint = new ProceedingJoinPoint(target, method, args, joinPoint);
                    joinPoint.getMethod().invoke(joinPoint.getInstance(), proceedingJoinPoint);

                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
