package com.ganwhat.hummingbird.resources.drools;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * @author cvvnx1@hotmail.com
 * @since Jun 08, 2023
 */
public class Executor {

    public static <IN, OUT, CTX> OUT exec(String rule, IN data, CTX context, OUT out) {
        KieSession session = getSession(rule);
        session.setGlobal("o", out);
        session.setGlobal("i", data);
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
        kfs.write("src/main/resources/com/ganwhat/hummingbird/kie/default.drl", rule);

        KieBuilder kb = ks.newKieBuilder(kfs);
        kb.buildAll();
        if (kb.getResults().hasMessages(Message.Level.ERROR)) {
            throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
        }

        KieContainer kContainer = ks.newKieContainer(kr.getDefaultReleaseId());

        return kContainer.newKieSession();
    }

}
