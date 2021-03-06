package com.portaone.servlet.core;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.portaone.servlet.utils.VideoRecordingUtils.doVideoProcessing;
import static com.portaone.servlet.utils.VideoRecordingUtils.getStackTrace;
import static com.portaone.servlet.utils.VideoRecordingUtils.startFFmpeg;
import static com.portaone.servlet.utils.VideoRecordingUtils.stopFFmpeg;

public class VideoServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	private void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String path = req.getPathInfo();
		String fileName = getFileName(req);
		try {
			switch (path) {
				case "/start":
					startFFmpeg(fileName);
					updateResponse(resp, 200, "recording started");
					break;
				case "/stop":
					stopFFmpeg();
					String filePath = doVideoProcessing(isSuccess(req), fileName);
					updateResponse(resp, 200, "recording stopped " + filePath);
					break;
			}
		} catch (Exception ex) {
			updateResponse(resp, 500,
					"Internal server error occurred while trying to start / stop recording: " + getStackTrace(ex));
		}
	}

	private String getFileName(HttpServletRequest req) {
		String name = req.getParameter("name");
		if (name == null || "null".equalsIgnoreCase(name)) {
			return "video";
		}
		return name;
	}

	private boolean isSuccess(HttpServletRequest req) {
		String result = req.getParameter("result");
		if (result == null) {
			return false;
		}
		return Boolean.valueOf(result);
	}

	public static void updateResponse(final HttpServletResponse response, final int status, final String message) throws IOException {
		response.setStatus(status);
		response.getWriter().write(message);
	}
}
