package mytomcatv1;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {
    public static void main(String[] args) throws IOException{
        ServerSocket severSocket = null;
        Socket socket = null;
        OutputStream ops = null;
        try{
            severSocket = new ServerSocket(8080);
            while(true){
                socket = severSocket.accept();
                ops = socket.getOutputStream();
                ops.write("HTTP/1.1 200 OK\n".getBytes());
                ops.write("Content-Type:text/html;charset=utf-8\n".getBytes());
                ops.write("Server:Apache-Coyote/1.1\n".getBytes());
                ops.write("\n\n".getBytes());
                StringBuffer buf = new StringBuffer();
                buf.append("wangle");
                ops.write(buf.toString().getBytes());
                ops.flush();
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(null != ops){
                ops.close();
                ops = null;
            }
            if(null != socket){
                ops.close();
                ops = null;
            }
        }

    }
}
