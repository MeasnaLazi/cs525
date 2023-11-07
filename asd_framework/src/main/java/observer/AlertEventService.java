package observer;

import app.annotation.EventListener;
import app.annotation.Service;
import app.event.NewInformationEvent;

@Service(name = "alertEventService")
public class AlertEventService {
    @EventListener
    public void  receiveInformation(NewInformationEvent event) {
        System.out.println("AlertEventService's new information receive (" + event.getInformation().getDescription() + ")");
    }
}
