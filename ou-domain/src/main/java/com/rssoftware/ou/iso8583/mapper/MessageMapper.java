package com.rssoftware.ou.iso8583.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rssoftware.ou.iso8583.message.ISO8583;

@FunctionalInterface
public interface MessageMapper<T,V extends ISO8583> {
	V mapTo(T t);
}
