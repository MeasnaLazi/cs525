package app.service;

import app.model.Information;

public interface IProfileService {
    public void getProfile();
    public void createInformation(String desciption);
    public void updateInformation(String description);
    public Information getInformation();
}
