package org.wora.entity;

import java.util.List;

public class Client {
    private int id;
    private String name;
    private String adress;
    private String numberPhone;
    private boolean isProfessionel;
    private List<Project> projects;

    public Client(){
    }

    public Client (int id, String name ,String adress,String numberPhone ,boolean isProfessionel){
       this.id=id;
       this.name=name;
       this.adress=adress;
       this.numberPhone=numberPhone;
       this.isProfessionel=isProfessionel;
    }

    public int getId() {
        return id;
    }

    public String getAdress() {
        return adress;
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

    public boolean getIsProfessionel() {
        return isProfessionel;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public void setProfessionel(boolean professionel) {
        isProfessionel = professionel;
    }



}
