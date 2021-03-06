package nl.vpro.io.prepr.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class PreprWebhookEventTest {


    @BeforeAll
    public static void init() {
        PreprObjectMapper.configureInstance(false);
    }


    @Test
    public void unmarshalChanged() throws IOException {


        PreprWebhookEvent webhook = PreprObjectMapper.INSTANCE.readerFor(PreprWebhookEvent.class)
            .readValue(getClass().getResourceAsStream("/webhook.scheduleevent.changed.json"));


    }


    @Test
    public void unmarshalCreated() throws IOException {


        PreprWebhookEvent webhook = PreprObjectMapper.INSTANCE.readerFor(PreprWebhookEvent.class)
            .readValue(getClass().getResourceAsStream("/webhook.scheduleevent.created.json"));


    }

     @Test
    public void unmarshalAnother() throws IOException {


        PreprWebhookEvent webhook = PreprObjectMapper.INSTANCE.readerFor(PreprWebhookEvent.class)
            .readValue(getClass().getResourceAsStream("/webhookevent.json"));

        assertThat(webhook.getPayload().get("label").textValue()).isEqualTo("TrackPlay");


    }



    @Test
    public void unmarshalV6() throws IOException {


        PreprWebhookEvent webhook = PreprObjectMapper.INSTANCE.readerFor(PreprWebhookEvent.class)
            .readValue(getClass().getResourceAsStream("/webhook.v6.asset.changed.json"));

        assertThat(webhook.getPayload().get("label").textValue()).isEqualTo("Audio");


    }

}
