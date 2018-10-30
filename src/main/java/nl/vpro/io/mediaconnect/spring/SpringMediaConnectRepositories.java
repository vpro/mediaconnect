package nl.vpro.io.mediaconnect.spring;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import nl.vpro.io.mediaconnect.MediaConnectRepositories;
import nl.vpro.io.mediaconnect.MediaConnectRepository;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Slf4j
public class SpringMediaConnectRepositories  implements MediaConnectRepositories, ApplicationContextAware  {

    final Map<String, MediaConnectRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;
    @Override
    public Optional<MediaConnectRepository> get(String channel) {
        return Optional.ofNullable(repositories.get(channel));

    }

    @Override
    public Map<String, MediaConnectRepository> getRepositories() {
        return repositories;

    }

    @Override
    public Iterator<MediaConnectRepository> iterator() {
        return repositories.values().iterator();

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;


    }
    @PostConstruct
    public void fill(){
        applicationContext.getBeansOfType(MediaConnectRepository.class).values().forEach(mc -> {
            repositories.put(mc.getChannel(), mc);
        });
        log.info("{}", repositories);
    }

}
