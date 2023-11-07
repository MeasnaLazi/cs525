package app.service;

import app.annotation.Async;
import app.annotation.Autowired;
import app.annotation.Profile;
import app.annotation.Service;
import observer.ApplicationEventPublisher;

@Service(name = "settingService")
@Profile(value = "production")
public class SettingService implements ISettingService {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    @Async
    public void print() {
        System.out.println("Setting : print...");
    }

    public ApplicationEventPublisher getPublisher() {
        return publisher;
    }
}
