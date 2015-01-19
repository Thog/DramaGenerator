package eu.thog92.dramagen.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;





import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import eu.thog92.dramagen.DramaGenerator;

public class HttpServerHandlers {
	
	public HttpServerHandlers(DramaGenerator instance) throws IOException
	{
        HttpServer server = HttpServer.create(new InetSocketAddress(1010), 0);
        server.createContext("/refresh", new RefreshHandler(instance));
        server.setExecutor(null); // creates a default executor
        server.start();
	}
	
	static class RefreshHandler implements HttpHandler {

		private DramaGenerator main;

		public RefreshHandler(DramaGenerator instance) {
			this.main = instance;
		}

		@Override
		public void handle(HttpExchange ext) throws IOException {
			main.reload();
            String response = "Config Reloaded";
            ext.sendResponseHeaders(200, response.length());
            OutputStream os = ext.getResponseBody();
            os.write(response.getBytes());
            os.close();
		}
		
	}
	
}
