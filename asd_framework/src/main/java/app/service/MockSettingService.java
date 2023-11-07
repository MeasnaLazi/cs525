package app.service;

import app.annotation.Autowired;
import app.annotation.Profile;
import app.annotation.Service;
import observer.ApplicationEventPublisher;

@Service(name = "mockSettingService")
@Profile(value = "test")
public class MockSettingService implements ISettingService {

    private ApplicationEventPublisher publisher;
    @Override
    public void print() {
        System.out.println("Mock Setting : print mock...");
    }

    @Override
    public ApplicationEventPublisher getPublisher() {
        return publisher;
    }

    @Autowired
    public void setPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }
}
