package com.readme.database;

public class Constants {
	

	
		public static final String DATABASE_NAME = "Notes.db";
	
		public static final int VERSION = 1;

		

	
		/**用户表*/
		public static class UserTable {
			/**表名*/
			public static final String TABLE_NAME = "user";
			public static final String ID = "id";
		    public static final String USER_NAME = "user";
		    public static final String PASSWORD = "password";
		    public static final String TOTAL_SIZE = "total_size";
		    public static final String USED_SIZE = "used_size";
		    public static final String REGISTER_TIME = "register_time";
		    public static final String LAST_LOGIN_TIME = "last_login_time";
		    public static final String LAST_MODIFY_TIME = "last_modify_time";
		    public static final String DEFAULT_NOTEBOOK = "default_notebook";

		}
		
		/**笔记本表*/
		public static class NotebookTable{
			/**表名*/
			public static final String TABLE_NAME = "notebook";
			public static final String PATH = "path";
		    public static final String NAME = "name";
		    public static final String NOTES = "notes_num";
		    public static final String CREATE_TIME = "create_time";
		    public static final String MODIFY_TIME = "modify_time";
		}
		
		/**笔记表*/
		public static class NoteTable {
			/**表名*/
			   public static final String TABLE_NAME = "note";
			   public static final String ID = "id";
			   public static final String PATH = "path";
			    public static final String TITLE = "title";
			    public static final String AUTHOR = "author";
			    public static final String SOURCE = "source";
			    public static final String SIZE = "size";
			    public static final String CREATE_TIME = "create_time";
			    public static final String MODIFY_TIME = "modify_time";
			    public static final String CONTENT = "content";
			    public static final String LOCAL_PATH = "local_path";
			   
		}
		
		/**资源表*/
		public static class ResourceTable {
			/**表名*/
			public static final String TABLE_NAME = "resource";
			public static final String SRC = "src";
		    public static final String URL = "url";
		  
		    public static final String LOCAL_URL = "local_url";
		 
		}
		
}
