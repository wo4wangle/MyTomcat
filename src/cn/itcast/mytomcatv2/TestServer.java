package cn.itcast.mytomcatv2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class TestServer {
    public static String WEB_ROOT = System.getProperty("user.dir")+"\\"+ "WebContent";
    private static String url = "";

    //define a map to store con.propertise
    private static Map<String,String> map = new HashMap<String,String>();

    static{
        //load con.propertise before running
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(WEB_ROOT+"\\conf.properties"));
            Set set = prop.keySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()){
                String key = (String)iterator.next();
                String value = prop.getProperty(key);
                map.put(key,value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(map);
        //System.out.println(WEB_ROOT);
        ServerSocket serverSocket = null;
        Socket socket = null;
        InputStream is = null;
        OutputStream ops = null;
        try{
            serverSocket = new ServerSocket(8080);
            while(true){
                socket = serverSocket.accept();
                is = socket.getInputStream();
                ops = socket.getOutputStream();
                //get the HTTP request part , get the url path and put on url variable
                parse(is);
                //send the html content to the web client
                if(null != url){
                    if(url.indexOf(".") != -1){
                        //if url likes demo01.html is a static resource
                        sendStaticResource(ops);
                    } else {
                        sendDynamicResource(is,ops);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != is){
                is.close();
                is = null;
            }
            if(null != ops){
                ops.close();
                ops = null;
            }
            if(null != socket){
                socket.close();
                socket = null;
            }
        }
    }

    private static void sendDynamicResource(InputStream is, OutputStream ops) throws Exception {
        //HTTP
        ops.write("HTTP/1.1 200 OK\n".getBytes());
        ops.write("Server:Apache\n".getBytes());
        ops.write("Content-type:text/html;charset=utf-8\n".getBytes());
        ops.write("\n".getBytes());
        //get the value of key
        if(map.containsKey(url)){
            String value = map.get(url);
            Class clazz = Class.forName(value);
            Servlet servlet = (Servlet)clazz.newInstance();
            servlet.init();
            servlet.Service(is, ops);
        }
    }

    private static void sendStaticResource(OutputStream ops) throws IOException {
        //save the html content
        byte[] bytes = new byte[2048];
        FileInputStream fis = null;
        try{
            File file = new File(WEB_ROOT,url);
            if(file.exists()){
                ops.write("HTTP/1.1 200 OK\n".getBytes());
                ops.write("Server:apache-Coyote/1.1\n".getBytes());
                ops.write("Content-Type:text/html;charset=utf-8\n".getBytes());
                ops.write("\n".getBytes());
                fis = new FileInputStream(file);
                int ch = fis.read(bytes);
                while(ch!=-1){
                    ops.write(bytes,0,ch);
                    ch =  fis.read(bytes);
                }
            } else {
                ops.write("HTTP/1.1 404 not found\n".getBytes());
                ops.write("Server:apache-Coyote/1.1\n".getBytes());
                ops.write("Content-Type:text/html;charset=utf-8\n".getBytes());
                ops.write("\n".getBytes());
                String errorMessage = "file not found";
                ops.write(errorMessage.getBytes());
            }
        } catch(Exception e){
            e.printStackTrace();
        }finally {
            if(null != fis){
                fis.close();
                fis = null;
            }
        }
    }

    private static void parse(InputStream is) throws IOException {
        StringBuffer content = new StringBuffer(2048);
        byte[] buffer = new byte[2048];
        int i = -1;
        i = is.read(buffer);
        for(int j = 0; j < i; j++){
            content.append((char)buffer[j]);
        }
        System.out.println(content);
        //GET /demo01.html HTTP/1.1
        // get demo01.html
        parseUrl(content.toString());
    }

    private static void parseUrl(String content) {
        int index1,index2;
        index1 = content.indexOf(" ");
        if(index1 != -1){
            index2 = content.indexOf(" ", index1+1);
            if(index1 < index2){
                url = content.substring(index1+2,index2);
            }
        }
        System.out.println("url:"+url);
    }
}
