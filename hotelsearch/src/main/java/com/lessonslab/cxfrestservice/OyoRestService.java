package com.lessonslab.cxfrestservice;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@WebService(name="oyoapi")
public interface OyoRestService {
	
	@GET
	@Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_HTML,MediaType.TEXT_PLAIN})
	@Path("/gethoteldetail")
	public Response getHotelDetails(@QueryParam("hotelid") String id);

}
