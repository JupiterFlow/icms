package kr.ac.ync.its;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Joiner;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class icmsManager {
	String clientToken = new String();
	final String hostURL = "http://....:50080";
	final String baseURL = "/icms/apis";
	// 제안점
	// 1. 컨텐츠/디바이스 정보 조회 시에 uri에 번호를 넣지 말고 인자로 번호를 전달??
	// 2. 삭제 키워드를 delete 대신 drop적기

	private String makeFormData(Map<String, Object> map) {
		return Joiner.on("&").withKeyValueSeparator("=").join(map);
	}

	public void setToken(String _token) {
		this.clientToken = "Bearer " + _token;
	}

	public String getToken() {
		return this.clientToken;
	}

	public void close() {
		this.clientToken = null;
	}

	// Authorization - POST (로그인)
	public String signIn(String id, String pw) {
		Client client = Client.create();

		WebResource webResource = client.resource(hostURL + baseURL + "/authentication/member");

		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("memberid", id);
		mapData.put("memberpw", pw);

		String formData = makeFormData(mapData);

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
				.post(ClientResponse.class, formData);
		/*
		 * if (response.getStatus() != 201) { throw new
		 * RuntimeException("Failed : HTTP error code : " + response.getStatus()); }
		 */
		return response.getEntity(String.class);
	}

	// member
	// member - POST(가입)
	public String signUp(String id, String pw, String tel, String name, String email) { // 아이디 중복 확인으로 인한 실패를 넣을거면
																						// int나
		Client client = Client.create();

		WebResource webResource = client.resource(hostURL + baseURL + "/member");

		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("memberid", id);
		mapData.put("memberpw", pw);
		mapData.put("membertel", tel);
		mapData.put("membername", name);
		mapData.put("memberemail", email);

		String formData = makeFormData(mapData);

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
				.post(ClientResponse.class, formData);
		/*
		 * if (response.getStatus() != 201) { throw new
		 * RuntimeException("Failed : HTTP error code : " + response.getStatus()); }
		 */

		return response.getEntity(String.class);
	}

	// member/[memberid] - GET(멤버정보불러오기)
	public String getMemberInfo(String memberid) {
		Client client = Client.create();

		WebResource webResource = client.resource(hostURL + baseURL + "/member/" + memberid);

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
				.header("authorization", this.clientToken).get(ClientResponse.class);
		/*
		 * if (response.getStatus() != 201) { throw new
		 * RuntimeException("Failed : HTTP error code : " + response.getStatus()); }
		 */
		return response.getEntity(String.class);
	}

	// member/[memberid] - PUT(멤버정보수정)
	public String modifyMember(String memberid, String memberpw, String tel, String name, String email) {
		Client client = Client.create();

		WebResource webResource = client.resource(hostURL + baseURL + "/member/" + memberid);

		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("memberpw", memberpw);
		mapData.put("membertel", tel);
		mapData.put("membername", name);
		mapData.put("memberemail", email);

		String formData = makeFormData(mapData);
		System.out.println(formData);
		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
				.header("authorization", this.clientToken).put(ClientResponse.class, formData);
		/*
		 * if (response.getStatus() != 201) { throw new
		 * RuntimeException("Failed : HTTP error code : " + response.getStatus()); }
		 */
		return response.getEntity(String.class);
	}

	// member/[memberid] - DELETE(회원탈퇴)
	public String dropOut(String memberid) {
		Client client = Client.create();

		WebResource webResource = client.resource(hostURL + baseURL + "/member/" + memberid);

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON + ";charset=UTF-8").header(HttpHeaders.AUTHORIZATION,this.clientToken)
				.delete(ClientResponse.class);

		/*
		 * if (response.getStatus() != 201) { throw new
		 * RuntimeException("Failed : HTTP error code : " + response.getStatus()); }
		 */

		return response.getEntity(String.class);
	}

	// member/contents - GET(소유중인 컨텐츠불러오기)
	public String getOwnContents() { // id는 세션의 id return은 콘텐츠목록
		Client client = Client.create();

		WebResource webResource = client.resource(hostURL + baseURL + "/member/contents");

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON + ";charset=UTF-8").header(HttpHeaders.AUTHORIZATION,this.clientToken)
				.get(ClientResponse.class);

		/*
		 * if (response.getStatus() != 201) { throw new
		 * RuntimeException("Failed : HTTP error code : " + response.getStatus()); }
		 */
		System.out.println(response.getStatus());
		return response.getEntity(String.class);
	}

	// member/devices - GET(소유중인 디바이스불러오기)
	public String getOwnDevices() {
		Client client = Client.create();

		WebResource webResource = client.resource(hostURL + baseURL + "/member/devices");

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON + ";charset=UTF-8").header(HttpHeaders.AUTHORIZATION,this.clientToken)
				.get(ClientResponse.class);

		/*
		 * if (response.getStatus() != 201) { throw new
		 * RuntimeException("Failed : HTTP error code : " + response.getStatus()); }
		 */

		return response.getEntity(String.class);
	}

	// content
	// content/[contentno] - GET(컨텐츠 번호로 내용 조회)
	public String getContentInfo(String contentno) {
		Client client = Client.create();

		WebResource webResource = client.resource(hostURL + baseURL + "/content/" + contentno);

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
				.get(ClientResponse.class);
		/*
		 * if (response.getStatus() != 201) { throw new
		 * RuntimeException("Failed : HTTP error code : " + response.getStatus()); }
		 */

		return response.getEntity(String.class);
	}

	// content/[contentno] - PUT(컨텐츠수정)
	public String modifyContent(String content_no, String type, String path, String detail) {
		Client client = Client.create();

		WebResource webResource = client.resource(hostURL + baseURL + "/content/" + content_no);

		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("type", type);
		mapData.put("path", path);
		mapData.put("detail", detail);

		String formData = makeFormData(mapData);

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
				.header("authorization", this.clientToken).put(ClientResponse.class, formData);
		/*
		 * if (response.getStatus() != 201) { throw new
		 * RuntimeException("Failed : HTTP error code : " + response.getStatus()); }
		 */
		return response.getEntity(String.class);
	}

	// content/[contentno] - DELETE(컨텐츠삭제)
	public String deleteContent(String content_no) {
		Client client = Client.create();

		WebResource webResource = client.resource(hostURL + baseURL + "/content/" + content_no);

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
				.header("authorization", this.clientToken).delete(ClientResponse.class);
		/*
		 * if (response.getStatus() != 201) { throw new
		 * RuntimeException("Failed : HTTP error code : " + response.getStatus()); }
		 */
		return response.getEntity(String.class);
	}

	// content - POST(컨텐츠 업로드)
	public String uploadContent(String type, String path, String detail) {
		Client client = Client.create();

		WebResource webResource = client.resource(hostURL + baseURL + "/content");

		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("type", type);
		mapData.put("path", path);
		mapData.put("detail", detail);

		String formData = makeFormData(mapData);
		
		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
				.header(HttpHeaders.AUTHORIZATION, this.clientToken).post(ClientResponse.class, formData);
		/*
		 * if (response.getStatus() != 201) { throw new
		 * RuntimeException("Failed : HTTP error code : " + response.getStatus()); }
		 */
		return response.getEntity(String.class);
	}

	// device
	// device - POST(디바이스 추가)
	// device/[deviceno] - GET (디바이스 번호로 디바이스 조회)
	public String getDeviceInfo(String device_no) {
		Client client = Client.create();

		WebResource webResource = client.resource(hostURL + baseURL + "/device/" + device_no);

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
				.header("authorization", this.clientToken).get(ClientResponse.class);
		/*
		 * if (response.getStatus() != 201) { throw new
		 * RuntimeException("Failed : HTTP error code : " + response.getStatus()); }
		 */
		return response.getEntity(String.class);
	}

	// device/[deviceno] - PUT (디바이스 번호로 디바이스 수정)
	public String modifyDevice(String device_no, String device_content, String ip) {
		Client client = Client.create();

		WebResource webResource = client.resource(hostURL + baseURL + "/device/" + device_no);

		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("device_content", device_content);
		mapData.put("ip", ip);

		String formData = makeFormData(mapData);

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
				.header("authorization", this.clientToken).put(ClientResponse.class, formData);
		/*
		 * if (response.getStatus() != 201) { throw new
		 * RuntimeException("Failed : HTTP error code : " + response.getStatus()); }
		 */
		return response.getEntity(String.class);
	}

	// device/[deviceno] - DELETE (디바이스 삭제)
	public String deleteDevice(String device_no) {
		Client client = Client.create();

		WebResource webResource = client.resource(hostURL + baseURL + "/device/" + device_no);

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON + ";charset=UTF-8")
				.header("authorization", this.clientToken).put(ClientResponse.class);
		/*
		 * if (response.getStatus() != 201) { throw new
		 * RuntimeException("Failed : HTTP error code : " + response.getStatus()); }
		 */
		return response.getEntity(String.class);
	}

}
