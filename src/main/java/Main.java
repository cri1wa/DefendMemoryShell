import core.LoadAgent;

public class Main {
    public static void main(String[] args){
        LoadAgent la = new LoadAgent(args[0],args[1]);
        la.loadAgent2JVM();
    }
}
