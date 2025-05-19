package org.defendev.junit.demo.extensioncontext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.Assertions.assertThat;



@HeavyButReusableName(name = "uranium")
@ExtendWith(HeavyButReusableExtension.class)
public class UraniumAlkaliTest {

    @Test
    public void shouldNotVapor(HeavyButReusable someUranium) {
        assertThat(someUranium).isNotNull();
        assertThat(someUranium.getPayload()).contains("uranium");
    }

}
