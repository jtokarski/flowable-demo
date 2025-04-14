package org.defendev.junit.extensioncontext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.Assertions.assertThat;



@HeavyButReusableName(name = "gold")
@ExtendWith(HeavyButReusableExtension.class)
public class GoldFissionTest {

    @Test
    public void shouldNotDisappear(HeavyButReusable someGold) {
        assertThat(someGold).isNotNull();
        assertThat(someGold.getPayload()).contains("gold");
    }

}
