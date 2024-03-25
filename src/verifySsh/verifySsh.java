package verifySsh;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class verifySsh {
    private static final String IPLIST_FILE = "ips.txt";
    private static final String SSH_USER = "NGOSS_SIA";
    private static final String SSH_PASSWORD = "Ng00ss!@s14";
    private static final int SSH_PORT = 22;

    public static void main(String[] args) {
        List<String> ips = readIPsFromFile();

        for (String SSH_HOST : ips) {
            JSch jsch = new JSch();
            Session session = null;

            try {
                session = jsch.getSession(SSH_USER, SSH_HOST, SSH_PORT);
                session.setPassword(SSH_PASSWORD);
                session.setConfig("StrictHostKeyChecking", "no"); // Auto-add host key
                session.connect();

                System.out.println(SSH_HOST + ",OK");
            } catch (JSchException e) {
                System.out.println(SSH_HOST + ",ERR");
            } finally {
                if (session != null && session.isConnected()) {
                    session.disconnect();
                }
            }
        }
    }

    private static List<String> readIPsFromFile() {
        List<String> ips = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(IPLIST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
            //while (((line = reader.readLine()) != null) && ((line = reader.readLine()) != "")) {
                //ips.add(line.strip());
            	ips.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading IPs from file: " + e.getMessage());
        }
        return ips;
    }
}