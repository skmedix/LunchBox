package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.Vector3f;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class CraftArmorStand extends CraftLivingEntity implements ArmorStand {

    public CraftArmorStand(CraftServer server, EntityArmorStand entity) {
        super(server, (EntityLiving) entity);
    }

    public String toString() {
        return "CraftArmorStand";
    }

    public EntityType getType() {
        return EntityType.ARMOR_STAND;
    }

    public EntityArmorStand getHandle() {
        return (EntityArmorStand) super.getHandle();
    }

    public ItemStack getItemInHand() {
        return this.getEquipment().getItemInHand();
    }

    public void setItemInHand(ItemStack item) {
        this.getEquipment().setItemInHand(item);
    }

    public ItemStack getBoots() {
        return this.getEquipment().getBoots();
    }

    public void setBoots(ItemStack item) {
        this.getEquipment().setBoots(item);
    }

    public ItemStack getLeggings() {
        return this.getEquipment().getLeggings();
    }

    public void setLeggings(ItemStack item) {
        this.getEquipment().setLeggings(item);
    }

    public ItemStack getChestplate() {
        return this.getEquipment().getChestplate();
    }

    public void setChestplate(ItemStack item) {
        this.getEquipment().setChestplate(item);
    }

    public ItemStack getHelmet() {
        return this.getEquipment().getHelmet();
    }

    public void setHelmet(ItemStack item) {
        this.getEquipment().setHelmet(item);
    }

    public EulerAngle getBodyPose() {
        return fromNMS(this.getHandle().bodyPose);
    }

    public void setBodyPose(EulerAngle pose) {
        this.getHandle().setBodyPose(toNMS(pose));
    }

    public EulerAngle getLeftArmPose() {
        return fromNMS(this.getHandle().leftArmPose);
    }

    public void setLeftArmPose(EulerAngle pose) {
        this.getHandle().setLeftArmPose(toNMS(pose));
    }

    public EulerAngle getRightArmPose() {
        return fromNMS(this.getHandle().rightArmPose);
    }

    public void setRightArmPose(EulerAngle pose) {
        this.getHandle().setRightArmPose(toNMS(pose));
    }

    public EulerAngle getLeftLegPose() {
        return fromNMS(this.getHandle().leftLegPose);
    }

    public void setLeftLegPose(EulerAngle pose) {
        this.getHandle().setLeftLegPose(toNMS(pose));
    }

    public EulerAngle getRightLegPose() {
        return fromNMS(this.getHandle().rightLegPose);
    }

    public void setRightLegPose(EulerAngle pose) {
        this.getHandle().setRightLegPose(toNMS(pose));
    }

    public EulerAngle getHeadPose() {
        return fromNMS(this.getHandle().headPose);
    }

    public void setHeadPose(EulerAngle pose) {
        this.getHandle().setHeadPose(toNMS(pose));
    }

    public boolean hasBasePlate() {
        return !this.getHandle().hasBasePlate();
    }

    public void setBasePlate(boolean basePlate) {
        this.getHandle().setBasePlate(!basePlate);
    }

    public boolean hasGravity() {
        return !this.getHandle().hasGravity();
    }

    public void setGravity(boolean gravity) {
        this.getHandle().setGravity(!gravity);
    }

    public boolean isVisible() {
        return !this.getHandle().isInvisible();
    }

    public void setVisible(boolean visible) {
        this.getHandle().setInvisible(!visible);
    }

    public boolean hasArms() {
        return this.getHandle().hasArms();
    }

    public void setArms(boolean arms) {
        this.getHandle().setArms(arms);
    }

    public boolean isSmall() {
        return this.getHandle().isSmall();
    }

    public void setSmall(boolean small) {
        this.getHandle().setSmall(small);
    }

    private static EulerAngle fromNMS(Vector3f old) {
        return new EulerAngle(Math.toRadians((double) old.getX()), Math.toRadians((double) old.getY()), Math.toRadians((double) old.getZ()));
    }

    private static Vector3f toNMS(EulerAngle old) {
        return new Vector3f((float) Math.toDegrees(old.getX()), (float) Math.toDegrees(old.getY()), (float) Math.toDegrees(old.getZ()));
    }

    public boolean isMarker() {
        return this.getHandle().s();
    }

    public void setMarker(boolean marker) {
        this.getHandle().n(marker);
    }
}
