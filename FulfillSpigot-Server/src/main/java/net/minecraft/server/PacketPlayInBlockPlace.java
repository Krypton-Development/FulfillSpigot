package net.minecraft.server;

import io.netty.handler.codec.DecoderException;
import xyz.swift.spigot.config.FulfillSpigotConfig;

import java.io.IOException;

public class PacketPlayInBlockPlace implements Packet<PacketListenerPlayIn> {

    private static final BlockPosition a = new BlockPosition(-1, -1, -1);
    private BlockPosition b;
    private int c;
    private ItemStack d;
    private float e;
    private float f;
    private float g;

    public long timestamp; // CraftBukkit

    public PacketPlayInBlockPlace() {}

    public PacketPlayInBlockPlace(ItemStack itemstack) {
        this(PacketPlayInBlockPlace.a, 255, itemstack, 0.0F, 0.0F, 0.0F);
    }

    public PacketPlayInBlockPlace(BlockPosition blockposition, int i, ItemStack itemstack, float f, float f1, float f2) {
        this.b = blockposition;
        this.c = i;
        this.d = itemstack != null ? itemstack.cloneItemStack() : null;
        this.e = f;
        this.f = f1;
        this.g = f2;
    }

    public void a(PacketDataSerializer serializer) throws IOException {
        timestamp = System.currentTimeMillis(); // CraftBukkit
        this.b = serializer.c();
        this.c = serializer.readUnsignedByte();
        // FulfillSpigot start - don't parse itemstack

        if (!FulfillSpigotConfig.stopDecodingItemStackOnPlace) {
            this.d = serializer.decodeItemStack();
        } else {
            // Consume everything and leave 3 bytes at the end
            if (serializer.readableBytes() < 3) {
                throw new DecoderException("Expected 3 facing bytes");
            }
            serializer.skipBytes(serializer.readableBytes() - 3);
        }

        // FulfillSpigot end
        this.e = (float) serializer.readUnsignedByte() / 16.0F;
        this.f = (float) serializer.readUnsignedByte() / 16.0F;
        this.g = (float) serializer.readUnsignedByte() / 16.0F;
    }

    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.a(this.b);
        packetdataserializer.writeByte(this.c);
        packetdataserializer.a(this.d);
        packetdataserializer.writeByte((int) (this.e * 16.0F));
        packetdataserializer.writeByte((int) (this.f * 16.0F));
        packetdataserializer.writeByte((int) (this.g * 16.0F));
    }

    public void a(PacketListenerPlayIn packetlistenerplayin) {
        packetlistenerplayin.a(this);
    }

    public BlockPosition a() {
        return this.b;
    }

    public int getFace() {
        return this.c;
    }

    public ItemStack getItemStack() {
        return this.d;
    }

    public float d() {
        return this.e;
    }

    public float e() {
        return this.f;
    }

    public float f() {
        return this.g;
    }
}
