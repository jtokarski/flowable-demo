package org.defendev.hibernate.envers.demo.eaa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;



@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    EaaScriptJpaConfig.class
})
public class EaaScriptTest {

    @Test
    public void contextStarts() {
    }

}
