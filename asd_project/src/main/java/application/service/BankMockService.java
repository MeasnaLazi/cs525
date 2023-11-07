package application.service;

import app.annotation.Profile;
import app.annotation.Service;

@Service(name = "backMockService")
@Profile(value = "test")
public class BankMockService implements IBankService {
    @Override
    public void deposit(double amount) {
        System.out.println("mock deposit proceeded");
    }

    @Override
    public void withdraw(double amount) {
        System.out.println("mock withdraw proceeded");
    }

    @Override
    public double getAmount() {
        return 0;
    }
}
