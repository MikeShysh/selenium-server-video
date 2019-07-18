package com.portaone.videonode.utils;

import com.portaone.videonode.exception.RecordingException;
import org.zeroturnaround.exec.ProcessExecutor;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

public final class VideoRecordingUtils {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(VideoRecordingUtils.class);

	public static final String RECORDING_TOOL = "ffmpeg";
	private static final String MOVIE_FOLDER = File.separator + "tmp" + File.separator + "video";
	private static final String EXTENSION = ".mp4";
	private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	public static void startFFmpeg(String fileName) {

		File videoFolder = new File(MOVIE_FOLDER);
		if (!videoFolder.exists()) {
			videoFolder.mkdirs();
		}
		final File videoFileName = getFile(fileName);

		final String[] commandsSequence = new String[]{
				"ffmpeg",
				"-y",
				"-video_size", getResolution(),
				"-f", "x11grab",
				"-i", System.getenv("DISPLAY"),
				"-an",
				"-r", "12",
				videoFileName.toString()
		};

		CompletableFuture.supplyAsync(() -> runCommand(commandsSequence))
				.whenCompleteAsync((output, errors) -> {
					log.info("Start recording output log: " + output + (errors != null ? "; ex: " + errors : ""));
				});
	}

	public static void stopFFmpeg() {
		String killLog = runCommand("pkill", "-INT", RECORDING_TOOL);
		log.info("Process kill output: " + killLog);
	}

	public static String doVideoProcessing(boolean successfulTest, String fileName) {
		File destFile =  getFile(fileName);
		if (!successfulTest) {
			log.info("Video recording: " + destFile);
			return destFile.toString();
		} else {
			destFile.delete();
			log.info("No video on success test");
		}
		return "";
	}

	private static File getFile(final String filename) {
		File movieFolder = new File(MOVIE_FOLDER);
		return new File(movieFolder + File.separator + filename + EXTENSION);
	}

	private static String getResolution() {
		return (int) screenSize.getWidth() + "x" + (int) screenSize.getHeight();
	}

	public static String runCommand(final String... args) {
		log.info("Trying to execute the following command: " + Arrays.asList(args));
		try {
			return new ProcessExecutor()
					.command(args)
					.readOutput(true)
					.execute()
					.outputUTF8();
		} catch (IOException | InterruptedException | TimeoutException e) {
			log.warn("Unable to execute command: " + e);
			throw new RecordingException(e);
		}
	}

	public static String getStackTrace(final Throwable throwable) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}

}

