package net.poweredbyawesome.snowbars;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

public class Snowbar {

    private Block base;
    private Block currentBlock;
    private int maxHeight; // Amount in blocks.
    private int baseHeight = 0;
    private int speed = 5;
    private int taskId;
    private boolean up = true;

    public Snowbar(Block block, int maxHeight, int speed) {
        this.base = block;
        this.currentBlock = block;
        this.maxHeight = maxHeight;
        this.speed = speed;
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
        baseHeight = getRandom();
        up = true;
        return base;
    }

    public int getBaseHeight() {
        return baseHeight;
    }

    private int getMaxH() {
        return maxHeight * 7;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    private int getRandom() {
        return ThreadLocalRandom.current().nextInt(maxHeight * 7);
    }

    public void destroy() {
        Bukkit.getScheduler().cancelTask(taskId);
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
        }, 0, speed));
    }

    public int getTaskId() {
        return taskId;
    }

    private void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void moveEntities() {
        for (Entity e : currentBlock.getLocation().getChunk().getEntities()) {
            if (e.getLocation().distance(currentBlock.getLocation()) < 1) {
                e.setVelocity(e.getVelocity().add(new Vector(0.0, 0.1, 0.0)));
            }
        }
    }
}
