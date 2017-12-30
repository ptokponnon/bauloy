package com.example.parfait.bauloy.residentList;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.parfait.bauloy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ResidentListContent {

    /**
     * An array of sample (dummy) items.
     */
    private static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();
    private static int size;
    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;
    private static Context mcontext;

    public static void populate(){
        // Add some sample items.
        clearItemList();
        SharedPreferences sharedPref = mcontext.getSharedPreferences(
                ""+R.string.preference_file_resident, Context.MODE_PRIVATE);
        String defaultValue = "defaultResident";
        int position = 1;
        String resident = sharedPref.getString(position+"", defaultValue);
        while (!resident.equals(defaultValue)){
            addItem(createDummyItem(position++, resident));
            resident = sharedPref.getString(position+"", defaultValue);;
        }
        size = position;
    }

    private static void clearItemList(){
        ITEMS.clear();
        ITEM_MAP.clear();
    }
    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position, String resident) {
        if(resident.split(":").length != 3)
            return new DummyItem("0", "0", "0");
        String name = resident.split(":")[0];
        String phone = resident.split(":")[1];
        String birthday = resident.split(":")[2];

        StringBuilder builder = new StringBuilder();
        builder.append("Détails au sujet de "+name+" : ").append("\n"+"Phone : "+phone+"\n"+"Date de Naissance : "+birthday);
        return new DummyItem(String.valueOf(position), name, builder.toString());
    }

    public ResidentListContent(Context context){
        mcontext = context;
        populate();
    }

    public static List<DummyItem> getItems(){
        return ITEMS;
    }

    public static int getSize(){
        return size;
    }
    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
