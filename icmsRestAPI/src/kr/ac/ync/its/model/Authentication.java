package kr.ac.ync.its.model;

import kr.ac.ync.its.controller.*;
import kr.ac.ync.its.dataCollection.*;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@Path("/authentication")
@Api("/Authentication")
@ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid ID supplied"),
		@ApiResponse(code = 404, message = "Page Nof Found") })
@SwaggerDefinition(tags = { @Tag(name = "Authentication", description = "RestAPI의 토큰 발행 및 검증을 위한 인증 APIs") })
public class Authentication {
	final String CHARSET_UTF8 = ";charset=UTF-8";

	JSONParser jParser = new JSONParser();
	ItsJsonBuilder iBuilder = new ItsJsonBuilder();
	ItsQueryContainer iContainer = new ItsQueryContainer();
	ItsTokenContainer tokenContainer = new ItsTokenContainer();

	/*
	 * @Path("/token")
	 * 
	 * @POST
	 * 
	 * @Produces(MediaType.APPLICATION_JSON + CHARSET_UTF8) public Response
	 * authenticateUser(@FormParam("memberid") final String memberid,
	 * 
	 * @FormParam("permission") final String
	 * permission, @HeaderParam("authorization") final String authString) throws
	 * Exception {
	 * 
	 * if (tokenContainer.verifyAuthenticate(authString)) { iBuilder.clearAll();
	 * iBuilder.put("result", true); iBuilder.put("msg", "The token is validated");
	 * iBuilder.buildObj(); return Response.ok(iBuilder.objToString()).build(); }
	 * else { return Response.status(Response.Status.FORBIDDEN).build(); }
	 * 
	 * }
	 */

	@Path("/member")
	@POST
	@Produces(MediaType.APPLICATION_JSON + CHARSET_UTF8)
	public Response loginUser(@FormParam("memberid") final String memberid,
			@FormParam("memberpw") final String memberpw) throws Exception {
		System.out.println(memberid + ":" + memberpw);
		String queryResult = iContainer.Query(ItsQueryCollection.LOGIN_MEMBER, memberid, memberpw);

		System.out.println(queryResult);
		JSONArray jArray = (JSONArray) jParser.parse(queryResult);
		if (jArray.size() == 0)
			return Response.status(Response.Status.FORBIDDEN).build();

		JSONObject jObject = (JSONObject) jArray.get(0);
		String permission = (String) jObject.get("permission");
		String token = tokenContainer.issueAuthenticate(memberid, permission);

		iBuilder.clearAll();
		iBuilder.put("result", true);
		iBuilder.put("memberid", memberid);
		iBuilder.put("permission", permission);
		iBuilder.put("msg", "Member Authentication Token Sign Success");
		iBuilder.put("token", token);
		iBuilder.buildObj();
		return Response.ok(iBuilder.objToString()).build();
	}

}
