package org.defendev.flowable.demo.multipoc.config;

import org.defendev.common.time.IClockManager;
import org.flowable.common.engine.impl.runtime.Clock;
import org.flowable.common.engine.impl.util.DefaultClockImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.defendev.flowable.demo.multipoc.config.DefendevAccountingProfile.timeTravel;



@Profile({timeTravel})
@Component
public class FlowableCompatibleClock extends DefaultClockImpl implements Clock {

    private final IClockManager clockManager;

    @Autowired
    public FlowableCompatibleClock(IClockManager clockManager) {
        this.clockManager = clockManager;
    }

    @Override
    public Date getCurrentTime() {
        return Date.from(clockManager.nowInstant());
    }

}
