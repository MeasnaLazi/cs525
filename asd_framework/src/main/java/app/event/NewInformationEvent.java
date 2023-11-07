package app.event;

import app.model.Information;

public class NewInformationEvent {
    private Information information;

    public NewInformationEvent(String description) {
        this.information = new Information(description);
    }

    public NewInformationEvent(Information information) {
        this.information = information;
    }

    public Information getInformation() {
        return information;
    }
}
