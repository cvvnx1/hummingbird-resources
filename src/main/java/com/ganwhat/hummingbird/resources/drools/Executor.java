package com.ganwhat.hummingbird.resources.drools;

import com.ganwhat.hummingbird.resources.data.GreedyWrapBean;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cvvnx1@hotmail.com
 * @since Jun 08, 2023
 */
public class Executor {

    public static <T> Map<String, String> exec(String rule, T data, Map<String, Object> context) {
        Map<String, String> out = new HashMap<>();
        KieSession session = getSession(rule);
        session.setGlobal("o", out);
        session.setGlobal("i", new GreedyWrapBean(data));
        session.setGlobal("c", context);
        session.fireAllRules();
        session.dispose();
        return out;
    }

    private static KieSession getSession(String rule) {
        KieServices ks = KieServices.Factory.get();

        KieRepository kr = ks.getRepository();
        kr.addKieModule(kr::getDefaultReleaseId);

        KieFileSystem kfs = ks.newKieFileSystem();
        kfs.write("src/main/resources/org/kie/example5/HAL5.drl", rule);

        KieBuilder kb = ks.newKieBuilder(kfs);
        kb.buildAll();
        if (kb.getResults().hasMessages(Message.Level.ERROR)) {
            throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
        }

        KieContainer kContainer = ks.newKieContainer(kr.getDefaultReleaseId());

        return kContainer.newKieSession();
    }

}
