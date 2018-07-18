package com.amglhit.mnetwork.progress

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import okio.*
import timber.log.Timber
import java.io.IOException

/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class ProgressInterceptor(private val progressListener: ProgressListener) : Interceptor {
  val sampleProgressListener = object : ProgressListener {
    private var firstUpdate = false
    override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
      if (done) {
        Timber.v("completed")
      } else {
        if (firstUpdate) {
          firstUpdate = false
          if (contentLength == -1L) {
            Timber.v("content-length: unknown")
          } else {
            Timber.v("content_length: $contentLength")
          }
        }

        Timber.v("bytesRead: $bytesRead")

        if (contentLength != -1L) {
          Timber.v("done: ${(100 * bytesRead) / contentLength}")
        }
      }
    }
  }

  override fun intercept(chain: Interceptor.Chain): Response {
    val originalResponse = chain.proceed(chain.request())
    return originalResponse.newBuilder()
      .body(ProgressResponseBody(originalResponse.body(), progressListener))
      .build()
  }

  private class ProgressResponseBody(
    private val responseBody: ResponseBody?,
    private val progressListener: ProgressListener?
  ) :
    ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentLength(): Long = responseBody?.contentLength() ?: 0

    override fun contentType(): MediaType? = responseBody?.contentType()

    override fun source(): BufferedSource? {
      if (bufferedSource == null && responseBody != null) {
        bufferedSource = Okio.buffer(source(responseBody.source()))
      }
      return bufferedSource
    }

    private fun source(source: Source): Source {
      return object : ForwardingSource(source) {
        private var totalBytesRead = 0L

        @Throws(IOException::class)
        override fun read(sink: Buffer, byteCount: Long): Long {
          val bytesRead = super.read(sink, byteCount)
          // read() returns the number of bytes read, or -1 if this source is exhausted.
          totalBytesRead += if (bytesRead != -1L) bytesRead else 0
          progressListener?.update(
            totalBytesRead,
            responseBody?.contentLength() ?: 0,
            bytesRead == -1L
          )
          return bytesRead
        }
      }
    }
  }

  interface ProgressListener {
    fun update(bytesRead: Long, contentLength: Long, done: Boolean)
  }
}