package com.potentii.ipc.test;

import com.potentii.ipc.service.api.IPCBridge;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

@RunWith(Suite.class)
public class RequestTest {


    @Test
    @Ignore
    public void testIfMessagesCanBeReceived() throws Exception {
        final String messageToBeSent = "";

        final InputStream in = new ByteArrayInputStream(StandardCharsets.UTF_8.encode(messageToBeSent).array());
        final PrintStream out = new PrintStream(System.out);

        final IPCBridge bridge = new IPCBridge(in, out);

        bridge.listen((req, res) -> {
            String testParam = req.queryString("testParam");
            res.text("Received " + testParam);
        });
    }
}
