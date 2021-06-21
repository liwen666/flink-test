package jrx.data.hub.flink.example.query;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author LW
 * @since 2021/5/19  10:29
 */
@Slf4j
public class WebSocketTest {
    public static WebSocketClient getClient() throws URISyntaxException {
        WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://localhost:9088/demo/imserver/10"), new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                log.info("[websocket] 连接成功");
            }

            @Override
            public void onMessage(String message) {
                log.info("[websocket] 收到消息={}", message);

            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                log.info("[websocket] 退出连接");
            }

            @Override
            public void onError(Exception ex) {
                log.info("[websocket] 连接错误={}", ex.getMessage());
            }
        };
        webSocketClient.connect();
        return webSocketClient;
    }
}
