package org.defendev.junit.demo.extensioncontext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;



@HeavyButReusableName(name = "platinum")
@ExtendWith(HeavyButReusableExtension.class)
public class PlatinumAcidTest {

    @Test
    public void shouldNotDissolve(HeavyButReusable somePlatinum) {
        assertThat(somePlatinum).isNotNull();
        assertThat(somePlatinum.getPayload()).contains("platinum");
    }

}
