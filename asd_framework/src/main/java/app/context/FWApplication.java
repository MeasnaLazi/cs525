package app.context;

import app.Main;
import org.apache.log4j.BasicConfigurator;

public class FWApplication {
    public static FWContext run(Class<?> clazz) {
        BasicConfigurator.configure();
        FWContext fwContext = new FWContext();
        fwContext.start(clazz);

        return fwContext;
    }
}
