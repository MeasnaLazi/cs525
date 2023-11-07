package app.service;

import app.annotation.Async;
import observer.ApplicationEventPublisher;

public interface ISettingService {
    void print();

    ApplicationEventPublisher getPublisher();
}
