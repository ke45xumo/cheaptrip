package com.example.cheaptrip.models.fueleconomy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;


@Root(name = "vehicle",strict = false)
public class Vehicle
{
    @Element(name="barrels08", required = false)
    //@Path("vehicles")
    private String barrels08;

    @Element(name="barrelsA08", required = false)
    //@Path("vehicles")
    private String barrelsA08;

    @Element(name="fuelType2", required = false)
    //@Path("vehicles")
    private String fuelType2;

    @Element(name="year", required = false)
    //@Path("vehicles")
    private String year;

    @Element(name="combA08U", required = false)
    //@Path("vehicles")
    private String combA08U;

    @Element(name="fuelType1", required = false)
    //@Path("vehicles")
    private String fuelType1;

    @Element(name="highway08U", required = false)
    //@Path("vehicles")
    private String highway08U;

    @Element(name="comb08U", required = false)
    //@Path("vehicles")
    private String comb08U;

    @Element(name="UCityA", required = false)
    //@Path("vehicles")
    private String UCityA;

    @Element(name="eng_dscr", required = false)
    //@Path("vehicles")
    private String eng_dscr;

    @Element(name="startStop", required = false)
    //@Path("vehicles")
    private String startStop;

    @Element(name="guzzler", required = false)
    //@Path("vehicles")
    private String guzzler;

    @Element(name="co2A", required = false)
    //@Path("vehicles")
    private String co2A;

    @Element(name="c240bDscr" ,required = false)
    //@Path("vehicles")
    private String c240bDscr;

    @Element(name="phevHwy", required = false)
    //@Path("vehicles")
    private String phevHwy;

    @Element(name="model", required = false)
    //@Path("vehicles")
    private String model;

    @Element(name="id", required = false)
    //@Path("vehicles")
    private String id;

    @Element(name="cityE", required = false)
    //@Path("vehicles")
    private String cityE;

    @Element(name="mpgData", required = false)
    //@Path("vehicles")
    private String mpgData;

    @Element(name="highwayUF", required = false)
    //@Path("vehicles")
    private String highwayUF;

    @Element(name="highwayA08U", required = false)
    //@Path("vehicles")
    private String highwayA08U;

    @Element(name="combinedCD", required = false)
    //@Path("vehicles")
    private String combinedCD;

    @Element(name="sCharger", required = false)
    //@Path("vehicles")
    private String sCharger;

    @Element(name="atvType",required = false)
    //@Path("vehicles")
    private String atvType;

    @Element(name="ghgScore", required = false)
    //@Path("vehicles")
    private String ghgScore;

    @Element(name="highway08", required = false)
    //@Path("vehicles")
    private String highway08;

    @Element(name="cylinders", required = false)
    //@Path("vehicles")
    private String cylinders;

    @Element(name="UCity", required = false)
    //@Path("vehicles")
    private String UCity;

    @Element(name="phevCity", required = false)
    //@Path("vehicles")
    private String phevCity;

    @Element(name="rangeA", required = false)
    //@Path("vehicles")
    private String rangeA;

    @Element(name="displ", required = false)
    //@Path("vehicles")
    private String displ;

    @Element(name="rangeCityA", required = false)
    //@Path("vehicles")
    private String rangeCityA;

    @Element(name="rangeHwyA", required = false)
    //@Path("vehicles")
    private String rangeHwyA;

    @Element(name="trany", required = false)
    //@Path("vehicles")
    private String trany;

    @Element(name="drive", required = false)
    //@Path("vehicles")
    private String drive;

    @Element(name="mpgRevised", required = false)
    //@Path("vehicles")
    private String mpgRevised;

    @Element(name="combE", required = false)
    //@Path("vehicles")
    private String combE;

    @Element(name="fuelCost08", required = false)
    //@Path("vehicles")
    private String fuelCost08;

    @Element(name="highwayE", required = false)
    //@Path("vehicles")
    private String highwayE;

    @Element(name="c240Dscr",required = false)
    //@Path("vehicles")
    private String c240Dscr;

    @Element(name="feScore", required = false)
    //@Path("vehicles")
    private String feScore;

