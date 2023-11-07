package app.schedule;

import java.lang.reflect.Method;
import java.util.Timer;

public class ScheduleTimer {
    private Object fixedRate;
    private Method method;
    private Object instance;
    private Object[] params;
    private Timer timer;

    public ScheduleTimer(Builder builder) {
        this.method = builder.method;
        this.instance = builder.instance;
        this.params = builder.params;
        this.fixedRate = builder.fixedRate;
    }

    public void start() {
        ScheduleTask task = new ScheduleTask(this.method, this.instance, this.params);
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(task, 0, covertFixedRateToMS());
    }

    public void stop() {
        this.timer.cancel();
    }

    private long covertFixedRateToMS() {
        long ms = 0;
        if (this.fixedRate instanceof Number) {
            ms = Long.parseLong(this.fixedRate.toString());
        } else {
            String[] items = this.fixedRate.toString().split(" ");
            if (items.length >= 2) {
                long sec = Long.parseLong(items[0]);
                long min = Long.parseLong(items[1]);
                ms = ((min * 60) + sec) * 1000;
            }
        }
        return ms;
    }

    public static class Builder {
        private Method method;
        private Object instance;
        private Object[] params;
        private Object fixedRate;

        public Builder withMethod(Method method) {
            this.method = method;
            return this;
        }
        public Builder withInstance(Object instance) {
            this.instance = instance;
            return this;
        }
        public Builder withParamaters(Object[] params) {
            this.params = params;
            return this;
        }

        public Builder runEvery(Object fixedRate) {
            this.fixedRate = fixedRate;
            return this;
        }

        public ScheduleTimer build() {
            return new ScheduleTimer(this);
        }
    }
}
