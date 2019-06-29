package nl.mesoplz.hue.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HueBridge {

    private ArrayList<HueLight> lights = new ArrayList<>();
    private String ip;
    private String user;

    private static final String USER_AGENT = "Mozilla/5.0";

    public HueBridge(String ip, String user) throws IOException {
        this.ip = ip;
        this.user = user;
        discoverLights();
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
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        OutputStream os = httpCon.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
        osw.write(jsonCommand);
        osw.flush();
        osw.close();
        os.close();  //don't forget to close the OutputStream
        httpCon.connect();

        //get result
        String result = readResult(httpCon);
        System.out.println(result);
    }

    private void discoverLights() throws IOException{
        URL url = new URL("http://" + ip + "/api/" + user + "/lights");
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("GET");

        //get result
        String result = readResult(httpCon);

        try {
            JSONObject object = new JSONObject(result);

            //Start reading the lights (from 1)
            int counter = 1;
            while(object.has(Integer.toString(counter))) {
                lights.add(new HueLight(counter, this));
                counter++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


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
