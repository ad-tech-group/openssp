package com.atg.openssp.unittest;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;

public class UnitTestHttpExchange extends HttpExchange{
    private InputStream bodyStream;
    private int status;
    private long length;
    private OutputStream outStream;

    public UnitTestHttpExchange(String body) {
        bodyStream = new ByteArrayInputStream(body.getBytes());
        outStream = new ByteArrayOutputStream();
    }

    @Override
    public Headers getRequestHeaders() {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public Headers getResponseHeaders() {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public URI getRequestURI() {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public String getRequestMethod() {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public HttpContext getHttpContext() {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public void close() {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public InputStream getRequestBody() {
        return bodyStream;
    }

    @Override
    public OutputStream getResponseBody() {
        return outStream;
    }

    @Override
    public void sendResponseHeaders(int status, long length) throws IOException {
        this.status = status;
        this.length = length;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        InetSocketAddress address = new InetSocketAddress("time.com", 80);
        return address;
    }

    @Override
    public int getResponseCode() {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return new InetSocketAddress("friendly.com", 20);
    }

    @Override
    public String getProtocol() {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public Object getAttribute(String s) {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public void setAttribute(String s, Object o) {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public void setStreams(InputStream inputStream, OutputStream outputStream) {
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public HttpPrincipal getPrincipal() {
        throw new RuntimeException("not implemented yet");
    }

    public int getTestStatus() {
        return status;
    }

    public long getTestLength() {
        return length;
    }

    public String getTestResult() {
        return outStream.toString();
    }
}
