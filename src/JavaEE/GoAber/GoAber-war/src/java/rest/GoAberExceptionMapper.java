
package rest;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


/** REST API exception mapper.
 * 
 * This gives a print out of the stack trace if something goes wrong with a rest
 * call instead of just throwing a generic 500 error.
 * 
 * Code is original based on http://stackoverflow.com/questions/26014184/
 * 
 * @author samuel
 */
@Provider
public class GoAberExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        e.printStackTrace();
        return Response
                    .status(Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(e.getCause())
                    .build();
    }

}