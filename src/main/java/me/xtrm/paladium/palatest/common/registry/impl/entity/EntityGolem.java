package me.xtrm.paladium.palatest.common.registry.impl.entity;

import me.xtrm.paladium.palatest.client.entity.renderer.RenderGolem;
import me.xtrm.paladium.palatest.common.registry.data.entity.CustomRenderer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

@CustomRenderer(RenderGolem.class)
public class EntityGolem extends EntityMob {
    private final double difficultyFactor;

    public EntityGolem(World worldIn) {
        this(worldIn, 0);
    }

    public EntityGolem(World worldIn, double difficultyFactor) {
        super(worldIn);
        this.difficultyFactor = difficultyFactor;
        setSize(0.6f, 1f);
        preventEntitySpawning = true;

        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(3, new EntityAIWander(this, 0.375D));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();

        getEntityAttribute(SharedMonsterAttributes.followRange)
            .setBaseValue(40 + difficultyFactor * 5);
        getEntityAttribute(SharedMonsterAttributes.attackDamage)
            .setBaseValue(6 + difficultyFactor * .5);
        getEntityAttribute(SharedMonsterAttributes.knockbackResistance)
            .setBaseValue(1D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed)
            .setBaseValue(0.23000000417232513D + difficultyFactor * .05);
        getEntityAttribute(SharedMonsterAttributes.attackDamage)
            .setBaseValue(3.0D + difficultyFactor * .2);
    }

    @Override
    protected boolean isAIEnabled() {
        return true;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }
}
