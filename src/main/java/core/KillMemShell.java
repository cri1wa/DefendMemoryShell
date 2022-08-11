package core;

import javassist.ClassPool;
import javassist.CtClass;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.ArrayList;

public class KillMemShell{
    public static BlackListClass blc = new BlackListClass();
    public static ArrayList<String> bl = blc.black_list;
    public static void agentmain(String agentArgs, Instrumentation ins) {
        System.out.println("[+] Scanning......");
        ins.addTransformer(new DefineTransformer(),true);
        Class[] classes = ins.getAllLoadedClasses();
        for (Class clas:classes){
            //System.out.println("Find ApplicationFilterChain11111:"+clas.getName());
            if (bl.contains(clas.getName())){
                try{
                    // 对类进行重新定义
                    ins.retransformClasses(new Class[]{clas});
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    public static class DefineTransformer implements ClassFileTransformer {

        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            className = className.replace("/",".");
            if (bl.contains(className)){
                System.out.println("[+] Checking classes that may be injected: " + className);
                ClassPool pool = ClassPool.getDefault();
                try {
                    //直接使用本地字节码覆盖JVM里面的字节码
                    CtClass c = pool.getCtClass(className);
                    if(c.toBytecode().length < classfileBuffer.length){
                        System.out.println("[+] className : "+ className + " JVM Bytecode length: "+classfileBuffer.length);
                        System.out.println("[+] className : "+ className + " Local Bytecode length: "+c.toBytecode().length);
                        System.out.println("[+] Bytecode length inconsistent, trying to clear......");
                        byte[] bytes = c.toBytecode();
                        System.out.println("[+] Kill the Mem_Shell: "+ className);
                        // 将 c 从 classpool 中删除以释放内存
                        c.detach();
                        return bytes;
                    }
                    c.detach();
                    return classfileBuffer;
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            return new byte[0];
        }
    }
}
