package app.service;

import app.annotation.Profile;
import app.annotation.Service;
import app.model.Information;

@Service(name = "mockProfileService")
@Profile(value = "test")
public class MockProfileService implements  IProfileService {

    private Information information;
    @Override
    public void getProfile() {
        System.out.println("Mock Profile information (Name: Mock, Gender: Female)");
    }

    @Override
    public void createInformation(String description) {
        information = new Information();
        information.setDescription(description);
    }

    @Override
    public void updateInformation(String description) {
        information.setDescription(description);
    }

    @Override
    public Information getInformation() {
        return information;
    }
}
