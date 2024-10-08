package org.wora.entity;

public abstract class Component {
    protected int id;
    private String name;
    private double unitCost;
    private double quantity;
    private String componentType;
    private double taxRate;
    private Project project;


    public Component(){}
    public Component(String name, double unitCost, double quantity, String componentType, double taxRate) {
        this.name = name;
        this.unitCost = unitCost;
        this.quantity = quantity;
        this.componentType = componentType;
        this.taxRate = taxRate;
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

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public Project getProject() {
        return project;
    }
    public void setProject(Project project) {
        this.project = project;
    }
    public abstract double getCost();

    public double getCostWithTVA() {
        double costBeforeVAT = getCost();
        return costBeforeVAT * (1 + taxRate);
    }
}

