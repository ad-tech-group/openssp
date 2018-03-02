package com.atg.openssp.dspSim.model.client;

public class ServerCommand {
    private ServerCommandType type;
    private String id;
    private float price;

    public void setType(ServerCommandType type) {
        this.type = type;
    }

    public ServerCommandType getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

}
