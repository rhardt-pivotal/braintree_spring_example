package springexample;

import java.io.File;
import java.util.Map;

import com.braintreegateway.BraintreeGateway;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    public static String DEFAULT_CONFIG_FILENAME = "config.properties";
    public static BraintreeGateway gateway;

    private static final Log log = LogFactory.getLog("BRAINTREE_DEBUG");

    public static void main(String[] args) {
        File configFile = new File(DEFAULT_CONFIG_FILENAME);
        try {
            if(configFile.exists() && !configFile.isDirectory()) {
                gateway = BraintreeGatewayFactory.fromConfigFile(configFile);
            } else {
                gateway = BraintreeGatewayFactory.fromConfigMapping(System.getenv());
            }
        } catch (NullPointerException e) {
            System.err.println("Could not load Braintree configuration from config file or system environment.");
            System.exit(1);
        }

        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner clr() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                log.info("IN CLR BLOCK");


            }
        };
    }


}
