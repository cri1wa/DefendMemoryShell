package core;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.VirtualMachine;
public class LoadAgent {
    String targetName;
    String path;
    public LoadAgent(String targetName,String agentPath) {
            this.targetName = targetName;
            this.path = agentPath;
    }

    public void loadAgent2JVM(){
        try{
            String tools_path;
            String os = System.getProperty("os.name").toLowerCase();
            if(os.contains("win")){
                tools_path = System.getProperty("java.home").replace("jre", "lib") + "\\"  + "tools.jar";
                //tools_path = tools_path.replace("\\","\\\\");
            }else{
                tools_path = System.getProperty("java.home").replace("jre", "lib") + File.separator + "tools.jar";
            }
            String tp = "C:\\Program Files\\Java\\jdk1.8.0_202\\lib\\tools.jar";
            java.io.File toolsPath = new java.io.File(tp);
            java.net.URL url = toolsPath.toURI().toURL();
            java.net.URLClassLoader classLoader = new java.net.URLClassLoader(new java.net.URL[]{url});
            Class/*<?>*/ MyVirtualMachine = classLoader.loadClass("com.sun.tools.attach.VirtualMachine");
            Class/*<?>*/ MyVirtualMachineDescriptor = classLoader.loadClass("com.sun.tools.attach.VirtualMachineDescriptor");
            java.lang.reflect.Method listMethod = MyVirtualMachine.getDeclaredMethod("list", null);
            java.util.List/*<Object>*/ list = (java.util.List/*<Object>*/) listMethod.invoke(MyVirtualMachine, null);

            System.out.println("Running JVM list ...");
            for (int i = 0; i < list.size(); i++) {
                Object o = list.get(i);
                java.lang.reflect.Method displayName = MyVirtualMachineDescriptor.getDeclaredMethod("displayName", null);
                java.lang.String name = (java.lang.String) displayName.invoke(o, null);
                // 列出当前有哪些 JVM 进程在运行
                // 这里的 if 条件根据实际情况进行更改
                if (name.contains(targetName)) {
                    // 获取对应进程的 pid 号
                    java.lang.reflect.Method getId = MyVirtualMachineDescriptor.getDeclaredMethod("id", null);
                    java.lang.String id = (java.lang.String) getId.invoke(o, null);
                    System.out.println("id >>> " + id);
                    java.lang.reflect.Method attach = MyVirtualMachine.getDeclaredMethod("attach", new Class[]{java.lang.String.class});
                    java.lang.Object vm = attach.invoke(o, new String[]{id});
                    java.lang.reflect.Method loadAgent = MyVirtualMachine.getDeclaredMethod("loadAgent", new Class[]{java.lang.String.class});
                    loadAgent.invoke(vm, new String[]{path});
                    java.lang.reflect.Method detach = MyVirtualMachine.getDeclaredMethod("detach", null);
                    detach.invoke(vm, null);
                    System.out.println("Agent.jar Start Success!");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}