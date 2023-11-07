package app.event;

import app.model.Information;

public class NoticeInformationEvent {
    private Information information;

    public NoticeInformationEvent(String description) {
        this.information = new Information(description);
    }

    public NoticeInformationEvent(Information information) {
        this.information = information;
    }

    public Information getInformation() {
        return information;
    }
}
