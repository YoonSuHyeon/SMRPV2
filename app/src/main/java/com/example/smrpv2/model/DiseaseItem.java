package com.example.smrpv2.model;

public class DiseaseItem {

    private String Str_symptom;
    private String Str_symptomContent;
    private String Str_disease;
    private String Str_probability;
    private String Str_department;
    private int viewType;


    public void setSymptom(String s){Str_symptom = s;}
    public String getSymptom(){return Str_symptom;}

    public void setSymptomContent(String s){Str_symptomContent = s;}
    public String getSymptomContent(){return Str_symptomContent;}

    public void setDisease(String d){Str_disease = d;}
    public String getDisease(){return Str_disease;}

    public void setProbability(String p){Str_probability = p;}
    public String getProbability(){return Str_probability;}

    public void setDepartment(String d){Str_department = d;}
    public String getDepartment(){return Str_department;}

    public void setViewType(int v){viewType = v;}
    public int getViewType(){return viewType;}

}