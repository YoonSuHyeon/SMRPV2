package com.example.smrpv2.model.home_model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "item")
public class Covid19_item {

    /*@Element(name = "careCnt",required = false) //치료 환자수
    String careCnt;*/
    @Element(name = "clearCnt") //격리 해제수
    String clearCnt;
    @Element(name = "createDt")
    String createDt;


    @Element(name = "deathCnt")//사망자 수
    String deathCnt;
    @Element(name = "decideCnt")//확진자 수
    String decideCnt;
    @Element(name = "examCnt")//누적검사수
    String examCnt;

    public String getClearCnt() {
        return clearCnt;
    }

    public String getDeathCnt() {
        return deathCnt;
    }

    public String getDecideCnt() {
        return decideCnt;
    }

    public String getExamCnt() {
        return examCnt;
    }
    public String getCreateDt() {
        return createDt;
    }

    /*<accDefRate>1.0903373809</accDefRate>
                <accExamCnt>2966405</accExamCnt>
                <accExamCompCnt>2910567</accExamCompCnt>
                <careCnt>4397</careCnt>
                <clearCnt>26825</clearCnt>
                <createDt>2020-11-25 09:32:47.722</createDt>
                <deathCnt>513</deathCnt>
                <decideCnt>31735</decideCnt>
                <examCnt>55838</examCnt>
                <resutlNegCnt>2878832</resutlNegCnt>
                <seq>333</seq>
                <stateDt>20201125</stateDt>
                <stateTime>00:00</stateTime>
                <updateDt>null</updateDt>*/
}
