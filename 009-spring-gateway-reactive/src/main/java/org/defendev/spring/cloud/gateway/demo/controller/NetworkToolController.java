package org.defendev.spring.cloud.gateway.demo.controller;

import org.apache.commons.lang3.time.StopWatch;
import org.defendev.spring.cloud.gateway.demo.dto.SocketProbeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
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
        if (!isInetAddress(ipAddress)) {
            final SocketProbeDto probeDto = new SocketProbeDto(false, false, 0, "Incorrect address format");
            return Mono.just(ResponseEntity.ok(probeDto));
        }
        if (port < 0x1 || port > 0xFFFF) {
            final SocketProbeDto probeDto = new SocketProbeDto(false, false, 0, "Incorrect port value");
            return Mono.just(ResponseEntity.ok(probeDto));
        }

        final StopWatch stopWatch = StopWatch.create();
        try {
            final InetAddress inetAddress = InetAddress.getByName(ipAddress);
            final Socket socket = new Socket();
            stopWatch.start();
            socket.connect(new InetSocketAddress(inetAddress, port), 15_000);
            stopWatch.stop();
            final long executionTime = stopWatch.getTime(TimeUnit.MILLISECONDS);
            final SocketProbeDto probeDto = new SocketProbeDto(true, true, executionTime, null);
            return Mono.just(ResponseEntity.ok(probeDto));
        } catch (Throwable e) {
            stopWatch.stop();
            final long executionTime = stopWatch.getTime(TimeUnit.MILLISECONDS);
            String errorDetail = e.getClass().getCanonicalName() + " | " + e.getMessage();
            if (nonNull(e.getCause())) {
                final Throwable cause = e.getCause();
                errorDetail += " | " + cause.getClass().getCanonicalName() + " | " + cause.getMessage();
            }
            final SocketProbeDto probeDto = new SocketProbeDto(true, false, executionTime, errorDetail);
            return Mono.just(ResponseEntity.ok(probeDto));
        }
    }

}
