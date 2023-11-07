package application.service;

import app.annotation.Profile;
import app.annotation.Service;

@Service(name = "accountService")
@Profile(value = "production")
public class AccountService implements IAccountService {

    @Override
    public void print() {
        System.out.println("account is real.");
    }
}
