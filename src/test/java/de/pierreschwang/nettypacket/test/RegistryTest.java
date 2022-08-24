/*
 * Copyright (c) 2021, Pierre Maurice Schwang <mail@pschwang.eu> - MIT
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package de.pierreschwang.nettypacket.test;

import de.pierreschwang.nettypacket.Packet;
import de.pierreschwang.nettypacket.buffer.PacketBuffer;
import de.pierreschwang.nettypacket.exception.PacketRegistrationException;
import de.pierreschwang.nettypacket.registry.IPacketRegistry;
import de.pierreschwang.nettypacket.registry.SimplePacketRegistry;
import de.pierreschwang.nettypacket.test.dummy.SimpleValidPacket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RegistryTest {

    private static class Packet1337 extends Packet {

        @Override
        public void write(PacketBuffer buffer) {
        }

        @Override
        public void read(PacketBuffer buffer) {
        }

        @Override
        public int getPacketId() {
            return 1337;
        }
    }

    private IPacketRegistry registry;

    /**
     * Re-initialize the registry before each test run to ensure a clean registry.
     */
    @BeforeEach
    void setupBeforeEach() {
        this.registry = new SimplePacketRegistry();
    }

    @Test
    @DisplayName("If the passed packet id is already in use - and therefore invalid - a PacketRegistrationException must be thrown")
    void duplicatePacketId() {
        Assertions.assertThrows(PacketRegistrationException.class, () -> {
            this.registry.registerPacket(1, SimpleValidPacket::new);
            this.registry.registerPacket(1, SimpleValidPacket::new);
        });
    }

    @Test
    @DisplayName("constructPacket() should return null if the passed id does not belong to a registered packet")
    void constructUnregisteredPacket() {
        Assertions.assertNull(this.registry.constructPacket(100));
    }

    @Test
    @DisplayName("getPacketId() should return the matching id if the packet is registered")
    void getPacketIdShouldReturnMatchingIdOfPacketClass() throws PacketRegistrationException {
        this.registry.registerPacket(1337, Packet1337::new);
        Assertions.assertEquals(1337, this.registry.constructPacket(1337).getPacketId());
    }

    @Test
    @DisplayName("containsPacketId(int) should return true if a packet with the passed id is registered - otherwise false")
    void containsPacketIdShouldReturnCorrectBoolValue() throws PacketRegistrationException {
        this.registry.registerPacket(1337, Packet1337::new);
        Assertions.assertTrue(this.registry.isRegistered(1337));
        Assertions.assertFalse(this.registry.isRegistered(1338));
    }
}