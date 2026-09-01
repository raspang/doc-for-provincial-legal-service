package com.lds.web.rest;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system")
public class SystemInfoResource {

    private final Logger log = LoggerFactory.getLogger(SystemInfoResource.class);

    // Inject the server port from application.yml (usually 8080)
    @Value("${server.port:8080}")
    private int serverPort;

    @GetMapping("/access-url")
    public ResponseEntity<String> getLocalAccessUrl() {
        try {
            // Iterate through all network interfaces to find the active LAN IP
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = networkInterfaces.nextElement();

                // Skip loopback (127.0.0.1), virtual (VMs/Docker), and down interfaces
                if (!ni.isLoopback() && ni.isUp() && !ni.isVirtual()) {
                    Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();

                        // We only want IPv4 addresses (e.g., 192.168.x.x)
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                            String ip = inetAddress.getHostAddress();
                            String accessUrl = "http://" + ip + ":" + serverPort;
                            log.info("Detected local access URL: {}", accessUrl);
                            return ResponseEntity.ok(accessUrl);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to detect local IP address", e);
        }

        // Fallback if detection fails
        return ResponseEntity.ok("http://localhost:" + serverPort);
    }
}
