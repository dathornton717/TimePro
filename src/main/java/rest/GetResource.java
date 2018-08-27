package rest;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import event.Event;
import factory.Factory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.Utilities;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Provider
@Path("/")
public class GetResource {
    private static final Logger LOGGER = LogManager.getLogger(GetResource.class.getName());

    public GetResource() {}

    @GET
    @Path("update-times")
    public Response updateTimes()
    throws Exception {
        LOGGER.info("Updating all times");
        Factory factory = new Factory();
        Map<String, List<String>> swimmers = factory.getAllNames();
        JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;

        for (String team : swimmers.keySet()) {
            ObjectNode json = jsonNodeFactory.objectNode();
            json.put("teamName", team);
            List<String> names = swimmers.get(team);
            for (String name : names) {
                int spaceIndex = name.indexOf(' ');
                String firstName = name.substring(0, spaceIndex);
                String lastName = name.substring(spaceIndex + 1);
                json.put("firstName", firstName);
                json.put("lastName", lastName);
                new PutResource().addSwimmer(json.toString());
            }
        }

        return Response.ok().build();
    }

    @GET
    @Path("times-by-name/{firstName}/{lastName}/{event}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTimesByName(
        @PathParam("firstName") String firstName,
        @PathParam("lastName") String lastName,
        @PathParam("event") String eventString)
    throws Exception {
        LOGGER.info("Getting times for " + firstName + " " + lastName + " for event " + eventString);
        Event event = Utilities.stringToEvent(eventString);

        Map<Long, Long> map = new Factory().getTimesByName(event, firstName, lastName);
        ArrayNode result = constructJsonArrayFromMap(map);

        return Response.ok().entity(result.toString()).build();
    }

    @GET
    @Path("times-between-dates-name/{firstName}/{lastName}/{event}/{start}/{end}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTimesBetweenDatesByName(
        @PathParam("firstName") String firstName,
        @PathParam("lastName") String lastName,
        @PathParam("event") String eventString,
        @PathParam("start") Long start,
        @PathParam("end") Long end)
    throws Exception {
        LOGGER.info("Getting time for " + firstName + " " + lastName + " for event " + eventString + " between dates " + start + " and " + end);
        Event event = Utilities.stringToEvent(eventString);

        Map<Long, Long> map = new Factory().getTimesBetweenDatesByName(event, firstName, lastName, start, end);
        ArrayNode result = constructJsonArrayFromMap(map);

        return Response.ok().entity(result.toString()).build();
    }

    @GET
    @Path("present-times-by-name/{firstName}/{lastName}/{event}/{start}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTimesToPresentByName(
        @PathParam("firstName") String firstName,
        @PathParam("lastName") String lastName,
        @PathParam("event") String eventString,
        @PathParam("start") Long start)
    throws Exception {
        LOGGER.info("Getting time for " + firstName + " " + lastName + " for event " + eventString + " from " + start + " to present");
        Event event = Utilities.stringToEvent(eventString);

        Map<Long, Long> map = new Factory().getTimesToPresentByName(event, firstName, lastName, start);
        ArrayNode result = constructJsonArrayFromMap(map);

        return Response.ok().entity(result.toString()).build();
    }

    @GET
    @Path("best-time-name/{firstName}/{lastName}/{event}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBestTimeByName(
        @PathParam("firstName") String firstName,
        @PathParam("lastName") String lastName,
        @PathParam("event") String eventString)
    throws Exception {
        LOGGER.info("Getting best time for " + firstName + " " + lastName + " for event " + eventString);
        Event event = Utilities.stringToEvent(eventString);

        Map<Long, Long> map = new Factory().getBestTimeByName(event, firstName, lastName);
        ArrayNode result = constructJsonArrayFromMap(map);

        return Response.ok().entity(result.toString()).build();
    }

    @GET
    @Path("times-by-id/{id}/{event}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTimesById(
        @PathParam("id") String idString,
        @PathParam("event") String eventString)
    throws Exception {
        LOGGER.info("Getting time for id " + idString + " for event " + eventString);
        Event event = Utilities.stringToEvent(eventString);
        UUID id = UUID.fromString(idString);

        Map<Long, Long> map = new Factory().getTimesById(event, id);
        ArrayNode result = constructJsonArrayFromMap(map);

        return Response.ok().entity(result.toString()).build();
    }

    @GET
    @Path("names")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllNames()
    throws Exception {
        LOGGER.info("Getting all swimmer names");
        Map<String, List<String>> names = new Factory().getAllNames();
        ArrayNode result = constructJsonArrayFromNameMap(names);
        return Response.ok().entity(result.toString()).build();
    }

    private ArrayNode constructJsonArrayFromMap(Map<Long, Long> map) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode result = factory.arrayNode();
        for (Long date : map.keySet()) {
            ObjectNode node = factory.objectNode();
            node.put("date", date);
            node.put("time", map.get(date));
            result.add(node);
        }

        return result;
    }

    private ArrayNode constructJsonArrayFromNameMap(Map<String, List<String>> map) {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode result = factory.arrayNode();
        for (String team : map.keySet()) {
            List<String> names = map.get(team);
            for (String name : names) {
                result.add(name);
            }
        }

        return result;
    }
}
