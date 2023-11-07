package application.service;

import app.annotation.EventListener;
import app.annotation.Service;
import application.event.DepositEvent;

@Service(name = "accountChangeService")
public class AccountChangeService {

    @EventListener
    public void change(DepositEvent event) {
        System.out.println("Receive deposit change: " + event.getDescription());
    }
}
