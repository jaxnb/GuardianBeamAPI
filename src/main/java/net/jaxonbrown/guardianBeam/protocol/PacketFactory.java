/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2016 Jaxon A Brown
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 *  rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 *  persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 *  OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package net.jaxonbrown.guardianBeam.protocol;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.injector.BukkitUnwrapper;
import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.BukkitConverters;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.google.common.collect.Lists;

/**
 * The PacketFactory generates and modifies the packets for the library to use.
 * If you are looking into making your own Guardian Beam type, this class is for you.
 * @author Jaxon A Brown
 */
public class PacketFactory {

	private static Entity fakeSquid;
	private static Entity fakeGuardian;

	static {
		fakeSquid = (Entity) Accessors.getConstructorAccessor(
				MinecraftReflection.getCraftBukkitClass("entity.CraftSquid"),
				MinecraftReflection.getCraftBukkitClass("CraftServer"),
				MinecraftReflection.getMinecraftClass("world.entity.animal.EntitySquid")
		).invoke(Bukkit.getServer(), Accessors.getConstructorAccessor(
				MinecraftReflection.getMinecraftClass("world.entity.animal.EntitySquid"),
				MinecraftReflection.getMinecraftClass("world.entity.EntityTypes"),
				MinecraftReflection.getNmsWorldClass()
		).invoke(new Object[] {BukkitConverters.getEntityTypeConverter().getGeneric(EntityType.SQUID),
				BukkitUnwrapper.getInstance().unwrapItem(Bukkit.getWorlds().get(0))}));

		fakeGuardian = (Entity) Accessors.getConstructorAccessor(
				MinecraftReflection.getCraftBukkitClass("entity.CraftGuardian"),
				MinecraftReflection.getCraftBukkitClass("CraftServer"),
				MinecraftReflection.getMinecraftClass("world.entity.monster.EntityGuardian")
		).invoke(Bukkit.getServer(), Accessors.getConstructorAccessor(
				MinecraftReflection.getMinecraftClass("world.entity.monster.EntityGuardian"),
				MinecraftReflection.getMinecraftClass("world.entity.EntityTypes"),
				MinecraftReflection.getNmsWorldClass()
				).invoke(new Object[] {BukkitConverters.getEntityTypeConverter().getGeneric(EntityType.GUARDIAN),
						BukkitUnwrapper.getInstance().unwrapItem(Bukkit.getWorlds().get(0))}));
	}

	public static WrappedBeamPacket createPacketSquidSpawn(Location location) {
		PacketContainer container = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
		int entityID = EIDGen.generateEID();
		container.getIntegers().write(0, entityID);
		container.getUUIDs().write(0, UUID.randomUUID());
		container.getEntityTypeModifier().write(0, EntityType.SQUID);
		container.getDoubles().write(0, location.getX());
		container.getDoubles().write(1, location.getY());
		container.getDoubles().write(2, location.getZ());
		container.getBytes().write(0, (byte) (location.getYaw() * 256.0F / 360.0F));
		container.getBytes().write(1, (byte) (location.getPitch() * 256.0F / 360.0F));

		WrapperPlayServerEntityMetadata wrapper = new WrapperPlayServerEntityMetadata();
		WrappedDataWatcher watcher = WrappedDataWatcher.getEntityWatcher(fakeSquid);
		// Invisible
		watcher.setObject(0, Registry.get(Byte.class), (byte) 0x20);
		wrapper.setMetadata(watcher.getWatchableObjects());
		wrapper.setEntityID(entityID);
		return new WrappedBeamPacket(container, wrapper);
	}

	public static WrappedBeamPacket createPacketGuardianSpawn(Location location, WrappedBeamPacket squidPacket) {
		PacketContainer container = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
		int entityID = EIDGen.generateEID();
		container.getIntegers().write(0, entityID);
		container.getUUIDs().write(0, UUID.randomUUID());
		container.getEntityTypeModifier().write(0, EntityType.GUARDIAN);
		container.getDoubles().write(0, location.getX());
		container.getDoubles().write(1, location.getY());
		container.getDoubles().write(2, location.getZ());
		container.getBytes().write(0, (byte) (location.getYaw() * 256.0F / 360.0F));
		container.getBytes().write(1, (byte) (location.getPitch() * 256.0F / 360.0F));

		WrapperPlayServerEntityMetadata wrapper = new WrapperPlayServerEntityMetadata();
		WrappedDataWatcher watcher = WrappedDataWatcher.getEntityWatcher(fakeGuardian);
		// Invisible
		watcher.setObject(0, Registry.get(Byte.class), (byte) 0x20);
		// Is retracting spikes
		watcher.setObject(16, false);
		// Target EID
		watcher.setObject(17, squidPacket.getHandle().getIntegers().read(0));
		wrapper.setMetadata(watcher.getWatchableObjects());
		wrapper.setEntityID(entityID);
		return new WrappedBeamPacket(container, wrapper);
	}

	public static WrappedBeamPacket modifyPacketEntitySpawn(WrappedBeamPacket entitySpawnPacket, Location location) {
		PacketContainer container = entitySpawnPacket.getHandle();
		container.getIntegers().write(2, (int) Math.floor(location.getX() * 32.0));
		container.getIntegers().write(3, (int) Math.floor(location.getY() * 32.0));
		container.getIntegers().write(4, (int) Math.floor(location.getZ() * 32.0));
		container.getBytes().write(0, (byte) (location.getYaw() * 256.0F / 360.0F));
		container.getBytes().write(1, (byte) (location.getPitch() * 256.0F / 360.0F));
		return entitySpawnPacket;
	}

	public static WrappedBeamPacket createPacketEntityMove(WrappedBeamPacket entityPacket) {
		PacketContainer container = new PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT);
		container.getIntegers().write(0, entityPacket.getHandle().getIntegers().read(0));
		return new WrappedBeamPacket(container);
	}

	public static WrappedBeamPacket modifyPacketEntityMove(WrappedBeamPacket entityMovePacket, Location location) {
		PacketContainer container = entityMovePacket.getHandle();
		container.getIntegers().write(1, (int) Math.floor(location.getX() * 32.0D));
		container.getIntegers().write(2, (int) Math.floor(location.getY() * 32.0D));
		container.getIntegers().write(3, (int) Math.floor(location.getZ() * 32.0D));
		container.getBytes().write(0, (byte) (location.getYaw() * 256.0F / 360.0F));
		container.getBytes().write(1, (byte) (location.getPitch() * 256.0F / 360.0F));
		return entityMovePacket;
	}

	public static WrappedBeamPacket createPacketRemoveEntities(WrappedBeamPacket squidPacket, WrappedBeamPacket guardianPacket) {
		PacketContainer container = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
		container.getIntLists().write(0, Lists.newArrayList(
				squidPacket.getHandle().getIntegers().read(0),
				guardianPacket.getHandle().getIntegers().read(0)));
		return new WrappedBeamPacket(container);
	}

}
