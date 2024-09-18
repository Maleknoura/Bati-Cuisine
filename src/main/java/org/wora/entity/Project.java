package org.wora.entity;

import org.wora.entity.Enum.Status;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private int id;
    private String name;
    private double profitMargin;
    private double totalCost;
    private Status status;
    private Quote quote;
    private Client client;
    private List<Component> components;

    public Project(){ components = new ArrayList<>();}
    public Project(int id,String name,double profitMargin,double totalCost,Status status,Component component){
        this.id=id;
        this.name=name;
        this.profitMargin=profitMargin;
        this.totalCost=totalCost;
        this.status=status;
        this.components = new ArrayList<>();
        this.components.add(component);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(double profitMargin) {
        this.profitMargin = profitMargin;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }
    public void add(Component component) {
        components.add(component);
    }


}
