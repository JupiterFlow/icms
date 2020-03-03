package kr.ac.ync.its.model;

import kr.ac.ync.its.controller.*;
import kr.ac.ync.its.dataCollection.*;

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

import java.sql.SQLException;

@Path("/member")
@Api("/Member")
@SwaggerDefinition(tags = {
		@Tag(name = "Member", description = "멤버의 조회, 등록, 수정, 삭제 / devices 목록, contents 목록 관련 APIs") })
public class Member {

	JSONParser jParser = new JSONParser();
	ItsJsonBuilder iBuilder = new ItsJsonBuilder();
	ItsQueryContainer iContainer = new ItsQueryContainer();
	ItsTokenContainer tokenContainer = new ItsTokenContainer();
	final String CHARSET_UTF8 = ";charset=UTF-8";

	// 회원가입
	@POST
	@Produces(MediaType.APPLICATION_JSON + CHARSET_UTF8)
	public Response registerMember(@FormParam("memberid") final String memberid,
			@FormParam("memberpw") final String memberpw, @FormParam("membertel") final String membertel,
			@FormParam("membername") final String membername, @FormParam("memberemail") final String memberemail)
			throws SQLException, ParseException {
		iBuilder.clearAll();
		JSONArray jArray = (JSONArray) jParser.parse(iContainer.Query(ItsQueryCollection.SELECT_MEMBER, memberid));
		if (jArray.size() == 0) {
			iContainer.Update(ItsQueryCollection.INSERT_MEMBER, memberid, memberpw, membertel, membername, memberemail);
			iBuilder.put("result", true);
			iBuilder.put("memberid", memberid);
			iBuilder.put("message", "The member register success ");
		} else {
			iBuilder.put("result", false);
			iBuilder.put("message", "The member register fail");
		}
		iBuilder.buildObj();
		return Response.ok(iBuilder.objToString()).build();
	}

