package nl.vpro.io.mediaconnect;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Properties;
import java.util.UUID;

import org.junit.Test;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class MediaConnectRepositoryImplTest {


    MediaConnectRepositoryImpl impl;

    {

        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(System.getProperty("user.home") + File.separator + "conf" + File.separator + "mediaconnect.properties"));
            impl = MediaConnectRepositoryImpl
                .builder()
                .clientId(properties.getProperty("client_id"))
                .clientSecret(properties.getProperty("client_secret"))
                .build();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }


    @Test
    public void authenticate() throws IOException {
        impl.authenticate();
        log.info("Token: {}", impl.getTokenResponse());
    }

    @Test
    public void getSchedule() throws IOException, URISyntaxException {
        log.info("schedule: {}",
            impl.getSchedule(UUID.fromString("59ad94c1-7dec-4ea0-a9b4-b9eb4b6cfb16") // Channel.RAD5)
                , LocalDate.of(2018, 5, 7), LocalDate.of(2018, 5, 8))
        );
    }
}