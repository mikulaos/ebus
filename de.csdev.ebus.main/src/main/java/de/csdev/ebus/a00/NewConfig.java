package de.csdev.ebus.a00;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import de.csdev.ebus.cfg.datatypes.EBusTypes;
import de.csdev.ebus.cfg.json.v1.OH1ConfigurationReader;
import de.csdev.ebus.cfg.json.v1.mapper.EBusConfigurationTelegram;
import de.csdev.ebus.command.EBusCommand;
import de.csdev.ebus.command.EBusCommandRegistry;
import de.csdev.ebus.command.EBusCommandUtils;
import de.csdev.ebus.command.IEBusCommand;
import de.csdev.ebus.core.EBusConnectorEventListener;
import de.csdev.ebus.core.EBusController;
import de.csdev.ebus.core.EBusDataException;
import de.csdev.ebus.core.connection.EBusEmulatorConnection;
import de.csdev.ebus.core.connection.IEBusConnection;
import de.csdev.ebus.main.EBusMain2;
import de.csdev.ebus.utils.EBusUtils;

public class NewConfig {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            new NewConfig().run();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private EBusTypes registry;

    public List<EBusConfigurationTelegram> loadConfiguration() throws IOException {

        ClassLoader classLoader = this.getClass().getClassLoader();
        // URL resource = classLoader.getResource("other.json");
        URL resource = classLoader.getResource("common-configuration.json");

        final ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        final InputStream inputStream = resource.openConnection().getInputStream();

        final List<EBusConfigurationTelegram> loadedTelegramRegistry = mapper.readValue(inputStream,
                new TypeReference<List<EBusConfigurationTelegram>>() {
                });

        return loadedTelegramRegistry;
    }

    public void run() throws InterruptedException {

    	URL url0 = EBusMain2.class.getResource("/replay.txt");
        IEBusConnection connection = new EBusEmulatorConnection(url0);
        EBusController controller = new EBusController(connection);

        registry = new EBusTypes();

        final EBusCommandRegistry tregistry = new EBusCommandRegistry();

        OH1ConfigurationReader ohreader = new OH1ConfigurationReader();
        ohreader.setEBusTypes(registry);
        
//        ConfigurationReader oh2reader = new ConfigurationReader();
//        oh2reader.setEBusTypes(registry);
//        tregistry.addTelegramConfigurationList(nlist);
        

        try {
            // oh2reader.aaaa();
            // ohreader.b(cfgs, registry)

            ClassLoader classLoader = this.getClass().getClassLoader();
            URL url = classLoader.getResource("common-configuration.json");

            ohreader.setEBusTypes(registry);
            List<EBusCommand> loadConfiguration = ohreader.loadConfiguration(url.openStream());
            tregistry.addTelegramConfigurationList(loadConfiguration);

            // List<EBusCommand> list = ohreader.b(loadConfiguration(), registry);
            // tregistry.addTelegramConfigurationList(list);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        controller.addEBusEventListener(new EBusConnectorEventListener() {

            public void onTelegramReceived(byte[] receivedData, Integer sendQueueId) {
                // TODO Auto-generated method stub
                System.out.println("NewConfig.run().new EBusConnectorEventListener() {...}.onTelegramReceived()"
                        + EBusUtils.toHexDumpString(receivedData));
                // List<EBusCommand> find = tregistry.find(receivedData);
                // System.out
                // .println("NewConfig.run().new EBusConnectorEventListener() {...}.onTelegramReceived()" + find);
                //
                List<IEBusCommand> find2 = tregistry.find(receivedData);
                // System.out
                // .println("NewConfig.run().new EBusConnectorEventListener() {...}.onTelegramReceived()" + find2);

                for (IEBusCommand eBusCommand : find2) {
                    Map<String, Object> encode = EBusCommandUtils.decodeTelegram(eBusCommand, receivedData);
                    System.out.println(encode);
                }
            }

            public void onTelegramException(EBusDataException exception, Integer sendQueueId) {
                System.out.println(
                        "NewConfig.run().new EBusConnectorEventListener() {...}.onTelegramException()" + exception);
                // TODO Auto-generated method stub

            }
        });

        controller.start();
        // main thread wait
        controller.join();
    }

}
