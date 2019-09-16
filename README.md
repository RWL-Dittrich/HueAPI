# JHAPI a JavaHueAPI
This is gonna be used for a school project.


# How to use?
### Making a Hue bridge:
```java
import nl.mesoplz.hue.exceptions.HueException;
import nl.mesoplz.hue.models.HueBridge;
import nl.mesoplz.hue.models.HueLight;

import java.io.IOException;
import java.util.ArrayList;

class HueTest {
    HueBridge bridge;
    {
        try {
            //Instantiating a bridge (Note that transitionTime is optional).
            bridge = new HueBridge("<Hue ip address>", "<Hue user string>", ((optional) transitionTime) 0);

            //The API automatically detects all the lights that the bridge has.
            //You could loop through the bridge.getLights() to change all lights.
            ArrayList<HueLight> lights = bridge.getLights();
            
            //You can also just get a light and change stuff.
            HueLight light = lights.get(0);
            
            //You can turn on a light with the setPower(powerState, (optional) transitionTime) method.
            //(The transitionTime here is overriding the transitionTime we set earlier in the huebridge).
            light.setPower(true, 20);
            
            //You can set the transitionTime of a light with the setTransitionTime(transitionTime) method.
            //This permanently changes the light's transitionTime
            //The transitionTime is in a scale of 10 means 1 second.
            //In this case 20 means two seconds!
            light.setTransitionTime(20);
            
            //You can set the RGB with the setRGB(R, G, B, (optional) transitionTime) method.
            //(The transitionTime here is overriding the transitionTime we set earlier in the huebridge).
            light.setRGB(255, 0, 150, 20);
            
            //You can set the brightness of the light with the setBri(brightness, (optional) transitionTime) method (0-254. keep in mind that 0 does not mean off!)
            //(The transitionTime here is overriding the transitionTime we set earlier in the huebridge).
            light.setBri(255, 10);
            
            
        } catch (HueException e) {
            e.printStackTrace();
        }
    }


}
```

# TODO:
```
- Test the API
- Update README.MD
```
