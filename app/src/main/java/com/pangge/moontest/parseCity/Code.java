package com.pangge.moontest.parseCity;

import com.pangge.moontest.City;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by iuuu on 17/6/24.
 * 删了
 * 统一使用City
 * List<City></City>
 */

@Root(name = "d", strict = false)
public class Code{


    @Attribute(name = "d1",required = false)
    public String d1;

    @Attribute(name = "d2",required = false)
    public String d2;
    @Attribute(name = "d3",required = false)
    public String d3;
    @Attribute(name = "d4",required = false)
    public String d4;

}
