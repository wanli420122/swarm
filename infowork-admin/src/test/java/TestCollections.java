import ch.qos.logback.core.net.SyslogOutputStream;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by wl on 2020/8/19
 */
public class TestCollections {
    public float a(){
        return 4.1f;
    }
    public static void main(String[] args) {
        List<Persion> list=new ArrayList<Persion>();
        Persion p=new Persion();
        Persion p1=new Persion();
        p1.id="2";
        p.id="1";
        list.add(p);
        //p=null;
        p.id="2";
        p=p1;
        System.out.println(list.get(0).id);
        System.out.println(list.get(0).toString());

        System.out.println(p.toString());
        StackTraceElement[] elements= Thread.currentThread().getStackTrace();
        for(int i=0;i<elements.length;i++){
            System.out.print(elements[i]+" ");
        }
        //System.out.println(Thread.currentThread().getStackTrace().toString());
//        float a=4.1f;
//        double b=a;
//        double c=4.12;
//        float d= (float) c;
//        System.out.println(b);
//        System.out.println(d);


    }
    public static class Persion{
        private String id;

    }
}
