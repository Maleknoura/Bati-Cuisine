package org.wora.entity;

public class Material extends Component{
        private double transportCost;
        private double qualityCoefficient;

    public Material(){}

    @Override
    public double getCost() {
        return 0;
    }

    public Material(String name,double unitCost, double quantity, String componentType, double taxRate,double transportCost,double qualityCoefficient){
        super (name,unitCost,quantity,componentType,taxRate);
        this.transportCost=transportCost;
        this.qualityCoefficient=qualityCoefficient;
    }
    public double getTransportCost() {
        return transportCost;
    }

    public void setTransportCost(double transportCost) {
        this.transportCost = transportCost;
    }

    public double getQualityCoefficient() {
        return qualityCoefficient;
    }

    public void setQualityCoefficient(double qualityCoefficient) {
        this.qualityCoefficient = qualityCoefficient;
    }
}
