package application;

import app.annotation.App;
import app.annotation.Autowired;
import app.annotation.EnableAsync;
import app.context.FWApplication;
import app.runnable.Runnable;
import application.service.IBankService;

@App
@EnableAsync
public class Main implements Runnable {
    @Autowired
    private IBankService bankService;

    public static void main(String[] args) {
        FWApplication.run(Main.class);
    }

    @Override
    public void run() {
        bankService.deposit(100);
        System.out.println("----");
        bankService.withdraw(5);
        System.out.println("----");
        System.out.println("Remaining Amount: " + bankService.getAmount());
    }
}