package eu.thog92.dramagen;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import eu.thog92.generator.api.tasks.GeneratorTask;

import java.io.*;
import java.nio.charset.Charset;

public class DramaHandler implements HttpHandler
{

    private final boolean plainTxt;
    private final GeneratorTask generatorTask;

    public DramaHandler(GeneratorTask generatorTask, boolean plain)
    {
        this.generatorTask = generatorTask;
        this.plainTxt = plain;
    }

    @Override
    public void handle(HttpExchange ext) throws IOException
    {
        if (!plainTxt)
            this.handleWithHTML(ext);
        else
            this.handlePlain(ext);
    }

    private void handlePlain(HttpExchange ext) throws IOException
    {
        String randomDrama = generatorTask.generateSentence(false);
        if (randomDrama == null)
            randomDrama = "The Minecraft Drama Generator has been bought by Microsoft.";

        OutputStream os = ext.getResponseBody();
        ext.sendResponseHeaders(200, randomDrama.length());
        os.write(randomDrama.getBytes(Charset.forName("UTF-8")));
        os.close();
    }

    private void handleWithHTML(HttpExchange ext)
    {
        try
        {
            OutputStream os = ext.getResponseBody();
            InputStream in = DramaHandler.class.getResourceAsStream("/public/drama.html");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                out.append(line);
            }
            reader.close();
            String randomDrama = generatorTask.generateSentence(false);
            if (randomDrama == null)
            {
                randomDrama = "The Minecraft Drama Generator has been bought by Microsoft.";
            }

            byte[] response = out.toString().replace("%DRAMA%", randomDrama).getBytes(Charset.forName("UTF-8"));
            ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(response);
            byte[] buf = new byte[1024];
            ext.sendResponseHeaders(200, response.length);
            int len;
            while ((len = arrayInputStream.read(buf, 0, 1024)) != -1)
                os.write(buf, 0, len);
            os.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
