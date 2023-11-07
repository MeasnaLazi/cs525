package app.service;

import app.annotation.Profile;
import app.annotation.Service;
import app.model.Information;

@Service(name = "profileService")
@Profile(value = "production")
public class ProfileService implements  IProfileService {

    private Information information;
    @Override
    public void getProfile() {
        System.out.println("Profile information (Name: Lazi, Gender: Male)");
    }

    @Override
    public void createInformation(String description) {
        information = new Information();
        information.setDescription(description);
    }

    @Override
    public void updateInformation(String description) {
        information.setDescription(description);
        System.out.println("updateInformation call...");
    }

    @Override
    public Information getInformation() {
        return information;
    }
}
