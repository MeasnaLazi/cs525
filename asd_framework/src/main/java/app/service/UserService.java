package app.service;

import app.annotation.Autowired;
import app.annotation.Service;

@Service(name = "userService")
public class UserService implements IUserService {

    @Autowired
    private IProfileService profileService;

    private ISettingService settingService;

    @Override
    public void login() {
        System.out.println("User login successfully!");
        this.profileService.getProfile();
        this.settingService.print();
    }

    @Autowired
    public void setSettingService(ISettingService settingService) {
        this.settingService = settingService;
    }
}
