package application.service;

import app.annotation.*;
import observer.ApplicationEventPublisher;
import application.event.DepositEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service(name = "bankService")
@Profile(value = "production")
public class BankService implements IBankService {
    private ApplicationEventPublisher publisher;
    private double amount = 0;
    @Override
    public void deposit(double amount) {
        System.out.println("deposit proceeded");
        this.amount += amount;
        publisher.publishEvent(new DepositEvent("Deposit USD " + amount));
    }

    @Override
//    @Async
    public void withdraw(double amount) {
        System.out.println("withdraw proceeded");
        this.amount -= amount;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    //    @Scheduled(fixedRate = 1000)
//    @Scheduled(cron = "5 0")
//    public void reminder() {
//        Date date = Calendar.getInstance().getTime();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//        String currentTime = df.format(date);
//        System.out.println("This task run at " + currentTime);
//    }

    @Autowired
    public void setPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }
}
