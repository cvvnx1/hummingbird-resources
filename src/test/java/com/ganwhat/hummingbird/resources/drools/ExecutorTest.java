package com.ganwhat.hummingbird.resources.drools;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cvvnx1@hotmail.com
 * @since Jun 08, 2023
 */
public class ExecutorTest {

    @Test
    public void exec() throws IOException {
        String rule = content("drools/r1.drl");
        _Hello h = new _Hello();
        h.setWorld("hello world!");
        Map<String, String> out = new HashMap<>();
        out = Executor.exec(rule, h, new HashMap<>(), out);
        Assertions.assertEquals("hello world!", out.get("world"));
        Assertions.assertEquals("hello world!", out.get("getWorld()"));
        Assertions.assertNull(out.get("break"));
    }

    private String content(String path) throws IOException {
        File f = new File(getClass().getClassLoader().getResource(path).getFile());
        return FileUtils.readFileToString(f, StandardCharsets.UTF_8);
    }

}
