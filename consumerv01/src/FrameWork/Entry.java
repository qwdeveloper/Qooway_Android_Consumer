package FrameWork;

import java.util.ArrayList;
import java.util.Map;

    public class Entry {
        public final String name;
        public final Map attribute;
        public final Map arrayAttribute;
        public static String[] inputElements = null;
        
        public Entry(String name, Map<String, String> attribute,  Map<String, ArrayList<String>> arrayAttribute) {
            this.name = name;
            this.attribute = attribute;
            this.arrayAttribute = arrayAttribute;
        }
    }

