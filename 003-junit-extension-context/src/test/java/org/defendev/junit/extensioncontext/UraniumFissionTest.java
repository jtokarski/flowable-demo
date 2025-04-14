package org.defendev.junit.extensioncontext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.Assertions.assertThat;



@ExtendWith(HeavyButReusableExtension.class)
public class UraniumFissionTest {

    @HeavyButReusableName(name = "uranium")
    @Test
    public void shouldNotVapor(HeavyButReusable someUranium) {
        assertThat(someUranium).isNotNull();
        assertThat(someUranium.getPayload()).contains("uranium");
    }

}