    @Element(name="co2", required = false)
    //@Path("vehicles")
    private String co2;

    @Element(name="combA08", required = false)
    //@Path("vehicles")
    private String combA08;

    @Element(name="range", required = false)
    //@Path("vehicles")
    private String range;

    @Element(name="co2TailpipeAGpm", required = false)
    //@Path("vehicles")
    private String co2TailpipeAGpm;

    @Element(name="createdOn", required = false)
    //@Path("vehicles")
    private String createdOn;

    @Element(name="phevComb", required = false)
    //@Path("vehicles")
    private String phevComb;

    @Element(name="cityUF", required = false)
    //@Path("vehicles")
    private String cityUF;

    @Element(name="city08U", required = false)
    //@Path("vehicles")
    private String city08U;

    @Element(name="modifiedOn", required = false)
    //@Path("vehicles")
    private String modifiedOn;

    @Element(name="trans_dscr", required = false)
    //@Path("vehicles")
    private String trans_dscr;

    @Element(name="highwayCD", required = false)
    //@Path("vehicles")
    private String highwayCD;

    @Element(name="co2TailpipeGpm", required = false)
    //@Path("vehicles")
    private String co2TailpipeGpm;

    @Element(name="combinedUF", required = false)
    //@Path("vehicles")
    private String combinedUF;

    @Element(name="engId", required = false)
    //@Path("vehicles")
    private String engId;

    @Element(name="ghgScoreA", required = false)
    //@Path("vehicles")
    private String ghgScoreA;

    @Element(name="rangeHwy", required = false)
    //@Path("vehicles")
    private String rangeHwy;

    @Element(name="make", required = false)
    //@Path("vehicles")
    private String make;

    @Element(name="charge240b", required = false)
    //@Path("vehicles")
    private String charge240b;

    @Element(name="city08", required = false)
    //@Path("vehicles")
    private String city08;

    @Element(name="phevBlended", required = false)
    //@Path("vehicles")
    private String phevBlended;

    @Element(name="cityCD", required = false)
    //@Path("vehicles")
    private String cityCD;

    @Element(name="VClass", required = false)
    //@Path("vehicles")
    private String VClass;

    @Element(name="fuelCostA08", required = false)
    //@Path("vehicles")
    private String fuelCostA08;

    @Element(name="rangeCity", required = false)
    //@Path("vehicles")
    private String rangeCity;

    @Element(name="tCharger", required = false)
    //@Path("vehicles")
    private String tCharger;

    @Element(name="evMotor", required = false)
    //@Path("vehicles")
    private String evMotor;

    @Element(name="lv2", required = false)
    //@Path("vehicles")
    private String lv2;

    @Element(name="charge240", required = false)
    //@Path("vehicles")
    private String charge240;

    @Element(name="charge120", required = false)
    //@Path("vehicles")
    private String charge120;

    @Element(name="cityA08U", required = false)
    //@Path("vehicles")
    private String cityA08U;

    @Element(name="lv4", required = false)
    //@Path("vehicles")
    private String lv4;

    @Element(name="pv2", required = false)
    //@Path("vehicles")
    private String pv2;

    @Element(name="cityA08", required = false)
    //@Path("vehicles")
    private String cityA08;

    @Element(name="hpv", required = false)
    //@Path("vehicles")
    private String hpv;

    @Element(name="pv4", required = false)
    //@Path("vehicles")
    private String pv4;

    @Element(name="youSaveSpend", required = false)
    //@Path("vehicles")
    private String youSaveSpend;

    @Element(name="hlv", required = false)
    //@Path("vehicles")
    private String hlv;

    @Element(name="fuelType", required = false)
    //@Path("vehicles")
    private String fuelType;

    @Element(name="highwayA08", required = false)
    //@Path("vehicles")
    private String highwayA08;

    @Element(name="UHighway", required = false)
    //@Path("vehicles")
    private String UHighway;

    @Element(name="comb08", required = false)
    //@Path("vehicles")
    private String comb08;

    @Element(name="UHighwayA", required = false)
    //@Path("vehicles")
    private String UHighwayA;

