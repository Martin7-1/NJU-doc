package cn.edu.nju.software.soa.archive;

import jakarta.xml.ws.Endpoint;

public class ArchiveServiceApplication {

    private static final String SERVICE_URL = "http://localhost:8082/archive";

    public static void main(String[] args) {
        ArchiveService archiveService = new ArchiveServiceImpl();
        Endpoint.publish(SERVICE_URL, archiveService);
    }
}
