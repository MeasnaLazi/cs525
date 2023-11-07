package application.advice;

import app.annotation.After;
import app.annotation.Around;
import app.annotation.Aspect;
import app.annotation.Before;
import app.model.JoinPoint;
import app.model.ProceedingJoinPoint;
import app.service.ProfileService;
import application.service.BankService;

@Aspect
public class BankServiceAdvice {

    @Before(pointcut = "BankService.deposit")
    public void beforeDeposite(JoinPoint joinPoint) {
        double amount =Double.parseDouble(joinPoint.getArgs()[0].toString());
        System.out.println("=> Before call deposit with arg amount: " + amount);
    }

    @After(pointcut = "BankService.deposit")
    public void afterDeposite(JoinPoint joinPoint) {
        double amount =Double.parseDouble(joinPoint.getArgs()[0].toString());
        System.out.println("=> After call deposit with arg amount: " + amount);
    }

    @Around(pointcut = "BankService.withdraw")
    public void aroundWithdraw(ProceedingJoinPoint proceedingJoinPoint) {
        BankService bs = (BankService)proceedingJoinPoint.getTargetInstance();
        System.out.println("Amount before withdraw : " + bs.getAmount());
        proceedingJoinPoint.proceed();
        System.out.println("Amount after withdraw:  " + bs.getAmount());
    }
}
