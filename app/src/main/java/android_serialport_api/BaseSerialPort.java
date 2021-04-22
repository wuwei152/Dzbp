package android_serialport_api;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class BaseSerialPort {
    public abstract InputStream getInputStream();
    public abstract OutputStream getOutputStream();
    public abstract void close();
}
