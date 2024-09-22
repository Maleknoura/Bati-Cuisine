package org.wora.entity;

import java.util.List;

public class Client {
    private int id;
    private String name;
    private String address;
    private String numberPhone;
    private boolean isProfessionel;
    private double remiseRate;
    private List<Project> projects;

    public Client(){
    }

    public Client (int id, String name ,String adress,String numberPhone ,boolean isProfessionel){
       this.id=id;
       this.name=name;
       this.address=adress;
       this.numberPhone=numberPhone;
       this.isProfessionel=isProfessionel;
    }

    public int getId() {
        return id;
    }

    public String getAdress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRemiseRate() {
        return remiseRate;
    }
    public void setRemiseRate(double remiseRate){
        this.remiseRate=remiseRate;
    }

    public boolean getIsProfessionel() {
        return isProfessionel;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAdress(String adress) {
        this.address = adress;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public void setProfessionel(boolean professionel) {
        isProfessionel = professionel;
    }



}
