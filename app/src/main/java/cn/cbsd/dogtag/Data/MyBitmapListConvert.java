package cn.cbsd.dogtag.Data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MyBitmapListConvert implements PropertyConverter<List<String>, String> {

    private final Gson mGson = new Gson();

    @Override
    public List<String> convertToEntityProperty(String databaseValue) {

        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> itemList = mGson.fromJson(databaseValue, type);
        return itemList;
    }


    @Override
    public String convertToDatabaseValue(List<String> entityProperty) {
        String dbString = mGson.toJson(entityProperty);
        return dbString;
    }

}
