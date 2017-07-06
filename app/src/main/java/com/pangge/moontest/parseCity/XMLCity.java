package com.pangge.moontest.parseCity;



import com.pangge.moontest.City;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iuuu on 17/6/24.
 */

@Root(name = "xml", strict = false)

public class XMLCity {
    @Attribute(name = "c1")
    @Path("c")
    public String id;


    @ElementList(required = true, entry = "d", inline = true)
    @Path("c")

    public List<City> cities;
    public List<City> getCityList(){
        if(cities == null){
            cities = new ArrayList<>();

        }
        return this.cities;
    }

}
