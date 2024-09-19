package com.example.selectametalsclad;

public class Zametki {
    public Zametki() {
    }

    public String getNameZametki() {
        return nameZametki;
    }

    public void setNameZametki(String id,String nameZametki) {
        this.nameZametki = nameZametki;
    }

    public String getTextZametki() {
        return textZametki;
    }

    public void setNameZametki(String nameZametki) {
        this.nameZametki = nameZametki;
    }

    public String getIdZametki() {
        return IdZametki;
    }

    public void setIdZametki(String idZametki) {
        IdZametki = idZametki;
    }

    public void setTextZametki(String textZametki) {
        this.textZametki = textZametki;
    }

    public Zametki(String idZametki,String nameZametki, String textZametki) {
        this.nameZametki = nameZametki;
        this.textZametki = textZametki;
        this.IdZametki = idZametki;
    }

    String nameZametki,textZametki,IdZametki;
}
