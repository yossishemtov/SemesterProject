package entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ClientConnectionStatus implements Comparable<ClientConnectionStatus> {
    private String ip, host, status;
    private final String startTime;
    private final LocalDateTime connectionTime; 

    public ClientConnectionStatus(String ip, String host, String status) {
        this.ip = ip;
        this.host = host;
        this.status = status;
        this.connectionTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.startTime = this.connectionTime.format(formatter);
    }

    // Getters
    public String getIp() { return ip; }
    public String getHost() { return host; } 
    public String getStatus() { return status; }
    public String getStartTime() { return startTime; }
    public LocalDateTime getConnectionTime() { return connectionTime; }

    // Setters
    public void setIp(String ip) { this.ip = ip; }
    public void setHost(String host) { this.host = host; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public int hashCode() { return Objects.hash(host, ip); }
    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ClientConnectionStatus other = (ClientConnectionStatus) obj;
        return Objects.equals(ip, other.ip) && Objects.equals(host, other.host);
    }

    @Override
    public String toString() {
        return String.format("[ip=%s, host=%s, status=%s, startTime=%s]", ip, host, status, startTime);
    }

    @Override
    public int compareTo(ClientConnectionStatus other) {
        return this.connectionTime.compareTo(other.connectionTime);
    }
}
