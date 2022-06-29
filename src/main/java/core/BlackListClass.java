package core;

import java.util.ArrayList;

public class BlackListClass {
    public static ArrayList<String> black_list = new ArrayList<String>();
    public BlackListClass(){
        black_list.add("org.apache.catalina.core.ApplicationFilterChain");
        black_list.add("javax.servlet.http.HttpServlet"); //冰蝎马
        checkBlackList();
    }
    public void addBlackList(String evilClassPath){
        evilClassPath = evilClassPath.replace("/",".");
        black_list.add(evilClassPath);
        checkBlackList();
    }

    public void checkBlackList(){
        //检查黑名单中是否存在有白名单的类，存在则删除
        WhiteListClass wlc;
        ArrayList<String> white_list = WhiteListClass.white_list;
        for(int i = 0;i<white_list.size();i++){
            if(black_list.contains(white_list.get(i))){
                black_list.remove(white_list.get(i));
            }
        }
    }
}
