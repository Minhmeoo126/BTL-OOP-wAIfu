package com.example.libapp.viewmodel;

import com.example.libapp.api.GPTClient;

public class GPTViewModel {
    private GPTClient gptClient = new GPTClient();

    public String getGPTResponse(String input) {
        return gptClient.sendRequest(input);
    }
}