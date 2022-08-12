package me.xtrm.paladium.palatest.server.database.dao.model;

import cpw.mods.fml.server.FMLServerHandler;
import lombok.Data;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Pattern;

public @Data class GrinderCraftModel {
    private static final DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
            .withZone(ZoneId.systemDefault());
    private transient int id;

    private String playerName;
    private String craftedItem;
    private String craftDate;
    private String worldName;
    private float x;
    private float y;
    private float z;

    public GrinderCraftModel(EntityPlayer player, ItemStack craftedItem,
                             Instant craftDate, World world) {
        this.playerName = player.getDisplayName();
        this.craftedItem =
            Item.itemRegistry.getNameForObject(craftedItem.getItem())
                + "@"
                + craftedItem.getMetadata();
        this.craftDate = formatter.format(craftDate);
        this.worldName = world.getWorldInfo().getWorldName();
        this.x = (float) player.posX;
        this.y = (float) player.posY;
        this.z = (float) player.posZ;
    }

    public EntityPlayer getPlayer() {
        return FMLServerHandler.instance()
            .getServer()
            .getConfigurationManager()
            .getPlayerByUsername(this.playerName);
    }

    public ItemStack getItem() {
        String[] data = this.craftedItem.split(Pattern.quote("@"));
        try {
            return new ItemStack(
                (Item) Item.itemRegistry.getObject(data[0]),
                1,
                Integer.parseInt(data[1])
            );
        } catch (ClassCastException exception) {
            return new ItemStack(
                (Block) Block.blockRegistry.getObject(data[0]),
                1,
                Integer.parseInt(data[1])
            );
        }
    }

    public World getWorld() {
        return Arrays.stream(FMLServerHandler.instance().getServer().worldServers)
            .filter(it -> it.getWorldInfo().getWorldName().equalsIgnoreCase(this.worldName))
            .findFirst()
            .orElse(null);
    }
}
