package com.newroad.cos.pilot;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A wrapper for an {@link HttpEntity} that can count the number of bytes
 * transferred. This is used internally to give updates for uploads.
 */
class ProgressHttpEntity extends HttpEntityWrapper {

	private final Logger logger = LoggerFactory
			.getLogger(ProgressHttpEntity.class);

	private final OssManagerListener listener;
	private final long length;
	private final Object userData;
	private boolean isAbort;

	public ProgressHttpEntity(final HttpEntity wrapped, long length,
			final OssManagerListener listener, Object userData) {
		super(wrapped);
		this.listener = listener;
		this.length = length;
		this.userData = userData;
		isAbort = false;

	}

	boolean isAbort() {
		return isAbort;
	}

	void setAbort(boolean isAbort) {
		this.isAbort = isAbort;
	}

	@Override
	public void writeTo(OutputStream out) throws IOException {
		wrappedEntity.writeTo(new CountingOutputStream(out));
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
								"ProgressHttpEntity write InterruptedException:",
								e);
					}
					delta_transferred += delta;
				}
			}
		}

		@Override
		public void write(int b) throws IOException {
			super.write(b);
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
