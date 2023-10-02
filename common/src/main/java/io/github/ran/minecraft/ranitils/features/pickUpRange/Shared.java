package io.github.ran.minecraft.ranitils.features.pickUpRange;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

import java.util.ArrayList;
import java.util.List;

public class Shared {
    public static boolean blink = false; // A flag for 'blinking' (no sending movement packets)
    public static boolean dump = false;  // A flag for controlling packet dumping (sending the cached packets to the server)
    public static final List<Packet<?>> packets = new ArrayList<>(); // A list of queued packets that'll be sent when blink is disabled
    public static boolean clearing = false; // A flag for clearing the queued packets
    public static ServerboundMovePlayerPacket lastPacket = null; // The last packet sent to the server
}
