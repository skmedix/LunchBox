package org.apache.commons.io.input;

public interface TailerListener {

    void init(Tailer tailer);

    void fileNotFound();

    void fileRotated();

    void handle(String s);

    void handle(Exception exception);
}
