package com.example.smrpv2.model.searchMed_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * MedicineInforResponDTO : 약 정보들 알려줌
 * 사용처 1. SearchActivity
 * 사용처 2.
 */
public class MedicineInfoRsponDTO {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("itemSeq")
    @Expose
    private String itemSeq;

    @SerializedName("itemName")
    //@Expose
    private String itemName;

    @SerializedName("entpSeq")
    @Expose
    private String entpSeq;

    @SerializedName("entpName")
    @Expose
    private String entpName;

    @SerializedName("chart")
    @Expose
    private String chart;

    @SerializedName("itemImage")
    @Expose
    private String itemImage;

    @SerializedName("printFront")
    @Expose
    private String printFront;
    @SerializedName("printBack")
    @Expose
    private String printBack;
    @SerializedName("drugShape")
    @Expose
    private String drugShape;
    @SerializedName("colorClass1")
    @Expose
    private String colorClass1;
    @SerializedName("colorClass2")
    @Expose
    private String colorClass2;
    @SerializedName("lineFront")
    @Expose
    private String lineFront;
    @SerializedName("lineBack")
    @Expose
    private String lineBack;

    @SerializedName("classNo")
    @Expose
    private String classNo;
    @SerializedName("className")
    @Expose
    private String className;
    @SerializedName("etcOtcName")
    @Expose
    private String etcOtcName;
    @SerializedName("formCodeName")
    @Expose
    private String formCodeName;

    @SerializedName("formula")
    @Expose
    private String formula;

    @SerializedName("effect")
    @Expose
    private String effect;

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }
    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getUsage() {
       return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
   }

    @SerializedName("usage")
    @Expose
    private String usage;

    public String getItemSeq() {
        return itemSeq;
    }

    public void setItemSeq(String itemSeq) {
        this.itemSeq = itemSeq;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getEntpSeq() {
        return entpSeq;
    }

    public void setEntpSeq(String entpSeq) {
        this.entpSeq = entpSeq;
    }

    public String getEntpName() {
        return entpName;
    }

    public void setEntpName(String entpName) {
        this.entpName = entpName;
    }

    public String getChart() {
        return chart;
    }

    public void setChart(String chart) {
        this.chart = chart;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getPrintFront() {
        return printFront;
    }

    public void setPrintFront(String printFront) {
        this.printFront = printFront;
    }

    public String getPrintBack() {
        return printBack;
    }

    public void setPrintBack(String printBack) {
        this.printBack = printBack;
    }

    public String getDrugShape() {
        return drugShape;
    }

    public void setDrugShape(String drugShape) {
        this.drugShape = drugShape;
    }

    public String getColorClass1() {
        return colorClass1;
    }

    public void setColorClass1(String colorClass1) {
        this.colorClass1 = colorClass1;
    }

    public Object getColorClass2() {
        return colorClass2;
    }

    public void setColorClass2(String colorClass2) {
        this.colorClass2 = colorClass2;
    }

    public String getLineFront() {
        return lineFront;
    }

    public void setLineFront(String lineFront) {
        this.lineFront = lineFront;
    }

    public Object getLineBack() {
        return lineBack;
    }

    public void setLineBack(String lineBack) {
        this.lineBack = lineBack;
    }

    public String getClassNo() {
        return classNo;
    }

    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getEtcOtcName() {
        return etcOtcName;
    }

    public void setEtcOtcName(String etcOtcName) {
        this.etcOtcName = etcOtcName;
    }

    public String getFormCodeName() {
        return formCodeName;
    }

    public void setFormCodeName(String formCodeName) {
        this.formCodeName = formCodeName;
    }
}
