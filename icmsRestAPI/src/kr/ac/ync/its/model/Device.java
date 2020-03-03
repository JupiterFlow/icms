package kr.ac.ync.its.model;

import java.sql.SQLException;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import kr.ac.ync.its.controller.ItsJsonBuilder;
import kr.ac.ync.its.controller.ItsQueryContainer;
import kr.ac.ync.its.controller.ItsTokenContainer;
import kr.ac.ync.its.dataCollection.ItsQueryCollection;

@Path("/device")
@Api("/Device")
@SwaggerDefinition(tags = { @Tag(name = "Device", description = "디바이스의 조회, 등록, 수정, 삭제를 위한 APIs") })
public class Device {
	JSONParser jParser = new JSONParser();
	ItsJsonBuilder iBuilder = new ItsJsonBuilder();
	ItsQueryContainer iContainer = new ItsQueryContainer();
	ItsTokenContainer tokenContainer = new ItsTokenContainer();
	final String CHARSET_UTF8 = ";charset=UTF-8";

	// 디바이스 등록
	@POST
	@Produces(MediaType.APPLICATION_JSON + CHARSET_UTF8)
	public Response registeredDevice(@FormParam("address") final String address, @FormParam("ip") final String ip,
			@HeaderParam("authorization") final String authString) throws SQLException {
		String userid = tokenContainer.getUserid(authString);
		iBuilder.clearAll();
		if (userid != "") {
			iContainer.Update(ItsQueryCollection.INSERT_DEVICE, userid, address, ip);
			iBuilder.put("result", true);
			iBuilder.put("message", "The registed is successful.");
		} else {
			iBuilder.put("result", false);
			iBuilder.put("message", "The registed is failed.");
		}
		iBuilder.buildObj();
		return Response.ok(iBuilder.objToString()).build();
	}

	// 디바이스 정보 조회
	@Path("/{device_no}")
	@GET
	@Produces(MediaType.APPLICATION_JSON + CHARSET_UTF8)
	public Response getDevice(@PathParam("device_no") final String device_no,
			@HeaderParam("authorization") final String authString) throws ParseException, SQLException {
		iBuilder.clearAll();
		if (tokenContainer.verifyAuthenticate(authString)) {
			JSONArray jArray = (JSONArray) jParser.parse(iContainer.Query(ItsQueryCollection.SELECT_DEVICE, device_no,
					tokenContainer.getUserid(authString)));
			if (jArray.size() > 0) {
				JSONObject jObj = (JSONObject) jArray.get(0);
				iBuilder.put("result", true);
				iBuilder.put("address", jObj.get("address"));
				iBuilder.put("ip", jObj.get("ip"));
				iBuilder.put("install_date", jObj.get("install_date"));
				iBuilder.put("device_content", jObj.get("device_content"));
				iBuilder.put("message", "The search was successful");
			} else {
				iBuilder.put("result", false);
				iBuilder.put("message", "The search was failed");
			}
		} else {
			iBuilder.put("result", false);
			iBuilder.put("message", "권한이 유효하지 않습니다.");
		}
		iBuilder.buildObj();
		return Response.ok(iBuilder.objToString()).build();
	}

	// 디바이스 정보 수정
	@Path("/{device_no}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON + CHARSET_UTF8)
	public Response editContent(@PathParam("device_no") final String device_no,
			@FormParam("device_content") final String device_content, @FormParam("ip") final String ip,
			@HeaderParam("authorization") final String authString) throws ParseException, SQLException {
		if (tokenContainer.verifyAuthenticate(authString)) {
			iBuilder.clearAll();
			JSONArray jArray = (JSONArray) jParser.parse(iContainer.Query(ItsQueryCollection.SELECT_DEVICE, device_no,
					tokenContainer.getUserid(authString)));
			if (jArray.size() > 0) {
				iContainer.Update(ItsQueryCollection.UPDATE_DEVICE, ip, device_content, device_no,
						tokenContainer.getUserid(authString));
				iBuilder.put("result", true);
				iBuilder.put("message", "성공적으로 디바이스를 수정하였습니다.");
			} else {
				iBuilder.put("result", false);
				iBuilder.put("message", "등록되지 않은 디바이스입니다.");
			}
		} else {
			iBuilder.put("result", false);
			iBuilder.put("message", "권한이 유효하지 않습니다.");
		}
		iBuilder.buildObj();
		return Response.ok(iBuilder.objToString()).build();
	}

	// 디바이스 정보 삭제
	@Path("/{device_no}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON + CHARSET_UTF8)
	public Response deleteContent(@PathParam("device_no") final String device_no,
			@HeaderParam("authorization") final String authString) throws SQLException, ParseException {
		if (tokenContainer.verifyAuthenticate(authString)) {
			iBuilder.clearAll();
			JSONArray jArray = (JSONArray) jParser.parse(iContainer.Query(ItsQueryCollection.SELECT_DEVICE, device_no,
					tokenContainer.getUserid(authString)));
			if (jArray.size() > 0) {
				iContainer.Update(ItsQueryCollection.DELETE_DEVICE, device_no, tokenContainer.getUserid(authString));
				iBuilder.put("result", true);
				iBuilder.put("message", "성공적으로 디바이스를 삭제하였습니다.");
			} else {
				iBuilder.put("result", false);
				iBuilder.put("message", "등록되지 않은 디바이스입니다.");
			}
		} else {
			iBuilder.put("result", false);
			iBuilder.put("message", "권한이 유효하지 않습니다.");
		}
		iBuilder.buildObj();
		return Response.ok(iBuilder.objToString()).build();
	}
}
