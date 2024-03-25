package verifySsh;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class verifySsh {
	
	//getPropertyValues prop1 = new getPropertyValues();
	
	private static final String IPLIST_FILE = "ips.txt";
	private static final String CONFIG_FILE = "config.properties";
    //private static final String SSH_USER = "NGOSS_SIA";
    //private static final String SSH_PASSWORD = "Ng00ss!@s14";
    //private static final int SSH_PORT = 22;
	
    public static void main(String[] args) {
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);
            //String IPLIST_FILE = prop.getProperty("iplist_file");
            String SSH_USER = prop.getProperty("ssh_user");
            String SSH_PASSWORD = prop.getProperty("ssh_password");
            int SSH_PORT = Integer.parseInt(prop.getProperty("ssh_port"));
            
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
        } catch (IOException ex) {
	        ex.printStackTrace();
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