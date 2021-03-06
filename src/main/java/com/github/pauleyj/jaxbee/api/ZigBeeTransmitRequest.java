/*
 * Copyright 2014 John C. Pauley
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.pauleyj.jaxbee.api;

import com.github.pauleyj.jaxbee.api.core.TxFrame;
import com.github.pauleyj.jaxbee.api.core.XBeeException;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ZigBeeTransmitRequest extends TxFrame<ZigBeeTransmitRequest> {
    private static final byte ADDRESS64_LENGTH = 0x08;
    private static final byte ADDRESS16_LENGTH = 0x02;
    private static final byte BROADCAST_RADIUS_SIZE = 0x01;
    private static final byte OPTIONS_LENGTH = 0x01;
    private static final byte BUFFER_ALLOCATION_CHUNK_SIZE = 0x04;

    public static final int MAX_DATA_BYTES = 72;
    public static final byte FRAME_TYPE = 0x10;

    private long destinationAddress64;
    private short destinationAddress16;
    private byte broadcastRadius;
    private byte options;
    private byte[] data;

    public long getDestinationAddress64() {
        return destinationAddress64;
    }

    public ZigBeeTransmitRequest setDestinationAddress64(long destinationAddress64) {
        this.destinationAddress64 = destinationAddress64;
        return this;
    }

    public short getDestinationAddress16() {
        return destinationAddress16;
    }

    public ZigBeeTransmitRequest setDestinationAddress16(short destinationAddress16) {
        this.destinationAddress16 = destinationAddress16;
        return this;
    }

    public byte getBroadcastRadius() {
        return broadcastRadius;
    }

    public ZigBeeTransmitRequest setBroadcastRadius(byte broadcastRadius) {
        this.broadcastRadius = broadcastRadius;
        return this;
    }

    public byte getOptions() {
        return options;
    }

    public ZigBeeTransmitRequest setOptions(byte options) {
        this.options = options;
        return this;
    }

    public byte[] getData() {
        return data;
    }

    public ZigBeeTransmitRequest setData(byte[] data) throws XBeeException {
        if (data == null) {
            throw new XBeeException("Null data not allowed");
        }
        if (data.length > MAX_DATA_BYTES) {
            throw new XBeeException("Data exceeds max buffer length of " + MAX_DATA_BYTES + " bytes");
        }
        this.data = Arrays.copyOf(data, data.length);
        return this;
    }

    private int getDataBytesSize() {
        int length = 0;
        if (data != null) {
            length = data.length;
        }
        return length;
    }

    @Override
    public byte getFrameType() {
        return FRAME_TYPE;
    }

    @Override
    public byte[] toBytes() {
        final int capacity = API_FRAME_TYPE_LENGTH + API_FRAME_ID_LENGTH + ADDRESS64_LENGTH + ADDRESS16_LENGTH + BROADCAST_RADIUS_SIZE + OPTIONS_LENGTH + getDataBytesSize();
        final ByteBuffer buffer =
            ByteBuffer.allocate(capacity)
                      .put(FRAME_TYPE)
                      .put(getFrameId())
                      .putLong(destinationAddress64)
                      .putShort(destinationAddress16)
                      .put(broadcastRadius)
                      .put(options);
        if (data != null && data.length > 0) {
            buffer.put(data);
        }
        return buffer.array();
    }
}
