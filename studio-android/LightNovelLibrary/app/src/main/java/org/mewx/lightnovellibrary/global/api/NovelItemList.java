package org.mewx.lightnovellibrary.global.api;

import android.util.Log;

import org.mewx.lightnovellibrary.global.GlobalConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MewX on 2015/1/21.
 */
public class NovelItemList {


    List<Integer> l;
    int currentPage;
    int totalPage;
    int lastRecord; // this save the last add number for reversing operation

    // Variables
    private boolean parseStatus; // default false

    /**
     * Init the whole struct with the received XML string
     *
     * @param str only str[0] is available, because I use array for pass by reference
     */
    public NovelItemList(String[] str, int page) {
        setNovelItemList(str, page);
        return;
    }

    public NovelItemList() {
        // init all values
        parseStatus = false;
        currentPage = 1;
        totalPage = 1;
        return;
    }

    public void setNovelItemList(String[] str, int page) {
        parseStatus = parseNovelItemList(str, page);
        return;
    }

    /**
     * get parse status
     *
     * @return true - parsed, else failed.
     */
    public boolean getParseStatus() {
        return parseStatus;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public List<Integer> getNovelItemList() {
        return l;
    }

    /**
     * Parse XML novel item list file
     *
     * @param str
     * @return
     */
    private boolean parseNovelItemList(String[] str, int page) {
        // <?xml version="1.0" encoding="utf-8"?>
        // <result>
        // <page num='166'/>
        // <item aid='1143'/>
        // <item aid='1034'/>
        // <item aid='1213'/>
        // <item aid='1'/>
        // <item aid='1011'/>
        // <item aid='1192'/>
        // <item aid='433'/>
        // <item aid='47'/>
        // <item aid='7'/>
        // <item aid='374'/>
        // </result>

        final char SEPERATOR = '\''; // seperator
        //if( page > totalPage ) return true;
        currentPage = page;
        if(l!=null) {
            lastRecord = l.size()==0?0:l.size()/10*10; // save last size
            if(GlobalConfig.inDebugMode())
                Log.i("MewX","set lastRecord: "+Integer.toString(lastRecord));
        }
        else
        lastRecord = 0;

        // get total page
        int beg = 0, temp;
        beg = str[0].indexOf(SEPERATOR);
        temp = str[0].indexOf(SEPERATOR, beg + 1);
        if (beg == -1 || temp == -1) return false; // this is an exception
        totalPage = Integer.parseInt(str[0].substring(beg + 1, temp));
        if (GlobalConfig.inDebugMode())
            Log.v("MewX", "TotalPage = " + totalPage + "; CurrentPage = " + currentPage + ".");
        beg = temp + 1; // prepare for loop

        // init array
        if(l==null)
            l = new ArrayList<Integer>();
        while (true) {
            beg = str[0].indexOf(SEPERATOR, beg);
            temp = str[0].indexOf(SEPERATOR, beg + 1);
            if (beg == -1 || temp == -1) break;

            l.add(Integer.parseInt(str[0].substring(beg + 1, temp)));
            if (GlobalConfig.inDebugMode())
                Log.v("MewX", "Add novel aid: " + l.get(l.size() - 1));

            beg = temp + 1; // prepare for next round
        }

        return true;
    }

    public void requestForReverse(){
        for( int i = lastRecord; i < l.size(); i ++ )
            l.remove(lastRecord);
    }
}
