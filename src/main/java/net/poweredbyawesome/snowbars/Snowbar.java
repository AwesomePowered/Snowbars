package net.poweredbyawesome.snowbars;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Snowbar implements ConfigurationSerializable {

    private Block base;
    private Block currentBlock;
    private int maxHeight; // Amount in blocks.
    private int baseHeight = 0;
    private int taskId;
    private boolean up = true;
    private boolean isRandom;

    public Snowbar(Block block, int maxHeight, boolean isRandom) {
        this.base = block;
        this.currentBlock = block;
        this.maxHeight = maxHeight;
        this.isRandom = isRandom;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("base", base.getLocation());
        map.put("currentBlock", currentBlock.getLocation());
        map.put("maxHeight", maxHeight);
        map.put("baseHeight", baseHeight);
        map.put("up", up);
        map.put("isRandom", isRandom);
        return map;
    }

    public Snowbar(Map<String, Object> map)
    {
        this.base = ((Location)map.get("base")).getBlock();
        this.currentBlock = ((Location)map.get("currentBlock")).getBlock();
        this.maxHeight = (int)map.get("maxHeight");
        this.baseHeight = (int)map.get("baseHeight");
        this.up = (boolean)map.get("up");
        visualize();
    }

    public Block getBase() {
        return base;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public int getBaseHeight() {
        return baseHeight;
    }

    private int getMaxH() {
        return maxHeight * 7;
    }

    public void destroy() {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    public int getTaskId() {
        return taskId;
    }

    private void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public boolean getIsRandom() {
        return isRandom;
    }

    public void setIsRandom(Boolean bool) {
        this.isRandom = bool;
    }

    private int getThreadRandom() {
        return ThreadLocalRandom.current().nextInt(maxHeight * 7);
    }

    private Block upBlock() {
        if (getBaseHeight() < getMaxH()) {
            currentBlock = currentBlock.getLocation().add(0,1,0).getBlock();
            currentBlock.setType(Material.SNOW);
            return currentBlock;
        }
        up = false;
        return currentBlock;
    }

    private Block downBlock() {
        if (currentBlock.getY() > base.getY()) {
            currentBlock = currentBlock.getLocation().add(0,-1,0).getBlock();
            currentBlock.setType(Material.SNOW);
            currentBlock.setData((byte) 7);
            return currentBlock;
        }
        if (isRandom) {
            baseHeight = getThreadRandom();
        }
        up = true;
        return base;
    }



    public void visualize() {
        base.setType(Material.SNOW);
        setTaskId(Bukkit.getScheduler().scheduleSyncRepeatingTask(Snowbars.intance, () -> {
            if (getBaseHeight() >= getMaxH()) {
                up = false;
            }
            if (up) {
                if (currentBlock.getData() == 7) {
                    baseHeight++;
                    upBlock();
                } else {
                    currentBlock.setData((byte) (currentBlock.getData()+1));
                    baseHeight++;
                }
                moveEntities();
            } else {
                if (currentBlock.getData() == 0) {
                    baseHeight--;
                    downBlock();
                } else {
                    baseHeight--;
                    currentBlock.setData((byte) (currentBlock.getData()-1));
                }
            }
        }, 0, 1));
    }

    public void moveEntities() {
        for (Entity e : currentBlock.getLocation().getChunk().getEntities()) {
            if (e.getLocation().distanceSquared(currentBlock.getLocation()) < 2.25) {
                e.setVelocity(e.getVelocity().add(new Vector(0.0, 0.1, 0.0)));
            }
        }
    }

    public boolean isSnowbar(Location loc) {
        if (getBase().getX() == loc.getX() && getBase().getZ() == loc.getZ()) {
            int why = loc.getBlockY();
            return  (why >= getBase().getY() && why <= (getBase().getY()+maxHeight));
        }
        return false;
    }
}
