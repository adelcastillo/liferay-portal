/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.server.manager.internal.executor;

import com.liferay.portal.kernel.util.ServerDetector;
import com.liferay.server.manager.executor.Executor;

import java.io.File;

import org.osgi.service.component.annotations.Component;

/**
 * @author Jonathan Potter
 * @author Brian Wing Shun Chan
 */
@Component(
	immediate = true,
	property = {"server.manager.executor.path=/server/log/error"},
	service = Executor.class
)
public class ErrorLogExecutor extends OutputLogExecutor {

	@Override
	protected File getLogFile() {
		File logFile = null;

		if (ServerDetector.isGlassfish()) {
			File logDirectory = new File(
				System.getProperty("catalina.home"), "logs");

			logFile = new File(logDirectory, "server.log");
		}
		else if (ServerDetector.isJBoss()) {
			File logDirectory = new File(
				System.getProperty("jboss.server.log.dir"));

			logFile = new File(logDirectory, "boot.log");
		}
		else if (ServerDetector.isTomcat()) {
			logFile = new File(
				System.getProperty("catalina.base") + "/logs/catalina." +
					getTomcatDateString() + ".log");
		}

		return logFile;
	}

}