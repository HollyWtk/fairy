package com.fairy.utils.data;

public class MngResponse {

	public int code;

	public String message;

	public Object data;

	public boolean msgWindow;




	public String actionView;

	public String actionName;

	public String actionOp;

	public Object condition;

	public int   opType;  //1 普通请求数据  2 下载文件  3 页面跳转
	

	public boolean isSucess() {
		return code == 0;
	}

	public static MngResponse SUCESS() {

		MngResponse ret = new MngResponse();

		ret.code = 0;

		ret.msgWindow = false;

		ret.message = "操作成功";

		return ret;
	}

	public static MngResponse ERROR_INNOR() {

		MngResponse ret = new MngResponse();

		ret.code = -1;

		ret.msgWindow = true;

		ret.message = "系统内部错误 ! 程序猿小哥正在抢修,请稍后再试";

		return ret;

	}

	public static MngResponse ERROR(int error_code, String msg) {

		MngResponse ret = new MngResponse();

		ret.code = error_code;

		ret.msgWindow = false;

		ret.message = msg;

		return ret;

	}

	public static MngResponse ERROR_MESSAGE(int error_code, String msg) {

		MngResponse ret = new MngResponse();

		ret.code = error_code;

		ret.msgWindow = true;

		ret.message = msg;

		return ret;

	}

	public static MngResponse SUCESS_RESULT(Object data) {

		MngResponse ret = new MngResponse();

		ret.code = 0;

		ret.message = "操作成功";

		ret.msgWindow = false;

		ret.data = data;

		return ret;

	}


	public static MngResponse SUCESS_MESSAGE(String message) {

		MngResponse ret = new MngResponse();

		ret.code = 0;

		ret.msgWindow = true;

		ret.message = message;

		
		return ret;

	}


	public MngResponse WITH_ACTION(String actionName,String actionView,String actionOp,Object condition) {

		this.actionName = actionName;
		this.actionView = actionView;
		this.condition = condition;
		this.actionOp = actionOp;
		this.opType = 1;

		return this;

	}

	public MngResponse WITH_ACTION_FILE(String actionName,String actionView,String actionOp,Object condition) {

		this.actionName = actionName;
		this.actionView = actionView;
		this.condition = condition;
		this.actionOp = actionOp;
		this.opType = 2;
		
		return this;

	}

	public MngResponse WITH_ACTION_VIEWLINK(String actionName,String actionView,String actionOp,Object condition) {

		this.actionName = actionName;
		this.actionView = actionView;
		this.condition = condition;
		this.actionOp = actionOp;
		this.opType = 3;
		
		return this;

	}


}
