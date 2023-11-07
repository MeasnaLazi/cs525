package application.service;

public interface IBankService {
    void deposit(double amount);
    void withdraw(double amount);

    double getAmount();
}
