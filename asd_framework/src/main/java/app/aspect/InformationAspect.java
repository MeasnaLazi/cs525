package app.aspect;

import app.annotation.After;
import app.annotation.Around;
import app.annotation.Aspect;
import app.annotation.Before;
import app.model.JoinPoint;
import app.model.ProceedingJoinPoint;
import app.service.ProfileService;

@Aspect
public class InformationAspect {

    @Before(pointcut = "ProfileService.createInformation")
    public void beforeSetDescription(JoinPoint joinPoint) {
        String info = (String) joinPoint.getArgs()[0];
        System.out.println("beforeSetter called!");
        System.out.println("pointCut: " + info);
    }

    @After(pointcut = "ProfileService.createInformation")
    public void afterSetDescription(JoinPoint joinPoint) {
        String info = (String) joinPoint.getArgs()[0];
        System.out.println("afterSetter called!");
        System.out.println("pointCut: " + info);
    }

    @Around(pointcut = "ProfileService.updateInformation")
    public void aroundAupdateDescription(ProceedingJoinPoint proceedingJoinPoint) {
        System.out.println("Around feature: before called");
//        ProfileService ps = (ProfileService)proceedingJoinPoint.getTargetInstance();
//        System.out.println("Around feature: before called : " + ps.getInformation().getDescription());
        proceedingJoinPoint.proceed();
//        System.out.println("Around feature: after called!: " + ps.getInformation().getDescription());
        System.out.println("Around feature: after called");
    }
}
