#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

#if ($JAVAEE_TYPE == "jakarta")
import jakarta.ws.rs.*;
#else
import javax.ws.rs.*;
#end

#parse("File Header.java")
@Path("/hello-world")
public class ${NAME} {
    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }
}