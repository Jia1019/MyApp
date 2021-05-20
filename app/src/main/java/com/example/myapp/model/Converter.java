package com.example.myapp.model;

import androidx.room.TypeConverter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Converter {
    @TypeConverter
    public static Timestamp stringToTimestamp(String s)
    {
        if (s!=null)
        {
            return Timestamp.valueOf(s);
        }
        return null;

    }

    @TypeConverter
    public static String timestampToString(Timestamp timestamp)
    {
        if (timestamp!=null)
        {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);

        }
        return null;
    }


}
