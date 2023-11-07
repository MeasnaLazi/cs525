package app;

import app.annotation.*;
import app.model.Information;
import app.runnable.Runnable;
import app.context.FWApplication;
//import app.service.IUserService;
import app.service.*;

@App
@EnableAsync
public class Main implements Runnable {

    @Autowired
    private IProfileService profileService;

    public static void main(String[] args) {
        FWApplication.run(Main.class);
    }

    @Override
    public void run() {
       profileService.createInformation("hiii");
       profileService.updateInformation("hello");
    }
}