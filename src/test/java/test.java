import core.BlackListClass;

import java.io.File;
import java.util.ArrayList;

public class test {
    public static void main(String[] args) throws Exception{
        test test = new test();
        BlackListClass blc = new BlackListClass();
        ArrayList bl = blc.black_list;
        System.out.println(bl.get(0));
        System.out.println(bl.contains("org.apache.catalina.core.ApplicationFilterChain"));
        test.getPath();
        //System.out.println(test.getClass().getClassLoader().getResource("lib"+File.separator+"tools.jar"));
    }
    public void getPath() throws Exception{
        String tools_path;
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win")){
            tools_path = System.getProperty("java.home").replace("jre", "lib") + "\\" + "tools.jar";
        }else{
            tools_path = System.getProperty("java.home").replace("jre", "lib") + File.separator + "tools.jar";
        }
        System.out.println("我手填的："+"C:\\Program Files\\Java\\jdk1.8.0_202\\lib\\tools.jar");
        System.out.println("由java自己获取: "+tools_path);
    }
}
