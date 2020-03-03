package kr.ac.ync.its.main;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import kr.ac.ync.its.*;

public class Main {

	public static void main(String[] args) throws IOException, ParseException {
		icmsManager hContainer = new icmsManager();
		String uID = "123456";
		String uPW = "654324";
		
		System.out.println(hContainer.signUp(uID, uPW, "010-0000-0000", "abcdef", "test@naver.com"));
		String resResult = hContainer.signIn(uID, uPW);

		JSONObject json = (JSONObject) new JSONParser().parse(resResult);
		System.out.println(json);
		String token = (String) json.get("token");
		String userid = (String) json.get("memberid");
		hContainer.setToken(token);
		System.out.println(userid);
		System.out.println(hContainer.getToken());

		// Content Upload 테스트 필요 함(반환값 Null String)
		System.out.println(hContainer.modifyMember(uID,uPW, "010-123213", "테스트", "asdasd@asdasdas.com"));
		System.out.println(hContainer.dropOut(uID));

	}

}
