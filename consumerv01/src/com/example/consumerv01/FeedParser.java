package com.example.consumerv01;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class FeedParser {

	private static final String ns = null;
	   
    public List<Entry> parse(InputStream in , Activity activity) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, null);
            parser.nextTag();
            List<Entry> yo = readFeed(parser ,  activity);
            return yo;
        }
        catch (IOException e)
        {
        	e.printStackTrace();
        	return null;
        }
        catch (XmlPullParserException e)
        {
        	e.printStackTrace();
        	return null;
        }

    }

    private List<Entry> readFeed(XmlPullParser parser , Activity activity) throws XmlPullParserException, IOException {
        List<Entry>  entries = new ArrayList();

        try{
        parser.require(XmlPullParser.START_TAG, ns, "ArrayOfMerchant");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Merchant")) {
                entries.add(readEntry(parser , activity));
            } else {
                skip(parser);
            }
        }  
        }
        catch(XmlPullParserException e)
        {
        	e.printStackTrace();
        }
        return entries;
    }
    

      
    // Parses the contents of an entry. If it encounters a name, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private Entry readEntry(XmlPullParser parser , Activity activity) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Merchant");
        String name = null;
        Map<String, String> info= new HashMap<String, String>();
        String[] infoString = activity.getResources().getStringArray(R.array.merchant_xml);
        ArrayList<String> infoNeeded =  new ArrayList<String>();
        
        for(String item : infoString ){
        	infoNeeded.add(item);
        	}

        try{
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String title = parser.getName();
            if (title.equals("Name")) {
                name = readInfo(parser, title);
            } else if (infoNeeded.contains(title)) {
            	info.put(title, readInfo(parser, title)) ;
            } else {
                skip(parser);
            }
        }
        }
        catch(XmlPullParserException e)
        {
        	e.printStackTrace();
        }
        return new Entry(name, info);
    }
      
    // Processes summary tags in the feed.
    private String readInfo(XmlPullParser parser , String startTag) throws IOException, XmlPullParserException {
    	
    	String summary=null;
    	try{
    		parser.require(XmlPullParser.START_TAG, ns, startTag);
            summary = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, startTag);
            return summary;
    	}
    	catch(XmlPullParserException e)
        {
        	e.printStackTrace();
        }

        return summary;
    }

    // For the tags name and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        try{
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
    	}
    	catch(XmlPullParserException e)
        {
        	e.printStackTrace();
        }
        return result;
    }
    
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                depth--;
                break;
            case XmlPullParser.START_TAG:
                depth++;
                break;
            }
        }
     }
}