    @Element(name="mfrCode", required = false)
    //@Path("vehicles")
    private String mfrCode;

    public String getBarrels08 ()
    {
        return barrels08;
    }

    public void setBarrels08 (String barrels08)
    {
        this.barrels08 = barrels08;
    }

    public String getBarrelsA08 ()
    {
        return barrelsA08;
    }

    public void setBarrelsA08 (String barrelsA08)
    {
        this.barrelsA08 = barrelsA08;
    }

    public String getFuelType2 ()
    {
        return fuelType2;
    }

    public void setFuelType2 (String fuelType2)
    {
        this.fuelType2 = fuelType2;
    }

    public String getYear ()
    {
        return year;
    }

    public void setYear (String year)
    {
        this.year = year;
    }

    public String getCombA08U ()
    {
        return combA08U;
    }

    public void setCombA08U (String combA08U)
    {
        this.combA08U = combA08U;
    }

    public String getFuelType1 ()
    {
        return fuelType1;
    }

    public void setFuelType1 (String fuelType1)
    {
        this.fuelType1 = fuelType1;
    }

    public String getHighway08U ()
    {
        return highway08U;
    }

    public void setHighway08U (String highway08U)
    {
        this.highway08U = highway08U;
    }

    public String getComb08U ()
    {
        return comb08U;
    }

    public void setComb08U (String comb08U)
    {
        this.comb08U = comb08U;
    }

    public String getUCityA ()
    {
        return UCityA;
    }

    public void setUCityA (String UCityA)
    {
        this.UCityA = UCityA;
    }

    public String getEng_dscr ()
    {
        return eng_dscr;
    }

    public void setEng_dscr (String eng_dscr)
    {
        this.eng_dscr = eng_dscr;
    }

    public String getStartStop ()
    {
        return startStop;
    }

    public void setStartStop (String startStop)
    {
        this.startStop = startStop;
    }

    public String getGuzzler ()
    {
        return guzzler;
    }

    public void setGuzzler (String guzzler)
    {
        this.guzzler = guzzler;
    }

    public String getCo2A ()
    {
        return co2A;
    }

    public void setCo2A (String co2A)
    {
        this.co2A = co2A;
    }

    public String getC240bDscr ()
    {
        return c240bDscr;
    }

    public void setC240bDscr (String c240bDscr)
    {
        this.c240bDscr = c240bDscr;
    }

    public String getPhevHwy ()
    {
        return phevHwy;
    }

    public void setPhevHwy (String phevHwy)
    {
        this.phevHwy = phevHwy;
    }

    public String getModel ()
    {
        return model;
    }

