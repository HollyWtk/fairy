package com.fairy.utils.commons;

import java.sql.Connection;

public class ConnectionContext {

	/**
	 * 构造方法私有化，将ConnectionContext设计成单例
	 */
	private ConnectionContext() {

	}

	private static ConnectionContext connectionContext = new ConnectionContext();

	public static ConnectionContext getInstance() {
		return connectionContext;
	}

	private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<Connection>();

	public static void bind(Connection connection) {
		connectionThreadLocal.set(connection);
	}

	public static Connection getConnection() {
		return connectionThreadLocal.get();
	}

	public static void remove() {
		connectionThreadLocal.remove();
	}
}