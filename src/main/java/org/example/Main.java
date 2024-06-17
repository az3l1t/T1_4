package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        Main m = new Main();
        System.out.println(m.getRoles());
        String email = m.signUp();
        String code = m.getCode(email);
        String result = m.encode(email,code);
        m.setStatus(result);
    }

    public String getRoles() throws IOException {
        URL url = new URL("http://193.19.100.32:7000/api/get-roles");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Настройка запроса
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            in.close();
            byte[] responseBytes = out.toByteArray();
            String response = new String(responseBytes, StandardCharsets.UTF_8);

            return response;
        } else {
            throw new IOException("Ошибка при отправке запроса. Код ответа: " + responseCode);
        }
    }

    public String signUp() throws IOException {
        URL url = new URL("http://193.19.100.32:7000/api/sign-up");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");

        UserRequest userRequest = new UserRequest();
        userRequest.setFirstName("Норайр");
        userRequest.setLastName("Аванесян");
        userRequest.setEmail("etern@example.com");
        userRequest.setRole("Разработчик Java");

        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = mapper.writeValueAsString(userRequest);

        OutputStream stream = conn.getOutputStream();
        byte[] mess = jsonInput.getBytes(StandardCharsets.UTF_8);
        stream.write(mess,0, mess.length);

        int response = conn.getResponseCode();
        if(response== HttpURLConnection.HTTP_OK){
            System.out.println("Вы были зарегестрированы!");
        } else {
            System.out.println("Что-то пошло не так!");
        }
        return userRequest.getEmail();
    }

    public String getCode(String email) throws IOException{
        URL url = new URL("http://193.19.100.32:7000/api/get-code?email=" + email);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "text/plain");

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            in.close();
            byte[] responseBytes = out.toByteArray();
            String response = new String(responseBytes, StandardCharsets.UTF_8);
            System.out.println("Код подтверждения: " + response);
            return response;
        } else {
            System.out.println("Ошибка при отправке запроса. Код ответа: " + responseCode);
            return null;
        }
    }

    public String encode(String email, String code) throws IOException {
        String encodedEmail = java.net.URLEncoder.encode(email, StandardCharsets.UTF_8.toString());
        String encodedCode = java.net.URLEncoder.encode(code, StandardCharsets.UTF_8.toString());

        URL urlEncode = new URL("http://localhost:8080/api/encode?email=" + encodedEmail + "&code=" + encodedCode);
        HttpURLConnection connEncode = (HttpURLConnection) urlEncode.openConnection();

        connEncode.setRequestMethod("GET");
        connEncode.setRequestProperty("Accept", "text/plain");

        int responseCodeEncode = connEncode.getResponseCode();
        String token = "";
        if (responseCodeEncode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connEncode.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            token = response.toString();
            System.out.println(token);
            return token;
        } else {
            System.out.println("Ошибка при отправке запроса на /api/encode. Код ответа: " + responseCodeEncode);
            return null;
        }
    }

    public void setStatus(String token) throws IOException {
        URL urlSetStatus = new URL("http://193.19.100.32:7000/api/set-status");
        HttpURLConnection connSetStatus = (HttpURLConnection) urlSetStatus.openConnection();

        connSetStatus.setDoOutput(true);
        connSetStatus.setRequestMethod("POST");
        connSetStatus.setRequestProperty("Content-Type", "application/json; utf-8");
        connSetStatus.setRequestProperty("Accept", "application/json");

        StatusRequest statusRequest = new StatusRequest();
        statusRequest.setToken(token);
        statusRequest.setStatus("increased");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInputString = objectMapper.writeValueAsString(statusRequest);

        try (OutputStream os = connSetStatus.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCodeSetStatus = connSetStatus.getResponseCode();
        if (responseCodeSetStatus == HttpURLConnection.HTTP_OK) {
            System.out.println("Запрос успешно отправлен на /api/set-status.");
        } else {
            System.out.println("Ошибка при отправке запроса на /api/set-status. Код ответа: " + responseCodeSetStatus);
        }
    }
}
