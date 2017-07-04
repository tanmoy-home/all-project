package com.rssoftware.ou.cbsiso.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.core.serializer.Serializer;

/**
 * A byte array serializer that does nothing with the payload; sends it raw to IMPS TCP socket.
 * For use with IMPS/FRM Spring Integration TCP Socket.
 */
public class UPICustomRawSerializer implements Serializer<byte[]> {
	
@Override
public void serialize(byte[] object, OutputStream outputStream)
		throws IOException {
	outputStream.write(object);
	
}

}
