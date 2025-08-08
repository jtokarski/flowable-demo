package org.defendev.hibernate.demo.haa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;



@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HaaJpaConfig.class})
public class HaaTest {

    @Test
    public void contextStarts() {
    }

}
