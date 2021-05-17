package com.example.myapplication.Model;

public class TempHum {
    private int humidity;
    private int temperature;

    public TempHum() {
    }
    public TempHum( int humidity, int temperature) {
        this.humidity = humidity;
        this.temperature = temperature;
    }
    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}

