package org.defendev.flowable.demo.multipoc.config;

import org.defendev.common.spring6.web.WebTimeTravelClock;
import org.defendev.common.time.ClockManager;
import org.defendev.common.time.IClockManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class ClockConfig {

    @Bean
    public IClockManager clockManager(ProfileFlags profileFlags) {
        if (profileFlags.timeTravel()) {
            final WebTimeTravelClock timeTravelClock = new WebTimeTravelClock(true);
            return new ClockManager(timeTravelClock);
        } else {
            return ClockManager.system();
        }
    }

}
