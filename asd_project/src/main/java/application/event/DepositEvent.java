package application.event;

public class DepositEvent {
    private String description;

    public DepositEvent(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
