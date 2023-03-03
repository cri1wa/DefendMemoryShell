package core;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

public class LoadAgent {
    String targetName;
    String path;
    public LoadAgent(String targetName,String agentPath) {
            this.targetName = targetName;
            this.path = agentPath;
    }

    public void loadAgent2JVM(){
        try{
            Class/*<?>*/ MyVirtualMachine = VirtualMachine.class.getClass();
            Class/*<?>*/ MyVirtualMachineDescriptor = VirtualMachineDescriptor.class.getClass();
            List<VirtualMachineDescriptor> list = VirtualMachine.list();
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