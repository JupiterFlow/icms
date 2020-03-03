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

@Path("/content")
@Api("/Content")
@SwaggerDefinition(tags = { @Tag(name = "Content", description = "컨텐츠의 조회, 등록, 수정, 삭제를 위한 APIs") })
public class Content {
	JSONParser jParser = new JSONParser();
	ItsJsonBuilder iBuilder = new ItsJsonBuilder();
	ItsQueryContainer iContainer = new ItsQueryContainer();
	ItsTokenContainer tokenContainer = new ItsTokenContainer();
	final String CHARSET_UTF8 = ";charset=UTF-8";

	// 컨텐츠 업로드
	@POST
	@Produces(MediaType.APPLICATION_JSON + CHARSET_UTF8)
	public Response uploadContent(@FormParam("type") final String type, @FormParam("path") final String path,
			@FormParam("detail") final String detail, @HeaderParam("authorization") final String authString)
			throws SQLException {
		String userid = tokenContainer.getUserid(authString);
		iBuilder.clearAll();
		if (userid != "") {
			iContainer.Update(ItsQueryCollection.INSERT_CONTENT, type, path, detail, userid);
			iBuilder.put("result", true);
			iBuilder.put("message", "The content upload success");
		} else {
			iBuilder.put("result", false);
			iBuilder.put("message", "The content upload fail");
		}
		iBuilder.buildObj();
		return Response.ok(iBuilder.objToString()).build();
	}

	// 컨텐츠 정보 조회
	@Path("/{content_no}")
	@GET
	@Produces(MediaType.APPLICATION_JSON + CHARSET_UTF8)
	public Response getContent(@PathParam("content_no") final String content_no,
			@HeaderParam("authorization") final String authString) throws ParseException, SQLException {
		iBuilder.clearAll();
		if (tokenContainer.verifyAuthenticate(authString)) {
			JSONArray jArray = (JSONArray) jParser.parse(iContainer.Query(ItsQueryCollection.SELECT_CONTENT, content_no,
					tokenContainer.getUserid(authString)));
			if (jArray.size() > 0) {
				JSONObject jObj = (JSONObject) jArray.get(0);
				iBuilder.put("result", true);
				iBuilder.put("type", jObj.get("type"));
				iBuilder.put("content_path", jObj.get("content_path"));
				iBuilder.put("detail", jObj.get("detail"));
				iBuilder.put("content_owner", jObj.get("content_owner"));
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

	// 컨텐츠 정보 수정
	@Path("/{content_no}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON + CHARSET_UTF8)
	public Response editContent(@PathParam("content_no") final String content_no, @FormParam("type") final String type,
			@FormParam("path") final String path, @FormParam("detail") final String detail,
			@HeaderParam("authorization") final String authString) throws ParseException, SQLException {
		if (tokenContainer.verifyAuthenticate(authString)) {
			iBuilder.clearAll();
			JSONArray jArray = (JSONArray) jParser.parse(iContainer.Query(ItsQueryCollection.SELECT_CONTENT, content_no,
					tokenContainer.getUserid(authString)));
			if (jArray.size() > 0) {
				iContainer.Update(ItsQueryCollection.UPDATE_CONTENT, type, path, detail, content_no,
						tokenContainer.getUserid(authString));
				iBuilder.put("result", true);
				iBuilder.put("message", "성공적으로 컨텐츠를 수정하였습니다.");
			} else {
				iBuilder.put("result", false);
				iBuilder.put("message", "등록되지 않은 컨텐츠입니다.");
			}
		} else {
			iBuilder.put("result", false);
			iBuilder.put("message", "권한이 유효하지 않습니다.");
		}
		iBuilder.buildObj();
		return Response.ok(iBuilder.objToString()).build();
	}

	// 컨텐츠 정보 삭제
	@Path("/{content_no}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON + CHARSET_UTF8)
	public Response deleteContent(@PathParam("content_no") final String content_no,
			@HeaderParam("authorization") final String authString) throws SQLException, ParseException {
		if (tokenContainer.verifyAuthenticate(authString)) {
			iBuilder.clearAll();
			JSONArray jArray = (JSONArray) jParser.parse(iContainer.Query(ItsQueryCollection.SELECT_CONTENT, content_no,
					tokenContainer.getUserid(authString)));
			if (jArray.size() > 0) {
				iContainer.Update(ItsQueryCollection.DELETE_CONTENT, content_no, tokenContainer.getUserid(authString));
				iBuilder.put("result", true);
				iBuilder.put("message", "성공적으로 컨텐츠를 삭제하였습니다.");
			} else {
				iBuilder.put("result", false);
				iBuilder.put("message", "등록되지 않은 컨텐츠입니다.");
			}
		} else {
			iBuilder.put("result", false);
			iBuilder.put("message", "권한이 유효하지 않습니다.");
		}
		iBuilder.buildObj();
		return Response.ok(iBuilder.objToString()).build();
	}

}
