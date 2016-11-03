/**
 * TameableMonster.java
 * 
 * Created on 16:54:00
 */
package de.kniffo80.mobplugin.entities.monster;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import de.kniffo80.mobplugin.entities.Tameable;

/**
 * @author <a href="mailto:kniffman@googlemail.com">Michael Gertz</a>
 */
public abstract class TameableMonster extends WalkingMonster implements Tameable {

    private Server          server          = null;

    private Player          owner           = null;

    private boolean         sitting         = false;

    public TameableMonster(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.server = Server.getInstance();
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        if (this.namedTag != null) {
            String ownerName = namedTag.getString(NAMED_TAG_OWNER);
            if (ownerName != null && ownerName.length() > 0) {
                Player player = server.getPlayer(ownerName);
                this.setOwner(player);
                this.setSitting(namedTag.getBoolean(NAMED_TAG_SITTING));
            }
        }
        
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        namedTag.putBoolean(NAMED_TAG_SITTING, this.sitting);
        if (this.owner != null) {
            namedTag.putString(NAMED_TAG_OWNER, this.owner.getName());
            namedTag.putString(NAMED_TAG_OWNER_UUID, owner.getUniqueId().toString());
        } else {
            namedTag.putString(NAMED_TAG_OWNER, "");
            namedTag.putString(NAMED_TAG_OWNER_UUID, "");
        }
    }

    @Override
    public Player getOwner() {
        return owner;
    }

    /**
     * Sets the owner of the tameable {@link Entity}
     * @param player the player that is the owner
     */
    public void setOwner(Player player) {
        this.owner = player;
//        setDataProperty(new StringEntityData(DATA_UUID, player.getUniqueId().toString()));
        setTamedDataProperty(true);
    }

    @Override
    public String getName() {
        return getNameTag();
    }

    public boolean isSitting() {
        return sitting;
    }

    public void setSitting(boolean flag) {
        this.sitting = flag;
        setSittingDataProperty(flag);
    }
    
    
    private void setTamedDataProperty (boolean tamed) {
        int var = getDataPropertyByte(DATA_TAMED_FLAG);

        if (tamed) {
            setDataProperty(new ByteEntityData(DATA_TAMED_FLAG, Byte.valueOf((byte) (var | 4))));
        } else {
            setDataProperty(new ByteEntityData(DATA_TAMED_FLAG, Byte.valueOf((byte) (var & -5))));
        }
    }
    
    private void setSittingDataProperty(boolean sit) {
        int var = getDataPropertyByte(DATA_TAMED_FLAG);

        if (sit) {
            setDataProperty(new ByteEntityData(DATA_TAMED_FLAG, (byte) (var | 1)));
        } else {
            setDataProperty(new ByteEntityData(DATA_TAMED_FLAG, (byte) (var & -2)));
        }
    }

}