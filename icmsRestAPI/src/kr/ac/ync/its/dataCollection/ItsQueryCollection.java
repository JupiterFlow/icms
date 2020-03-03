package kr.ac.ync.its.dataCollection;

public interface ItsQueryCollection {
	String SELECT_MEMBER = "SELECT * FROM `member` WHERE id='%s'";
	String LOGIN_MEMBER  = "SELECT * FROM `member` WHERE id='%s' AND password='%s';";
	String INSERT_MEMBER = "INSERT INTO `member` (`id`, `password`, `telephone`, `name`, `email`) VALUES ('%s', '%s', '%s', '%s', '%s');";
	String UPDATE_MEMBER = "UPDATE `member` SET `password`='%s', `telephone`='%s', `name`='%s', `email`='%s' WHERE (`id` = '%s');";
	String DELETE_MEMBER = "DELETE FROM `member` where id='%s';";

	String OWNED_CONTENT_BY_MEMBER = "SELECT * FROM `content` WHERE content_owner='%s';";
	String OWNED_DEVICE_BY_MEMBER  = "SELECT * FROM `device` WHERE device_owner='%s';";
	
	
	String SELECT_CONTENT = "SELECT * FROM `content` where content_no='%s' and content_owner='%s'";
	String INSERT_CONTENT = "INSERT INTO `content` (`type`, `content_path`, `detail`, `content_owner`) VALUES ('%s', '%s', '%s', '%s');";
	String UPDATE_CONTENT = "UPDATE `content` SET `type` = '%s', `content_path` = '%s', `detail` = '%s' WHERE content_no='%s' and content_owner='%s';";
	String DELETE_CONTENT = "DELETE FROM `content` where content_no='%s' and content_owner='%s';";

	String SELECT_DEVICE = "SELECT * FROM `device` where device_no='%s' and device_owner='%s'";
	String INSERT_DEVICE = "INSERT INTO `device` (`device_owner`, `address`, `ip`) VALUES ('%s', '%s', '%s');";
	String UPDATE_DEVICE = "UPDATE `device` SET `ip` = '%s', `device_content` = '%s' WHERE device_no='%s' and device_owner='%s';";
	String DELETE_DEVICE = "DELETE FROM `device` WHERE device_no='%s' and device_owner='%s';";	
}