    public void setModel (String model)
    {
        this.model = model;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getCityE ()
    {
        return cityE;
    }

    public void setCityE (String cityE)
    {
        this.cityE = cityE;
    }

    public String getMpgData ()
    {
        return mpgData;
    }

    public void setMpgData (String mpgData)
    {
        this.mpgData = mpgData;
    }

    public String getHighwayUF ()
    {
        return highwayUF;
    }

    public void setHighwayUF (String highwayUF)
    {
        this.highwayUF = highwayUF;
    }

    public String getHighwayA08U ()
    {
        return highwayA08U;
    }

    public void setHighwayA08U (String highwayA08U)
    {
        this.highwayA08U = highwayA08U;
    }

    public String getCombinedCD ()
    {
        return combinedCD;
    }

    public void setCombinedCD (String combinedCD)
    {
        this.combinedCD = combinedCD;
    }

    public String getSCharger ()
    {
        return sCharger;
    }

    public void setSCharger (String sCharger)
    {
        this.sCharger = sCharger;
    }

    public String getAtvType ()
    {
        return atvType;
    }

    public void setAtvType (String atvType)
    {
        this.atvType = atvType;
    }

    public String getGhgScore ()
    {
        return ghgScore;
    }

    public void setGhgScore (String ghgScore)
    {
        this.ghgScore = ghgScore;
    }

    public String getHighway08 ()
    {
        return highway08;
    }

    public void setHighway08 (String highway08)
    {
        this.highway08 = highway08;
    }

    public String getCylinders ()
    {
        return cylinders;
    }

    public void setCylinders (String cylinders)
    {
        this.cylinders = cylinders;
    }

    public String getUCity ()
    {
        return UCity;
    }

    public void setUCity (String UCity)
    {
        this.UCity = UCity;
    }

    public String getPhevCity ()
    {
        return phevCity;
    }

    public void setPhevCity (String phevCity)
    {
        this.phevCity = phevCity;
    }

    public String getRangeA ()
    {
        return rangeA;
    }

    public void setRangeA (String rangeA)
    {
        this.rangeA = rangeA;
    }

    public String getDispl ()
    {
        return displ;
    }

    public void setDispl (String displ)
    {
        this.displ = displ;
    }

    public String getRangeCityA ()
    {
        return rangeCityA;
    }

    public void setRangeCityA (String rangeCityA)
    {
        this.rangeCityA = rangeCityA;
    }

    public String getRangeHwyA ()
    {
        return rangeHwyA;
    }

    public void setRangeHwyA (String rangeHwyA)
    {
        this.rangeHwyA = rangeHwyA;
    }

    public String getTrany ()
    {
        return trany;
    }

    public void setTrany (String trany)
    {
        this.trany = trany;
    }

    public String getDrive ()
    {
        return drive;
    }

    public void setDrive (String drive)
    {
        this.drive = drive;
    }

    public String getMpgRevised ()
    {
        return mpgRevised;
    }

    public void setMpgRevised (String mpgRevised)
    {
        this.mpgRevised = mpgRevised;
    }

    public String getCombE ()
    {
        return combE;
    }

    public void setCombE (String combE)
    {
        this.combE = combE;
    }

    public String getFuelCost08 ()
    {
        return fuelCost08;
    }

    public void setFuelCost08 (String fuelCost08)
    {
        this.fuelCost08 = fuelCost08;
    }

    public String getHighwayE ()
    {
        return highwayE;
    }

    public void setHighwayE (String highwayE)
    {
        this.highwayE = highwayE;
    }

    public String getC240Dscr ()
    {
        return c240Dscr;
    }

    public void setC240Dscr (String c240Dscr)
    {
        this.c240Dscr = c240Dscr;
    }

    public String getFeScore ()
    {
        return feScore;
    }

    public void setFeScore (String feScore)
    {
        this.feScore = feScore;
    }

    public String getCo2 ()
    {
        return co2;
    }

    public void setCo2 (String co2)
    {
        this.co2 = co2;
    }

    public String getCombA08 ()
    {
        return combA08;
    }

    public void setCombA08 (String combA08)
    {
        this.combA08 = combA08;
    }

    public String getRange ()
    {
        return range;
    }

    public void setRange (String range)
    {
        this.range = range;
    }

    public String getCo2TailpipeAGpm ()
    {
        return co2TailpipeAGpm;
    }

    public void setCo2TailpipeAGpm (String co2TailpipeAGpm)
    {
        this.co2TailpipeAGpm = co2TailpipeAGpm;
    }

    public String getCreatedOn ()
    {
        return createdOn;
    }

    public void setCreatedOn (String createdOn)
    {
        this.createdOn = createdOn;
    }

    public String getPhevComb ()
    {
        return phevComb;
    }

    public void setPhevComb (String phevComb)
    {
        this.phevComb = phevComb;
    }

    public String getCityUF ()
    {
        return cityUF;
    }

    public void setCityUF (String cityUF)
    {
        this.cityUF = cityUF;
    }

    public String getCity08U ()
    {
        return city08U;
    }

    public void setCity08U (String city08U)
    {
        this.city08U = city08U;
    }

    public String getModifiedOn ()
    {
        return modifiedOn;
    }

    public void setModifiedOn (String modifiedOn)
    {
        this.modifiedOn = modifiedOn;
    }

    public String getTrans_dscr ()
    {
        return trans_dscr;
    }

    public void setTrans_dscr (String trans_dscr)
    {
        this.trans_dscr = trans_dscr;
    }

    public String getHighwayCD ()
    {
        return highwayCD;
    }

    public void setHighwayCD (String highwayCD)
    {
        this.highwayCD = highwayCD;
    }

    public String getCo2TailpipeGpm ()
    {
        return co2TailpipeGpm;
    }

    public void setCo2TailpipeGpm (String co2TailpipeGpm)
    {
        this.co2TailpipeGpm = co2TailpipeGpm;
    }

    public String getCombinedUF ()
    {
        return combinedUF;
    }

    public void setCombinedUF (String combinedUF)
    {
        this.combinedUF = combinedUF;
    }

    public String getEngId ()
    {
        return engId;
    }

    public void setEngId (String engId)
    {
        this.engId = engId;
    }

    public String getGhgScoreA ()
    {
        return ghgScoreA;
    }

    public void setGhgScoreA (String ghgScoreA)
    {
        this.ghgScoreA = ghgScoreA;
    }

    public String getRangeHwy ()
    {
        return rangeHwy;
    }

    public void setRangeHwy (String rangeHwy)
    {
        this.rangeHwy = rangeHwy;
    }

    public String getMake ()
    {
        return make;
    }

    public void setMake (String make)
    {
        this.make = make;
    }

    public String getCharge240b ()
    {
        return charge240b;
    }

    public void setCharge240b (String charge240b)
    {
        this.charge240b = charge240b;
    }

    public String getCity08 ()
    {
        return city08;
    }

    public void setCity08 (String city08)
    {
        this.city08 = city08;
    }

    public String getPhevBlended ()
    {
        return phevBlended;
    }

    public void setPhevBlended (String phevBlended)
    {
        this.phevBlended = phevBlended;
    }

    public String getCityCD ()
    {
        return cityCD;
    }

    public void setCityCD (String cityCD)
    {
        this.cityCD = cityCD;
    }

    public String getVClass ()
    {
        return VClass;
    }

    public void setVClass (String VClass)
    {
        this.VClass = VClass;
    }

    public String getFuelCostA08 ()
    {
        return fuelCostA08;
    }

    public void setFuelCostA08 (String fuelCostA08)
    {
        this.fuelCostA08 = fuelCostA08;
    }

    public String getRangeCity ()
    {
        return rangeCity;
    }

    public void setRangeCity (String rangeCity)
    {
        this.rangeCity = rangeCity;
    }

    public String getTCharger ()
    {
        return tCharger;
    }

    public void setTCharger (String tCharger)
    {
        this.tCharger = tCharger;
    }

    public String getEvMotor ()
    {
        return evMotor;
    }

    public void setEvMotor (String evMotor)
    {
        this.evMotor = evMotor;
    }

    public String getLv2 ()
    {
        return lv2;
    }

    public void setLv2 (String lv2)
    {
        this.lv2 = lv2;
    }

    public String getCharge240 ()
    {
        return charge240;
    }

    public void setCharge240 (String charge240)
    {
        this.charge240 = charge240;
    }

    public String getCharge120 ()
    {
        return charge120;
    }

    public void setCharge120 (String charge120)
    {
        this.charge120 = charge120;
    }

    public String getCityA08U ()
    {
        return cityA08U;
    }

    public void setCityA08U (String cityA08U)
    {
        this.cityA08U = cityA08U;
    }

    public String getLv4 ()
    {
        return lv4;
    }

    public void setLv4 (String lv4)
    {
        this.lv4 = lv4;
    }

    public String getPv2 ()
    {
        return pv2;
    }

    public void setPv2 (String pv2)
    {
        this.pv2 = pv2;
    }

    public String getCityA08 ()
    {
        return cityA08;
    }

    public void setCityA08 (String cityA08)
    {
        this.cityA08 = cityA08;
    }

    public String getHpv ()
    {
        return hpv;
    }

    public void setHpv (String hpv)
    {
        this.hpv = hpv;
    }

    public String getPv4 ()
    {
        return pv4;
    }

    public void setPv4 (String pv4)
    {
        this.pv4 = pv4;
    }

    public String getYouSaveSpend ()
    {
        return youSaveSpend;
    }

    public void setYouSaveSpend (String youSaveSpend)
    {
        this.youSaveSpend = youSaveSpend;
    }

    public String getHlv ()
    {
        return hlv;
    }

    public void setHlv (String hlv)
    {
        this.hlv = hlv;
    }

    public String getFuelType ()
    {
        return fuelType;
    }

    public void setFuelType (String fuelType)
    {
        this.fuelType = fuelType;
    }

    public String getHighwayA08 ()
    {
        return highwayA08;
    }

    public void setHighwayA08 (String highwayA08)
    {
        this.highwayA08 = highwayA08;
    }

    public String getUHighway ()
    {
        return UHighway;
    }

    public void setUHighway (String UHighway)
    {
        this.UHighway = UHighway;
    }

    public String getComb08 ()
    {
        return comb08;
    }

    public void setComb08 (String comb08)
    {
        this.comb08 = comb08;
    }

    public String getUHighwayA ()
    {
        return UHighwayA;
    }

    public void setUHighwayA (String UHighwayA)
    {
        this.UHighwayA = UHighwayA;
    }

    public String getMfrCode ()
    {
        return mfrCode;
    }

    public void setMfrCode (String mfrCode)
    {
        this.mfrCode = mfrCode;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [barrels08 = "+barrels08+", barrelsA08 = "+barrelsA08+", fuelType2 = "+fuelType2+", year = "+year+", combA08U = "+combA08U+", fuelType1 = "+fuelType1+", highway08U = "+highway08U+", comb08U = "+comb08U+", UCityA = "+UCityA+", eng_dscr = "+eng_dscr+", startStop = "+startStop+", guzzler = "+guzzler+", co2A = "+co2A+", c240bDscr = "+c240bDscr+", phevHwy = "+phevHwy+", model = "+model+", id = "+id+", cityE = "+cityE+", mpgData = "+mpgData+", highwayUF = "+highwayUF+", highwayA08U = "+highwayA08U+", combinedCD = "+combinedCD+", sCharger = "+sCharger+", atvType = "+atvType+", ghgScore = "+ghgScore+", highway08 = "+highway08+", cylinders = "+cylinders+", UCity = "+UCity+", phevCity = "+phevCity+", rangeA = "+rangeA+", displ = "+displ+", rangeCityA = "+rangeCityA+", rangeHwyA = "+rangeHwyA+", trany = "+trany+", drive = "+drive+", mpgRevised = "+mpgRevised+", combE = "+combE+", fuelCost08 = "+fuelCost08+", highwayE = "+highwayE+", c240Dscr = "+c240Dscr+", feScore = "+feScore+", co2 = "+co2+", combA08 = "+combA08+", range = "+range+", co2TailpipeAGpm = "+co2TailpipeAGpm+", createdOn = "+createdOn+", phevComb = "+phevComb+", cityUF = "+cityUF+", city08U = "+city08U+", modifiedOn = "+modifiedOn+", trans_dscr = "+trans_dscr+", highwayCD = "+highwayCD+", co2TailpipeGpm = "+co2TailpipeGpm+", combinedUF = "+combinedUF+", engId = "+engId+", ghgScoreA = "+ghgScoreA+", rangeHwy = "+rangeHwy+", make = "+make+", charge240b = "+charge240b+", city08 = "+city08+", phevBlended = "+phevBlended+", cityCD = "+cityCD+", VClass = "+VClass+", fuelCostA08 = "+fuelCostA08+", rangeCity = "+rangeCity+", tCharger = "+tCharger+", evMotor = "+evMotor+", lv2 = "+lv2+", charge240 = "+charge240+", charge120 = "+charge120+", cityA08U = "+cityA08U+", lv4 = "+lv4+", pv2 = "+pv2+", cityA08 = "+cityA08+", hpv = "+hpv+", pv4 = "+pv4+", youSaveSpend = "+youSaveSpend+", hlv = "+hlv+", fuelType = "+fuelType+", highwayA08 = "+highwayA08+", UHighway = "+UHighway+", comb08 = "+comb08+", UHighwayA = "+UHighwayA+", mfrCode = "+mfrCode+"]";
    }
}