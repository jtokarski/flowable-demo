package org.defendev.junit.demo.extensioncontext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.Assertions.assertThat;



@HeavyButReusableName(name = "gold")
@ExtendWith(HeavyButReusableExtension.class)
public class GoldAcidTest {

    @Test
    public void shouldNotDisappear(HeavyButReusable someGold) {
        assertThat(someGold).isNotNull();
        assertThat(someGold.getPayload()).contains("gold");
    }

}
