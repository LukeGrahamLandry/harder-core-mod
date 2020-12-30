package com.lukegraham.hardercore.entities;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

public class WonderingSpiritEntity extends MonsterEntity {
    public WonderingSpiritEntity(EntityType<? extends WonderingSpiritEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public void livingTick() {
        super.livingTick();

        if (!world.isRemote() && !world.isNightTime() && this.world.canSeeSky(this.getPosition())){
            doParticles(ParticleTypes.SMOKE);
            this.remove();
        }
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        doParticles(ParticleTypes.SMOKE);
        this.remove();
    }

    @Override
    protected void onDeathUpdate() {}

    public void doParticles(IParticleData t){
        if (!world.isRemote()) {
            for (int i = 0; i < 20; i++) {
                Vector3d pos = posInBox();
                ((ServerWorld) world).spawnParticle(t, pos.x, pos.y, pos.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    private Vector3d posInBox(){
        AxisAlignedBB box = this.getBoundingBox();
        return box.getCenter().add(randomize(box.getXSize()), randomize(box.getYSize()), randomize(box.getZSize()));
    }

    private double randomize(double d){
        int factor = rand.nextInt(2) == 0 ? 1 : -1;
        return rand.nextFloat() * (d / 2) * factor;
    }


    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.func_234295_eP_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 30.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23F)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 3.0D);
    }


    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isInRangeToRender3d(double x, double y, double z) {
        return super.isInRangeToRender3d(x, y, z);
    }
}
