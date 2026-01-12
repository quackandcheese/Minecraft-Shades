package com.quackandcheese.shades.entity.custom;

import com.quackandcheese.shades.data.ModDataAttachments;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class Shade extends Monster implements TraceableEntity, IEntityWithComplexSpawn {
    public static final String NAME = "shade";

    private UUID associatedPlayer;
    private ListTag storedInventory;

//    public static final EntityDataAccessor<Optional<UUID>> ASSOCIATED_PLAYER =
//            SynchedEntityData.defineId(
//                    Shade.class,
//                    EntityDataSerializers.OPTIONAL_UUID
//            );

    public Shade(EntityType<? extends Shade> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new Shade.ShadeMoveControl(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAssociatedPlayer));
        this.goalSelector.addGoal(2, new FlyChaseAttackGoal());
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 10f, 1F));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 14d)
                .add(Attributes.ATTACK_DAMAGE, 4d)
                .add(Attributes.FOLLOW_RANGE, 10d);
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buf) {
        buf.writeUUID(associatedPlayer);
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf buf) {
        associatedPlayer = buf.readUUID();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putUUID("AssociatedPlayer", associatedPlayer);
        compound.put("StoredInventory", storedInventory);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        associatedPlayer = compound.getUUID("AssociatedPlayer");
        storedInventory = compound.getList("StoredInventory", CompoundTag.TAG_COMPOUND);
    }


    public UUID getAssociatedPlayer() {
        return associatedPlayer;
    }

    public void setAssociatedPlayer(UUID playerUUID) {
        this.associatedPlayer = playerUUID;
    }

    public ListTag getStoredInventory() {
        return this.storedInventory;
    }

    public void setStoredInventory(ListTag inventoryNBT) {
        this.storedInventory = inventoryNBT;
    }

    @Override
    public void tick() {
        this.setNoGravity(true);
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
    }

    @Override
    public void move(MoverType type, Vec3 pos) {
        super.move(type, pos);
        this.checkInsideBlocks();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        var damageSourceEntity = source.getEntity();
        if (damageSourceEntity == null)
            return super.isInvulnerableTo(source);

        if (isAssociatedPlayerOnline()) { // ensures associated player is online
            UUID damageCauser = damageSourceEntity.getUUID();
            if (!damageCauser.equals(associatedPlayer)) {
                return true; // only allow associated player to damage
            }
        }

        return super.isInvulnerableTo(source);
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
        Optional<Entity> owner = Optional.ofNullable(getOwner());
        if (owner.isEmpty())
            return;
        Optional<UUID> shadeUUID = owner.get().getData(ModDataAttachments.SHADE).shadeUuid();
        if (shadeUUID.isPresent() && shadeUUID.get().equals(associatedPlayer)) {
            owner.get().setData(ModDataAttachments.SHADE, ModDataAttachments.ShadeAttachment.EMPTY); // set shade to empty in player
        }
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);

        //player.getInventory().load(storedInventory);
        dropAllItems();
    }

    public void dropAllItems() {
        getItemsFromStoredInventory(storedInventory).forEach(this::spawnAtLocation); // drop all items in shade
    }

    public NonNullList<ItemStack> getItemsFromStoredInventory(ListTag inventoryNBT) {
        NonNullList<ItemStack> result = NonNullList.withSize(41, ItemStack.EMPTY);
        for (int i = 0; i < inventoryNBT.size(); i++) {
            int index = i;
            ItemStack.parse(registryAccess(), inventoryNBT.getCompound(i)).ifPresent(itemStack -> {
                result.set(index, itemStack);
            });
        }
        return result;
    }

    @Override
    public @Nullable Entity getOwner() {
        return level().getPlayerByUUID(associatedPlayer);
    }

    private boolean isAssociatedPlayerOnline() {
        return getOwner() != null;
    }

    private boolean isAssociatedPlayer(LivingEntity entity) {
        if (entity == null)
            return false;
        return associatedPlayer.equals(entity.getUUID());
    }

    class FlyChaseAttackGoal extends Goal {
        private int ticksUntilNextAttack;
        private final int attackInterval = 20;

        @Override
        public boolean canUse() {
            LivingEntity target = Shade.this.getTarget();
            return target != null
                    && target.isAlive()
                    && target.getUUID().equals(Shade.this.associatedPlayer)
                    && !Shade.this.getMoveControl().hasWanted();
        }

        @Override
        public boolean canContinueToUse() {
            return canUse();
        }

        @Override
        public void start() {
            LivingEntity target = Shade.this.getTarget();
            if (target != null) {
                Vec3 targetPos = target.trackingPosition();
                Shade.this.moveControl.setWantedPosition(targetPos.x, targetPos.y, targetPos.z, 1.0);
            }

            //Shade.this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = Shade.this.getTarget();
            if (target != null) {
                if (!Shade.this.getBoundingBox().intersects(target.getBoundingBox())) {
                    double stopDistSq = Shade.this.distanceToSqr(target);
                    if (stopDistSq < 9.0) {
                        Vec3 targetPos = target.trackingPosition();
                        Shade.this.moveControl.setWantedPosition(targetPos.x, targetPos.y, targetPos.z, 1.0);
                    }
                }
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
                this.checkAndPerformAttack(target);
            }
        }

        protected void checkAndPerformAttack(LivingEntity target) {
            if (this.canPerformAttack(target)) {
                this.resetAttackCooldown();
                Shade.this.swing(InteractionHand.MAIN_HAND);
                Shade.this.doHurtTarget(target);
            }
        }

        protected void resetAttackCooldown() {
            this.ticksUntilNextAttack = this.adjustedTickDelay(20);
        }

        protected boolean isTimeToAttack() {
            return this.ticksUntilNextAttack <= 0;
        }

        protected boolean canPerformAttack(LivingEntity entity) {
            return this.isTimeToAttack() && Shade.this.isWithinMeleeAttackRange(entity) && Shade.this.getSensing().hasLineOfSight(entity);
        }
    }

    class ShadeMoveControl extends MoveControl {
        public ShadeMoveControl(Shade shade) {
            super(shade);
        }

        @Override
        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                Vec3 vec3 = new Vec3(this.wantedX - Shade.this.getX(), this.wantedY - Shade.this.getY(), this.wantedZ - Shade.this.getZ());
                double d0 = vec3.length();
                if (d0 < Shade.this.getBoundingBox().getSize()) {
                    this.operation = MoveControl.Operation.WAIT;
                    Shade.this.setDeltaMovement(Shade.this.getDeltaMovement().scale(0.5));
                } else {
                    Shade.this.setDeltaMovement(Shade.this.getDeltaMovement().add(vec3.scale(this.speedModifier * 0.05 / d0)));
                    if (Shade.this.getTarget() == null) {
                        Vec3 vec31 = Shade.this.getDeltaMovement();
                        Shade.this.setYRot(-((float) Mth.atan2(vec31.x, vec31.z)) * (180.0F / (float)Math.PI));
                        Shade.this.yBodyRot = Shade.this.getYRot();
                    } else {
                        double d2 = Shade.this.getTarget().getX() - Shade.this.getX();
                        double d1 = Shade.this.getTarget().getZ() - Shade.this.getZ();
                        Shade.this.setYRot(-((float)Mth.atan2(d2, d1)) * (180.0F / (float)Math.PI));
                        Shade.this.yBodyRot = Shade.this.getYRot();
                    }
                }
            }
        }
    }
}
