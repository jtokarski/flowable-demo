package org.defendev.flowable.demo.multipoc.config;

import org.defendev.common.spring6.web.WebTimeTravelClock;
import org.defendev.common.time.ClockManager;
import org.defendev.common.time.IClockManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.defendev.flowable.demo.multipoc.config.DefendevAccountingProfile.timeTravel;



@Configuration
public class ClockConfig {

    @Profile({"!" + timeTravel})
    @Bean
    public IClockManager systemClockManager() {
        return ClockManager.system();
    }

    @Profile({timeTravel})
    @Bean
    public IClockManager timeTravelClockManager() {
        final WebTimeTravelClock timeTravelClock = new WebTimeTravelClock(true);
        return new ClockManager(timeTravelClock);
    }

}
