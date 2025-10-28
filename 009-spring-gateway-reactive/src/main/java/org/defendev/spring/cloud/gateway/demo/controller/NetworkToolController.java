package org.defendev.spring.cloud.gateway.demo.controller;

import org.apache.commons.lang3.time.StopWatch;
import org.defendev.spring.cloud.gateway.demo.dto.SlowConsumerDto;
import org.defendev.spring.cloud.gateway.demo.dto.SocketProbeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.common.net.InetAddresses.isInetAddress;
import static java.util.Objects.nonNull;



@RequestMapping(path = { "/network-tool" })
@Controller
public class NetworkToolController {

    @RequestMapping(path = "socket-probe/{ipAddress}/{port}")
    public Mono<ResponseEntity<SocketProbeDto>> executeSocketProbe(
        @PathVariable String ipAddress,
        @PathVariable int port
    ) {
        /*
         * todo: investigate (and potentially prove) that this method works for hosts that don't support ICMP
         *
         */
        if (!isInetAddress(ipAddress)) {
            final SocketProbeDto probeDto = new SocketProbeDto(false, null, false, 0, "Incorrect address format");
            return Mono.just(ResponseEntity.ok(probeDto));
        }
        if (port < 0x1 || port > 0xFFFF) {
            final SocketProbeDto probeDto = new SocketProbeDto(false, null, false, 0, "Incorrect port value");
            return Mono.just(ResponseEntity.ok(probeDto));
        }

        final StopWatch stopWatch = StopWatch.create();
        String localHost = "unknown";
        try {
            localHost = InetAddress.getLocalHost().toString();
            final InetAddress inetAddress = InetAddress.getByName(ipAddress);
            final Socket socket = new Socket();
            stopWatch.start();
            socket.connect(new InetSocketAddress(inetAddress, port), 15_000);
            stopWatch.stop();
            final long executionTime = stopWatch.getTime(TimeUnit.MILLISECONDS);
            final SocketProbeDto probeDto = new SocketProbeDto(true, localHost, true, executionTime, null);
            return Mono.just(ResponseEntity.ok(probeDto));
        } catch (Throwable e) {
            stopWatch.stop();
            final long executionTime = stopWatch.getTime(TimeUnit.MILLISECONDS);
            String errorDetail = e.getClass().getCanonicalName() + " | " + e.getMessage();
            if (nonNull(e.getCause())) {
                final Throwable cause = e.getCause();
                errorDetail += " | " + cause.getClass().getCanonicalName() + " | " + cause.getMessage();
            }
            final SocketProbeDto probeDto = new SocketProbeDto(true, localHost, false, executionTime, errorDetail);
            return Mono.just(ResponseEntity.ok(probeDto));
        }
    }

    @RequestMapping(path = "slow-consumer")
    public Mono<ResponseEntity<SlowConsumerDto >> simulateSlowConsumer(
        @RequestParam int delayStartHandshake,
        @RequestParam int delayWriteRequest,
        @RequestParam int delayByteRead
    ) {
        /*
         * Example candidate URLs:
         *   - https://login.microsoftonline.com/d9d3ff2d-d7ca-4821-bd09-ed953ac26c13/v2.0/.well-known/openid-configuration
         *   - https://login.microsoftonline.com/d9d3ff2d-d7ca-4821-bd09-ed953ac26c13/discovery/v2.0/keys
         *
         *
         */
        final List<String> completedSteps = new ArrayList<>();

        final String host = "login.microsoftonline.com";
        final int port = 443;
        final String requestPath = "/d9d3ff2d-d7ca-4821-bd09-ed953ac26c13/discovery/v2.0/keys";

        final StopWatch stopWatch = StopWatch.create();
        stopWatch.start();
        SSLSocket sslSocket = null;
        int totalBytes = 0;
        try {
            // 1. Get the default SSL Socket Factory and create a socket
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            sslSocket = (SSLSocket) factory.createSocket(host, port);

            // Set a socket timeout for robustness
            sslSocket.setSoTimeout(5000); // 5 second read timeout

            Thread.sleep(delayStartHandshake);

            // 2. Perform SSL Handshake
            sslSocket.startHandshake();
            completedSteps.add("SSL/TLS handshake complete.");

            Thread.sleep(delayWriteRequest);

            // 3. Construct and send the HTTP GET request
            final String request =
                "GET " + requestPath + " HTTP/1.1\r\n" +
                "Host: " + host + "\r\n" +
                "Connection: close\r\n" +
                "\r\n";

            final OutputStream out = sslSocket.getOutputStream();
            // Using US_ASCII encoding for standard HTTP request headers
            out.write(request.getBytes(StandardCharsets.US_ASCII));
            out.flush();

            completedSteps.add("Sent request:\n---\n" + request.trim() + "\n---");
            completedSteps.add("Starting slow read. Watching for data...");

            // 4. Read the response byte by byte
            final InputStream in = sslSocket.getInputStream();
            int byteRead;

            // in.read() returns the byte as an integer (0 to 255) or -1 for end of stream
            while ((byteRead = in.read()) != -1) {
                totalBytes++;
                byte b = (byte) byteRead;

                // Determine what to print for the byte
                final String charOutput;
                // Check if it's a printable ASCII character or a standard line break
                if (b >= 32 && b <= 126 || b == '\r' || b == '\n') {
                    charOutput = String.valueOf((char) b);
                } else {
                    // Non-printable or extended character, print as hex for debugging
                    charOutput = String.format("\\x%02x", b);
                }

                // Print the byte to the console in real-time
                System.out.print(charOutput);

                // Introduce the intentional delay
                Thread.sleep(delayByteRead);
            }

            completedSteps.add("--- Connection Closed ---");

        } catch (IOException e) {
            completedSteps.add("[ERROR] I/O or Socket error occurred: " + e.getClass().getCanonicalName() + " | " +
                e.getMessage());
        } catch (InterruptedException e) {
            // Restore interrupt status in case thread was externally interrupted
            Thread.currentThread().interrupt();
            completedSteps.add("[ERROR] Thread interrupted during sleep: " + e.getClass().getCanonicalName() + " | " +
                e.getMessage());
        } finally {
            // 5. Clean up
            if (sslSocket != null) {
                try {
                    sslSocket.close();
                } catch (IOException e) {
                    completedSteps.add("[ERROR] Failed to close socket: " + e.getClass().getCanonicalName() + " | " +
                        e.getMessage());
                }
            }
        }
        stopWatch.stop();
        final long executionTime = stopWatch.getTime(TimeUnit.MILLISECONDS);
        final SlowConsumerDto dto = new SlowConsumerDto(delayStartHandshake, delayWriteRequest, delayByteRead,
            executionTime, totalBytes, completedSteps);
        return Mono.just(ResponseEntity.ok(dto));
    }

}
