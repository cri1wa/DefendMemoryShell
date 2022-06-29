package core;

import java.util.ArrayList;

public class WhiteListClass {
    static ArrayList<String> white_list = new ArrayList<String>();

    public WhiteListClass(){
        addWhiteList("");
    }
    public void addWhiteList(String WhiteList){
        white_list.add(WhiteList);
    }
}
