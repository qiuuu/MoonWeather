package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

import java.util.ArrayList;
import java.util.List;

public class GreenDaoGenerate {
    public static void main(String[] args) throws Exception{
        Schema schema = new Schema(3,"com.pangge.moontest");
        addWeather(schema);

        //add a city id
        addCity(schema);

        new DaoGenerator().generateAll(schema,"../MoonWeather/app/src/main/java-gen");



    }
    private static void addCity(Schema schema){
        Entity city = schema.addEntity("City");
        city.addIdProperty();
        city.addStringProperty("d1").notNull();
        city.addStringProperty("d2").notNull();
        city.addStringProperty("d3").notNull();
        city.addStringProperty("d4").notNull();

    }
    private static void addWeather(Schema schema){
        //Entity weather = schema.addEntity("Weather");
        List<Entity> entityList = new ArrayList<>();


        for(int i = 1; i<6; i++){
            entityList.add(schema.addEntity("Weather"+i));

        }
        int j = 1;
        for(Entity weather : entityList){

            //weather = schema.addEntity("weather"+j);
            weather.addIdProperty();
            weather.addStringProperty("date");
            weather.addStringProperty("type");
            weather.addStringProperty("high");
            weather.addStringProperty("low");
            weather.addStringProperty("fengxiang");
            weather.addStringProperty("fengli");
            weather.addStringProperty("city");
            weather.addStringProperty("wendu");
            weather.addStringProperty("ganmao");

            weather.addContentProvider();
            j++;

        }



    }
}
