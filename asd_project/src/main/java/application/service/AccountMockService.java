package application.service;

import app.annotation.Profile;
import app.annotation.Service;

@Service(name = "accountService")
@Profile(value = "test")
public class AccountMockService implements IAccountService {

    @Override
    public void print() {
        System.out.println("account is mock.");
    }
}
