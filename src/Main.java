import nl.mesoplz.hue.models.HueBridge;
import nl.mesoplz.hue.models.HueLight;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
//            HueBridge bridge = new HueBridge("localhost", "66986704230b2e75868416979af78fe");
            HueBridge bridge = new HueBridge("192.168.1.102", "8f36bb73f410a65f044469ea5b645dca", 5);

            System.out.println(bridge.getLights());
            for (HueLight light : bridge.getLights()) {
                light.setRGB(255, 0, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
