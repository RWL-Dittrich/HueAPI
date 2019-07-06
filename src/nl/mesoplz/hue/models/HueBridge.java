package nl.mesoplz.hue.models;

import nl.mesoplz.hue.exceptions.BridgeNotFoundException;
import nl.mesoplz.hue.exceptions.HueException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

public class HueBridge {

    private ArrayList<HueLight> lights = new ArrayList<>();
    private String ip;
    private String user;
    private int transitionTime;

    private static final String USER_AGENT = "Mozilla/5.0";

    public HueBridge(String ip, String user, int transitionTime) throws IOException, HueException {
        this.ip = ip;
        this.user = user;
        this.transitionTime = transitionTime;
        discoverLights();
    }

    public HueBridge(String ip, String user) throws IOException, HueException {
        this(ip, user, 10);
    }


    //A bunch of getter methods
    public ArrayList<HueLight> getLights() {
        return lights;
    }

    public String getIp() {
        return ip;
    }

    public String getUser() {
        return user;
    }

    /**
     * Makes a PUT command to the hue bridge
     * @param jsonCommand this is the message body that should be sent
     * @param subURL this is the subURL that the command should use eg. /lights/1
     * @throws IOException throws IOException when something went wrong with the connection
     */
    void putCommand(String jsonCommand, String subURL) throws IOException {
        URL url = new URL("http://" + ip + "/api/" + user + subURL);
//        System.out.println(url.toString());
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        OutputStream os = httpCon.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
        osw.write(jsonCommand);
        osw.flush();
        osw.close();
        os.close();
//        System.out.println(jsonCommand);
        httpCon.connect();

        //get result
        String result = readResult(httpCon);
//        System.out.println(result);
        httpCon.disconnect();
    }

    /**
     * Discovers lights in this Hue
     * @throws IOException when something goes wrong
     */
    private void discoverLights() throws HueException {
        //Create the Http connection
        try {
            URL url = new URL("http://" + ip + "/api/" + user + "/lights");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("GET");

            //get result
            String result = readResult(httpCon);
            try {
                JSONObject object = new JSONObject(result);

                //Start reading the lights (from 1)
                Iterator<String> keys = object.keys();
                while(keys.hasNext()) {
                    String key = keys.next();
                    if (object.get(key) instanceof JSONObject) {
                        lights.add(new HueLight(Integer.parseInt(key), transitionTime, this));
                    }
                }
            } catch (JSONException e) {
                throw new HueException("Lights JSON could not be read!");
            }
            httpCon.disconnect();
        } catch (IOException e) {
            throw new BridgeNotFoundException("Request to bridge " + ip + " could not be made!");
        }

    }


    /**
     * Reads a result from a HttpUrlConnection
     * @param httpCon the opened HttpURLConnection
     * @return the result string
     * @throws IOException when reading went wrong
     */
    private String readResult(HttpURLConnection httpCon) throws IOException {
        String result;
        BufferedInputStream bis = new BufferedInputStream(httpCon.getInputStream());
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result2 = bis.read();
        while(result2 != -1) {
            buf.write((byte) result2);
            result2 = bis.read();
        }
        result = buf.toString();
        return result;
    }


}
