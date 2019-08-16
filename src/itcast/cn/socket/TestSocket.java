package itcast.cn.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TestSocket {
    public static void main(String[] args) throws Exception{
        Socket s = null;
        InputStream in = null;
        OutputStream out = null;
        try {
            s = new Socket("47.96.228.71",8080);//www.itcast.cn
            in = s.getInputStream();
            out = s.getOutputStream();

            out.write("GET /tmall/login.jsp HTTP/1.1\n".getBytes());
            out.write("HOST:47.96.228.71\n".getBytes());
            out.write("\n".getBytes());

            int i = in.read();
            while(i != -1){
                System.out.print((char)i);
                i = in.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(null != s){
                s.close();
                s = null;
            }
            if(null != in){
                in.close();
                in = null;
            }
            if(null != out){
                out.close();
                out = null;
            }
        }
    }

}
