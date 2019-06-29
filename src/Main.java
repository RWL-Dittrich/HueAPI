import nl.mesoplz.hue.models.HueBridge;
import nl.mesoplz.hue.models.HueLight;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            HueBridge bridge = new HueBridge("localhost", "66986704230b2e75868416979af78fe");
            System.out.println(bridge.getLights());
            for (HueLight light : bridge.getLights()) {
                light.setRGB(150, 0, 255);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
