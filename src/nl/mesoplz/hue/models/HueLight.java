package nl.mesoplz.hue.models;

import java.awt.*;
import java.io.IOException;

public class HueLight {
    private int lightID;
    private HueBridge bridge;
    private int transitionTime;

    HueLight(int lightID, int transitionTime ,HueBridge bridge) {
        this.lightID = lightID;
        this.bridge = bridge;
        this.transitionTime = transitionTime;
    }


    /**
     * Sets the RGB of a light of this light
     * @param r r value
     * @param g g value
     * @param b b value
     * @throws IOException IOException when something went wrong with sending the command
     */
    public void setRGB(int r, int g, int b) throws IOException{
        if(r > 255 || r < 0 || g > 255 || g < 0 || b > 255 || b < 0) {
            throw new IllegalArgumentException("Value of r g or b cannot be more than 255 and less than 0");
        }
        double[] xy = getXYFromColor(new Color(r, g, b));
        bridge.putCommand("{\"xy\": [" + xy[0] + ", " + xy[1] + "], \"transitiontime\": " + transitionTime + "}","/lights/" + lightID + "/state");

    }

    /**
     * Sets the RGB of a light of this light with transitionTime
     * @param r r value
     * @param g g value
     * @param b b value
     * @param transitionTime time in seconds to transition
     * @throws IOException IOException when something went wrong with sending the command
     */
    public void setRGB(int r, int g, int b, int transitionTime) throws IOException{
        if(r > 255 || r < 0 || g > 255 || g < 0 || b > 255 || b < 0 || transitionTime < 0) {
            throw new IllegalArgumentException("Value of r g or b cannot be more than 255 and less than 0. Or TransitionTime is less than 0");
        }
        double[] xy = getXYFromColor(new Color(r, g, b));
        bridge.putCommand("{\"xy\": [" + xy[0] + ", " + xy[1] + "], \"transitiontime\": " + transitionTime + "}","/lights/" + lightID + "/state");

    }

    /**
     * Sets the brightness of a light of this light
     * @param bri brightness 0 - 254
     * @throws IOException IOException when something went wrong with sending the command
     */
    public void setBri(int bri) throws IOException{
        if(bri > 254 || bri < 0) {
            throw new IllegalArgumentException("Value of brightness cannot be more than 254 or less than 0");
        }
        bridge.putCommand("{\"bri\": " + bri + ", \"transitiontime\": " + transitionTime + "}","/lights/" + lightID + "/state");
    }

    /**
     * Sets the brightness of a light of this light with transitionTime
     * @param bri brightness 0 - 254
     * @param transitionTime time in seconds to transition
     * @throws IOException IOException when something went wrong with sending the command
     */
    public void setBri(int bri, int transitionTime) throws IOException{
        if(bri > 254 || bri < 0 || transitionTime < 0) {
            throw new IllegalArgumentException("Value of brightness cannot be more than 254 or less than 0. Or TransitionTime is less than 0");
        }
        bridge.putCommand("{\"bri\": " + bri + ", \"transitiontime\": " + transitionTime + "}","/lights/" + lightID + "/state");

    }

    /**
     * Sets the power of the light
     * @param power true = turn on light, false = turn off light
     * @throws IOException IOException when something went wrong with sending the command
     */
    public void setPower(boolean power) throws IOException{
        bridge.putCommand("{\"on\": " + power + ", \"transitiontime\": " + transitionTime + "}","/lights/" + lightID + "/state");
    }

    /**
     * Sets the power of the light with transitionTime
     * @param power true = turn on light, false = turn off light
     * @param transitionTime The transitiontime to change the light with
     * @throws IOException IOException when something went wrong with sending the command
     */
    public void setPower(boolean power, int transitionTime) throws IOException{
        if (power) {
            bridge.putCommand("{\"on\": " + true + ", \"transitiontime\": " + transitionTime + "}", "/lights/" + lightID + "/state");
        } else {
            bridge.putCommand("{\"on\": " + false + "}", "/lights/" + lightID + "/state");

        }
    }

    public void setTransitionTime(int transitionTime) {
        this.transitionTime = transitionTime;
    }

    /**
     * Gets the XY values from a java Color object
     * @param c the color
     * @return a double[] that contains the x value on 0 and y value on 1
     */
    private double[] getXYFromColor(Color c) {
        // For the hue bulb the corners of the triangle are:
        // -Red: 0.675, 0.322
        // -Green: 0.4091, 0.518
        // -Blue: 0.167, 0.04
        double[] normalizedToOne = new double[3];
        float cRed, cGreen, cBlue;
        cRed = c.getRed();
        cGreen = c.getGreen();
        cBlue = c.getBlue();
        normalizedToOne[0] = (cRed / 255);
        normalizedToOne[1] = (cGreen / 255);
        normalizedToOne[2] = (cBlue / 255);
        float red, green, blue;

        // Make red more vivid
        if (normalizedToOne[0] > 0.04045) {
            red = (float) Math.pow(
                    (normalizedToOne[0] + 0.055) / (1.0 + 0.055), 2.4);
        } else {
            red = (float) (normalizedToOne[0] / 12.92);
        }

        // Make green more vivid
        if (normalizedToOne[1] > 0.04045) {
            green = (float) Math.pow((normalizedToOne[1] + 0.055)
                    / (1.0 + 0.055), 2.4);
        } else {
            green = (float) (normalizedToOne[1] / 12.92);
        }

        // Make blue more vivid
        if (normalizedToOne[2] > 0.04045) {
            blue = (float) Math.pow((normalizedToOne[2] + 0.055)
                    / (1.0 + 0.055), 2.4);
        } else {
            blue = (float) (normalizedToOne[2] / 12.92);
        }

        float X = red * 0.649926f + green * 0.103455f + blue * 0.197109f;
        float Y = red * 0.234327f + green * 0.743075f + blue * 0.022598f;
        float Z = red * 0.0000000f + green * 0.053077f + blue * 1.035763f;

        float x = X / (X + Y + Z);
        float y = Y / (X + Y + Z);

        double[] xy = new double[2];
        xy[0] = x;
        xy[1] = y;
        return xy;
    }
}
