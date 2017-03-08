package springexample;

import com.braintreegateway.BraintreeGateway;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BraintreeGatewayFactory {

    private static final Log log = LogFactory.getLog(BraintreeGatewayFactory.class);


    private static BraintreeGateway maybeAddProxyToGateway(Map<String, String> properties, BraintreeGateway gateway) {
        if(properties.containsKey("BT_PROXY_HOST")) {
            log.info("Found Proxy");
            String host = properties.get("BT_PROXY_HOST");
            String port;
            if(properties.containsKey("BT_PROXY_PORT")){
                port = properties.get("BT_PROXY_PORT");
            }
            else if(host.toLowerCase().startsWith("https")){
                port = "443";
            }
            else {
                port = "80";
            }
            if(!(host.toLowerCase().startsWith("http:") || host.toLowerCase().startsWith("https:"))){
                log.error("BT_PROXY_HOST should be in the format: http://hostname.your.domain or https://hostname.your.domain");
            }
            log.info("proxy scheme+host: "+host);
            log.info("proxy port: "+port);
            gateway.setProxy(host, new Integer(port));
        }
        return gateway;
    }

    public static BraintreeGateway fromConfigMapping(Map<String, String> mapping) {
        BraintreeGateway gateway = new BraintreeGateway(
            mapping.get("BT_ENVIRONMENT"),
            mapping.get("BT_MERCHANT_ID"),
            mapping.get("BT_PUBLIC_KEY"),
            mapping.get("BT_PRIVATE_KEY")
        );

        return maybeAddProxyToGateway(mapping, gateway);
    }

    public static BraintreeGateway fromConfigFile(File configFile) {
        InputStream inputStream = null;
        Properties properties = new Properties();

        try {
            inputStream = new FileInputStream(configFile);
            properties.load(inputStream);
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        } finally {
            try { inputStream.close(); }
            catch (IOException e) { System.err.println("Exception: " + e); }
        }

        BraintreeGateway gateway = new BraintreeGateway(
            properties.getProperty("BT_ENVIRONMENT"),
            properties.getProperty("BT_MERCHANT_ID"),
            properties.getProperty("BT_PUBLIC_KEY"),
            properties.getProperty("BT_PRIVATE_KEY")
        );

        Map<String, String> props = new HashMap<>();
        for (Object k : properties.keySet()){
            props.put(k.toString(), properties.getProperty(k.toString()));
        }

        return maybeAddProxyToGateway(props, gateway);

    }
}
