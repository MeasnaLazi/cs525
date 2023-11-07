package observer;

import app.annotation.EventListener;
import app.annotation.Service;
import app.event.NoticeInformationEvent;

@Service(name = "notificationService")
public class NotificationService {
    @EventListener
    public void  receiveInformation(NoticeInformationEvent event) {
        System.out.println("NotificationService's new information receive (" + event.getInformation().getDescription() + ")");
    }
}
