package nl.mesoplz.hue.compare;

import nl.mesoplz.hue.models.HueLight;

import java.util.Comparator;

public class LightComparator implements Comparator<HueLight> {
    @Override
    public int compare(HueLight o1, HueLight o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
