package com.github.achmadns.lab;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Created by achmad on 19/02/16.
 */
public class ResourceTest {
    @Test
    public void validate_behaviour() {
        final Resource resource = new Resource();
        assertThat(resource.isBusy()).isEqualTo(false);
        assertThat(resource.check()).isEqualTo("OK");
        assertThat(resource.busy()).isEqualTo(true);
        assertThat(resource.check()).isEqualTo("BUSY");
        assertThat(resource.isBusy()).isEqualTo(true);
        assertThatExceptionOfType(BusyException.class).isThrownBy(() -> resource.get());
        assertThat(resource.free()).isEqualTo(false);
        assertThat(resource.isBusy()).isEqualTo(false);
        assertThat(resource.get()).isEqualTo("ABC");
        assertThat(resource.check()).isEqualTo("OK");
    }
}