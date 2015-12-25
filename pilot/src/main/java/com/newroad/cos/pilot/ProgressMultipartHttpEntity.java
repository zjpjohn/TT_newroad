package com.newroad.cos.pilot;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @info ProgressMultipartHttpEntity
 * @author tangzj1
 * @date Nov 18, 2013
 * @version
 */
@SuppressWarnings("deprecation")
class ProgressMultipartHttpEntity extends MultipartEntity {

	private final Logger logger = LoggerFactory
			.getLogger(ProgressMultipartHttpEntity.class);

	private final OssManagerListener listener;
	private final long length;
	private final Object userData;
	private boolean isAbort;

	public ProgressMultipartHttpEntity(final HttpMultipartMode mode,
			final OssManagerListener listener, long length, Object userData) {
		super(mode);
		this.listener = listener;
		this.length = length;
		this.userData = userData;
	}

	public ProgressMultipartHttpEntity(HttpMultipartMode mode,
			final String boundary, final Charset charset,
			final OssManagerListener listener, long length, Object userData) {
		super(mode, boundary, charset);
		this.listener = listener;
		this.length = length;
		this.userData = userData;

	}

	boolean isAbort() {
		return isAbort;
	}

	void setAbort(boolean isAbort) {
		this.isAbort = isAbort;
	}

	@Override
	public void writeTo(OutputStream out) throws IOException {
		super.writeTo(new CountingOutputStream(out));
	}

	private class CountingOutputStream extends FilterOutputStream {
		private long lastListened = 0;
		private long intervalMs = 0;
		private long transferred = 0;

		public CountingOutputStream(final OutputStream out) {
			super(out);
			intervalMs = listener.getProgressInterval();
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			// Using out.write instead of super.write speeds it up because
			// the superclass seems to default to byte-by-byte transfers.
			boolean status = true;
			out.write(b, off, len);
			if (len < 1024 * 10) {
				transferred += len;
				long now = System.currentTimeMillis();
				if (now - lastListened > intervalMs) {
					lastListened = now;
					status = listener.onProgress(transferred, length, userData);
					if (!status && transferred < length) {
						isAbort = true;
						throw new IOException("user write abort");
					}
				}
			} else {
				long delta = len / 10;
				long delta_transferred = 0;
				while (delta_transferred <= len) {
					transferred += delta;
					status = listener.onProgress(transferred, length, userData);
					if (!status) {
						isAbort = true;
						throw new IOException("user write abort");
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						logger.error(
								"ProgressMultipartHttpEntity write InterruptedException:",
								e);
					}
					delta_transferred += delta;
				}
			}
		}

		@Override
		public void write(int b) throws IOException {
			out.write(b);
			this.transferred++;
			long now = System.currentTimeMillis();
			if (now - lastListened > intervalMs) {
				lastListened = now;
				boolean status = listener.onProgress(this.transferred, length,
						userData);
				if (!status && transferred < length) {
					isAbort = true;
					throw new IOException("user write abort");
				}
			}
		}
	}

}