	// 회원정보 수정
	@Path("/{memberid}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON + CHARSET_UTF8)
	public Response editMember(@PathParam("memberid") final String memberid,
			@FormParam("memberpw") final String memberpw, @FormParam("membertel") final String membertel,
			@FormParam("membername") final String membername, @FormParam("memberemail") final String memberemail,
			@HeaderParam("authorization") final String authString) throws ParseException, SQLException {
		
		if (tokenContainer.getUserid(authString).equals(memberid) && tokenContainer.verifyAuthenticate(authString)) {
			iBuilder.clearAll();
			JSONArray jArray = (JSONArray) jParser.parse(iContainer.Query(ItsQueryCollection.SELECT_MEMBER, memberid));
			if (jArray.size() > 0) {
				iContainer.Update(ItsQueryCollection.UPDATE_MEMBER, memberpw, membertel, membername, memberemail,
						memberid);
				iBuilder.put("result", true);
				iBuilder.put("message", "성공적으로 멤버 정보를 수정하였습니다.");
			} else {
				iBuilder.put("result", false);
				iBuilder.put("message", "등록되지 않은 멤버입니다.");
			}
		} else {
			iBuilder.put("result", false);
			iBuilder.put("message", "권한이 유효하지 않습니다.");
		}
		iBuilder.buildObj();
		return Response.ok(iBuilder.objToString()).build();
		
	}

	// 회원정보 삭제
	@Path("/{memberid}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON + CHARSET_UTF8)
	public Response deleteMember(@PathParam("memberid") final String memberid,
			@HeaderParam("authorization") final String authString) throws ParseException, SQLException {
		if (tokenContainer.getUserid(authString).equals(memberid) && tokenContainer.verifyAuthenticate(authString)) {
			iBuilder.clearAll();
			JSONArray jArray = (JSONArray) jParser.parse(iContainer.Query(ItsQueryCollection.SELECT_MEMBER, memberid));
			if (jArray.size() > 0) {
				iContainer.Update(ItsQueryCollection.DELETE_MEMBER, memberid);
				iBuilder.put("result", true);
				iBuilder.put("message", "성공적으로 멤버 정보를 삭제하였습니다.");
			} else {
				iBuilder.put("result", false);
				iBuilder.put("message", "등록되지 않은 멤버입니다.");
			}
		} else {
			iBuilder.put("result", false);
			iBuilder.put("message", "권한이 유효하지 않습니다.");
		}
		iBuilder.buildObj();
		return Response.ok(iBuilder.objToString()).build();
	}

	// 멤버의 device 목록 출력
	@Path("/devices")
	@GET
	@Produces(MediaType.APPLICATION_JSON + CHARSET_UTF8)
	public Response getMemberDevices(@HeaderParam("authorization") final String authString) throws ParseException, SQLException {
		final String memberid = tokenContainer.getUserid(authString);
		System.out.println(memberid);
		JSONArray jArray = (JSONArray) jParser.parse(iContainer.Query(ItsQueryCollection.OWNED_DEVICE_BY_MEMBER, memberid));
		iBuilder.clearAll();
		if (jArray.size() > 0) {
			iBuilder.put("result", true);
			iBuilder.put("message", "The search was successful");
			iBuilder.buildObj();
			iBuilder.appendToArr();
			iBuilder.clearJsonObj();
			iBuilder.clearHashMap();
			for (int idx=0;idx<jArray.size();idx++) {
				JSONObject jObj = (JSONObject) jArray.get(idx);
				iBuilder.put("no", jObj.get("device_no"));
				iBuilder.put("owner", jObj.get("device_owner"));
				iBuilder.put("address", jObj.get("address"));
				iBuilder.put("ip", jObj.get("ip"));
				iBuilder.put("install_date", jObj.get("install_date"));
				iBuilder.put("device_content", jObj.get("device_content"));
				
				iBuilder.buildObj();
				iBuilder.appendToArr();
				iBuilder.clearJsonObj();
				iBuilder.clearHashMap();
			}
		} else {
			iBuilder.put("result", false);
			iBuilder.put("message", "The search was failed");
			iBuilder.buildObj();
			return Response.ok(iBuilder.objToString()).build();
		}
	System.out.println(iBuilder.arrToString());
	return Response.ok(iBuilder.arrToString()).build();
	}

	// 멤버의 content 목록 출력
	@Path("/contents")
	@GET
	@Produces(MediaType.APPLICATION_JSON + CHARSET_UTF8)
	public Response getMemberContents(@HeaderParam("authorization") final String authString) throws ParseException, SQLException {
		final String memberid = tokenContainer.getUserid(authString);
		JSONArray jArray = (JSONArray) jParser.parse(iContainer.Query(ItsQueryCollection.OWNED_CONTENT_BY_MEMBER, memberid));
		iBuilder.clearAll();
		if (jArray.size() > 0) {
			iBuilder.put("result", true);
			iBuilder.put("message", "The search was successful");
			iBuilder.buildObj();
			iBuilder.appendToArr();
			iBuilder.clearJsonObj();
			iBuilder.clearHashMap();
			for (int idx=0;idx<jArray.size();idx++) {
				JSONObject jObj = (JSONObject) jArray.get(idx);				
				iBuilder.put("no", jObj.get("content_no"));
				iBuilder.put("type", jObj.get("type"));
				iBuilder.put("content_path", jObj.get("content_path"));
				iBuilder.put("detail", jObj.get("detail"));
				iBuilder.put("content_owner", jObj.get("content_owner"));
				iBuilder.buildObj();
				iBuilder.appendToArr();
				iBuilder.clearJsonObj();
				iBuilder.clearHashMap();
			}
		} else {
			iBuilder.put("result", false);
			iBuilder.put("message", "The search was failed");
			iBuilder.buildObj();
			return Response.ok(iBuilder.objToString()).build();
		}
	System.out.println(iBuilder.arrToString());
	return Response.ok(iBuilder.arrToString()).build();
	}

	// 멤버 정보 조회(memberid 기반)
	@Path("/{memberid}")
	@GET
	@Produces(MediaType.APPLICATION_JSON + CHARSET_UTF8)
	public Response getMemberInformation(@PathParam("memberid") final String memberid,
			@HeaderParam("authorization") final String authString) throws ParseException, SQLException {
		if (tokenContainer.getUserid(authString).equals(memberid) && tokenContainer.verifyAuthenticate(authString)) {
			iBuilder.clearAll();
			JSONArray jArray = (JSONArray) jParser.parse(iContainer.Query(ItsQueryCollection.SELECT_MEMBER, memberid));
			if (jArray.size() > 0) {
				JSONObject jObj = (JSONObject)jArray.get(0);
				iBuilder.put("result", true);
				iBuilder.put("message", "The search was successful");
				iBuilder.put("type", jObj.get("type"));
				iBuilder.put("content_path", jObj.get("content_path"));
				iBuilder.put("detail", jObj.get("detail"));
				iBuilder.put("content_owner", jObj.get("content_owner"));
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

}