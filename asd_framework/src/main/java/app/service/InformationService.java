package app.service;

import app.annotation.Autowired;
import app.annotation.Service;
import app.annotation.Value;
import app.model.Information;

@Service(name = "informationService")
public class InformationService {
    private IUserService userService;
    private IProfileService profileService;
    private ISettingService settingService;

    @Value(key = "facebook")
    private String facebook;

    @Autowired
    public InformationService(IUserService user, IProfileService profile, ISettingService setting) {
        this.userService = user;
        this.profileService = profile;
        this.settingService = setting;
    }

    public void print() {
        this.userService.login();
        this.profileService.getProfile();
        this.settingService.print();
    }

    public String getFacebook() {
        return facebook;
    }

//    @Scheduled(fixedRate = 1000)
//    @Scheduled(cron = "5 1")
//    public void reminder() {
//        Date date = Calendar.getInstance().getTime();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//        String currentTime = df.format(date);
//        System.out.println("This task run at " + currentTime);
//    }
}
