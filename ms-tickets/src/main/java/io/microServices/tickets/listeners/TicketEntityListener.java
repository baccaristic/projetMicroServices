package io.microServices.tickets.listeners;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class TicketEntityListener {
    @SneakyThrows
    public static String callRemoteEndpoint(String testValue) {
        URL url = new URL("http://192.168.96.1:4747/openai/answer");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String payload = String.format("{\"text\":\"%s\"}", testValue);

        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        writer.write(payload);
        writer.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Extract 'text' from JSON. Assuming a simple {"text": "some_value"} structure.
        String text = response.toString().split(":")[1];
        text = text.substring(1, text.length() - 2); // Remove quotes and closing brace.

        return text;
    }



}